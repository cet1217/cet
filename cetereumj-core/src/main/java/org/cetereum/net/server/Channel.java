/*
 * Copyright (c) [2016] [ <ceter.camp> ]
 * This file is part of the cetereumJ library.
 *
 * The cetereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The cetereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the cetereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cetereum.net.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.Block;
import org.cetereum.core.BlockHeaderWrapper;
import org.cetereum.core.Transaction;
import org.cetereum.db.ByteArrayWrapper;
import org.cetereum.net.MessageQueue;
import org.cetereum.net.client.Capability;
import org.cetereum.net.cet.handler.Cet;
import org.cetereum.net.cet.handler.CetAdapter;
import org.cetereum.net.cet.handler.CetHandler;
import org.cetereum.net.cet.handler.CetHandlerFactory;
import org.cetereum.net.cet.CetVersion;
import org.cetereum.net.cet.message.Cet62MessageFactory;
import org.cetereum.net.cet.message.Cet63MessageFactory;
import org.cetereum.net.message.ReasonCode;
import org.cetereum.net.rlpx.*;
import org.cetereum.sync.SyncStatistics;
import org.cetereum.net.message.MessageFactory;
import org.cetereum.net.message.StaticMessages;
import org.cetereum.net.p2p.HelloMessage;
import org.cetereum.net.p2p.P2pHandler;
import org.cetereum.net.p2p.P2pMessageFactory;
import org.cetereum.net.rlpx.discover.NodeManager;
import org.cetereum.net.rlpx.discover.NodeStatistics;
import org.cetereum.net.shh.ShhHandler;
import org.cetereum.net.shh.ShhMessageFactory;
import org.cetereum.net.swarm.bzz.BzzHandler;
import org.cetereum.net.swarm.bzz.BzzMessageFactory;
import org.cetereum.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Roman Mandeleil
 * @since 01.11.2014
 */
@Component
@Scope("prototype")
public class Channel {

    private final static Logger logger = LoggerFactory.getLogger("net");

    @Autowired
    SystemProperties config;

    @Autowired
    private MessageQueue msgQueue;

    @Autowired
    private P2pHandler p2pHandler;

    @Autowired
    private ShhHandler shhHandler;

    @Autowired
    private BzzHandler bzzHandler;

    @Autowired
    private MessageCodec messageCodec;

    @Autowired
    private HandshakeHandler handshakeHandler;

    @Autowired
    private NodeManager nodeManager;

    @Autowired
    private CetHandlerFactory cetHandlerFactory;

    @Autowired
    private StaticMessages staticMessages;

    @Autowired
    private WireTrafficStats stats;

    private ChannelManager channelManager;

    private Cet cet = new CetAdapter();

    private InetSocketAddress inetSocketAddress;

    private Node node;
    private NodeStatistics nodeStatistics;

    private boolean discoveryMode;
    private boolean isActive;
    private boolean isDisconnected;

    private String remoteId;

    private PeerStatistics peerStats = new PeerStatistics();

    public static final int MAX_SAFE_TXS = 192;

    public void init(ChannelPipeline pipeline, String remoteId, boolean discoveryMode, ChannelManager channelManager) {
        this.channelManager = channelManager;
        this.remoteId = remoteId;

        isActive = remoteId != null && !remoteId.isEmpty();

        pipeline.addLast("readTimeoutHandler",
                new ReadTimeoutHandler(config.peerChannelReadTimeout(), TimeUnit.SECONDS));
        pipeline.addLast(stats.tcp);
        pipeline.addLast("handshakeHandler", handshakeHandler);

        this.discoveryMode = discoveryMode;

        if (discoveryMode) {
            // temporary key/nodeId to not accidentally smear our reputation with
            // unexpected disconnect
//            handshakeHandler.generateTempKey();
        }

        handshakeHandler.setRemoteId(remoteId, this);

        messageCodec.setChannel(this);

        msgQueue.setChannel(this);

        p2pHandler.setMsgQueue(msgQueue);
        messageCodec.setP2pMessageFactory(new P2pMessageFactory());

        shhHandler.setMsgQueue(msgQueue);
        messageCodec.setShhMessageFactory(new ShhMessageFactory());

        bzzHandler.setMsgQueue(msgQueue);
        messageCodec.setBzzMessageFactory(new BzzMessageFactory());
    }

    public void publicRLPxHandshakeFinished(ChannelHandlerContext ctx, FrameCodec frameCodec,
                                            HelloMessage helloRemote) throws IOException, InterruptedException {

        logger.debug("publicRLPxHandshakeFinished with " + ctx.channel().remoteAddress());

        messageCodec.setSupportChunkedFrames(false);

        FrameCodecHandler frameCodecHandler = new FrameCodecHandler(frameCodec, this);
        ctx.pipeline().addLast("medianFrameCodec", frameCodecHandler);

        if (SnappyCodec.isSupported(Math.min(config.defaultP2PVersion(), helloRemote.getP2PVersion()))) {
            ctx.pipeline().addLast("snappyCodec", new SnappyCodec(this));
            logger.debug("{}: use snappy compression", ctx.channel());
        }

        ctx.pipeline().addLast("messageCodec", messageCodec);
        ctx.pipeline().addLast(Capability.P2P, p2pHandler);

        p2pHandler.setChannel(this);
        p2pHandler.setHandshake(helloRemote, ctx);

        getNodeStatistics().rlpxHandshake.add();
    }

    public void sendHelloMessage(ChannelHandlerContext ctx, FrameCodec frameCodec,
                                 String nodeId) throws IOException, InterruptedException {

        final HelloMessage helloMessage = staticMessages.createHelloMessage(nodeId);

        ByteBuf byteBufMsg = ctx.alloc().buffer();
        frameCodec.writeFrame(new FrameCodec.Frame(helloMessage.getCode(), helloMessage.getEncoded()), byteBufMsg);
        ctx.writeAndFlush(byteBufMsg).sync();

        if (logger.isDebugEnabled())
            logger.debug("To:   {}    Send:  {}", ctx.channel().remoteAddress(), helloMessage);
        getNodeStatistics().rlpxOutHello.add();
    }

    public void activateCet(ChannelHandlerContext ctx, CetVersion version) {
        CetHandler handler = cetHandlerFactory.create(version);
        MessageFactory messageFactory = createCetMessageFactory(version);
        messageCodec.setCetVersion(version);
        messageCodec.setCetMessageFactory(messageFactory);

        logger.debug("Cet{} [ address = {} | id = {} ]", handler.getVersion(), inetSocketAddress, getPeerIdShort());

        ctx.pipeline().addLast(Capability.CET, handler);

        handler.setMsgQueue(msgQueue);
        handler.setChannel(this);
        handler.setPeerDiscoveryMode(discoveryMode);

        handler.activate();

        cet = handler;
    }

    private MessageFactory createCetMessageFactory(CetVersion version) {
        switch (version) {
            case V62:   return new Cet62MessageFactory();
            case V63:   return new Cet63MessageFactory();
            default:    throw new IllegalArgumentException("Cet " + version + " is not supported");
        }
    }

    public void activateShh(ChannelHandlerContext ctx) {
        ctx.pipeline().addLast(Capability.SHH, shhHandler);
        shhHandler.activate();
    }

    public void activateBzz(ChannelHandlerContext ctx) {
        ctx.pipeline().addLast(Capability.BZZ, bzzHandler);
        bzzHandler.activate();
    }

    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    public NodeStatistics getNodeStatistics() {
        return nodeStatistics;
    }

    /**
     * Set node and register it in NodeManager if it is not registered yet.
     */
    public void initWithNode(byte[] nodeId, int remotePort) {
        node = new Node(nodeId, inetSocketAddress.getHostString(), remotePort);
        nodeStatistics = nodeManager.getNodeStatistics(node);
    }

    public void initWithNode(byte[] nodeId) {
        initWithNode(nodeId, inetSocketAddress.getPort());
    }

    public Node getNode() {
        return node;
    }

    public void initMessageCodes(List<Capability> caps) {
        messageCodec.initMessageCodes(caps);
    }

    public boolean isProtocolsInitialized() {
        return cet.hasStatusPassed();
    }

    public void onDisconnect() {
        isDisconnected = true;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void onSyncDone(boolean done) {

        if (done) {
            cet.enableTransactions();
        } else {
            cet.disableTransactions();
        }

        cet.onSyncDone(done);
    }

    public boolean isDiscoveryMode() {
        return discoveryMode;
    }

    public String getPeerId() {
        return node == null ? "<null>" : node.getHexId();
    }

    public String getPeerIdShort() {
        return node == null ? (remoteId != null && remoteId.length() >= 8 ? remoteId.substring(0,8) :remoteId)
                : node.getHexIdShort();
    }

    public byte[] getNodeId() {
        return node == null ? null : node.getId();
    }

    /**
     * Indicates whceter this connection was initiated by our peer
     */
    public boolean isActive() {
        return isActive;
    }

    public ByteArrayWrapper getNodeIdWrapper() {
        return node == null ? null : new ByteArrayWrapper(node.getId());
    }

    public void disconnect(ReasonCode reason) {
        getNodeStatistics().nodeDisconnectedLocal(reason);
        msgQueue.disconnect(reason);
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public PeerStatistics getPeerStats() {
        return peerStats;
    }

    // CET sub protocol

    public void fetchBlockBodies(List<BlockHeaderWrapper> headers) {
        cet.fetchBodies(headers);
    }

    public boolean isCetCompatible(Channel peer) {
        return peer != null && peer.getCetVersion().isCompatible(getCetVersion());
    }

    public Cet getCetHandler() {
        return cet;
    }

    public boolean hasCetStatusSucceeded() {
        return cet.hasStatusSucceeded();
    }

    public String logSyncStats() {
        return cet.getSyncStats();
    }

    public BigInteger getTotalDifficulty() {
        return getCetHandler().getTotalDifficulty();
    }

    public SyncStatistics getSyncStats() {
        return cet.getStats();
    }

    public boolean isHashRetrievingDone() {
        return cet.isHashRetrievingDone();
    }

    public boolean isHashRetrieving() {
        return cet.isHashRetrieving();
    }

    public boolean isMaster() {
        return cet.isHashRetrieving() || cet.isHashRetrievingDone();
    }

    public boolean isIdle() {
        return cet.isIdle();
    }

    public void prohibitTransactionProcessing() {
        cet.disableTransactions();
    }

    /**
     * Send transactions from input to peer corresponded with channel
     * Using {@link #sendTransactionsCapped(List)} is recommended instead
     * @param txs   Transactions
     */
    public void sendTransactions(List<Transaction> txs) {
        cet.sendTransaction(txs);
    }

    /**
     * Sames as {@link #sendTransactions(List)} but input list is randomly sliced to
     * contain not more than {@link #MAX_SAFE_TXS} if needed
     * @param txs   List of txs to send
     */
    public void sendTransactionsCapped(List<Transaction> txs) {
        List<Transaction> slicedTxs;
        if (txs.size() <= MAX_SAFE_TXS) {
            slicedTxs = txs;
        } else {
            slicedTxs = CollectionUtils.truncateRand(txs, MAX_SAFE_TXS);
        }
        cet.sendTransaction(slicedTxs);
    }

    public void sendNewBlock(Block block) {
        cet.sendNewBlock(block);
    }

    public void sendNewBlockHashes(Block block) {
        cet.sendNewBlockHashes(block);
    }

    public CetVersion getCetVersion() {
        return cet.getVersion();
    }

    public void dropConnection() {
        cet.dropConnection();
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;


        if (inetSocketAddress != null ? !inetSocketAddress.equals(channel.inetSocketAddress) : channel.inetSocketAddress != null) return false;
        if (node != null ? !node.equals(channel.node) : channel.node != null) return false;
        return false;
    }

    @Override
    public int hashCode() {
        int result = inetSocketAddress != null ? inetSocketAddress.hashCode() : 0;
        result = 31 * result + (node != null ? node.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s | %s", getPeerIdShort(), inetSocketAddress);
    }
}
