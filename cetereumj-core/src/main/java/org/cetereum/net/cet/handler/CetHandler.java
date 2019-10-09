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
package org.cetereum.net.cet.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.cetereum.db.BlockStore;
import org.cetereum.listener.CetereumListener;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.*;
import org.cetereum.listener.CompositeCetereumListener;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.net.MessageQueue;
import org.cetereum.net.cet.CetVersion;
import org.cetereum.net.cet.message.*;
import org.cetereum.net.message.ReasonCode;
import org.cetereum.net.server.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Process the messages between peers with 'cet' capability on the network<br>
 * Contains common logic to all supported versions
 * delegating version specific stuff to its descendants
 *
 */
public abstract class CetHandler extends SimpleChannelInboundHandler<CetMessage> implements Cet {

    private final static Logger logger = LoggerFactory.getLogger("net");

    protected Blockchain blockchain;

    protected SystemProperties config;

    protected CompositeCetereumListener cetereumListener;

    protected Channel channel;

    private MessageQueue msgQueue = null;

    protected CetVersion version;

    protected boolean peerDiscoveryMode = false;

    protected Block bestBlock;
    protected CetereumListener listener = new CetereumListenerAdapter() {
        @Override
        public void onBlock(Block block, List<TransactionReceipt> receipts) {
            bestBlock = block;
        }
    };

    protected boolean processTransactions = false;

    protected CetHandler(CetVersion version) {
        this.version = version;
    }

    protected CetHandler(final CetVersion version, final SystemProperties config,
                         final Blockchain blockchain, final BlockStore blockStore,
                         final CompositeCetereumListener cetereumListener) {
        this.version = version;
        this.config = config;
        this.cetereumListener = cetereumListener;
        this.blockchain = blockchain;
        bestBlock = blockStore.getBestBlock();
        this.cetereumListener.addListener(listener);
        // when sync enabled we delay transactions processing until sync is complete
        processTransactions = !config.isSyncEnabled();
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, CetMessage msg) throws InterruptedException {

        if (CetMessageCodes.inRange(msg.getCommand().asByte(), version))
            logger.trace("CetHandler invoke: [{}]", msg.getCommand());

        cetereumListener.trace(String.format("CetHandler invoke: [%s]", msg.getCommand()));

        channel.getNodeStatistics().cetInbound.add();

        msgQueue.receivedMessage(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Cet handling failed", cause);
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.debug("handlerRemoved: kill timers in CetHandler");
        cetereumListener.removeListener(listener);
        onShutdown();
    }

    public void activate() {
        logger.debug("CET protocol activated");
        cetereumListener.trace("CET protocol activated");
        sendStatus();
    }

    protected void disconnect(ReasonCode reason) {
        msgQueue.disconnect(reason);
        channel.getNodeStatistics().nodeDisconnectedLocal(reason);
    }

    protected void sendMessage(CetMessage message) {
        msgQueue.sendMessage(message);
        channel.getNodeStatistics().cetOutbound.add();
    }

    public StatusMessage getHandshakeStatusMessage() {
        return channel.getNodeStatistics().getCetLastInboundStatusMsg();
    }

    public void setMsgQueue(MessageQueue msgQueue) {
        this.msgQueue = msgQueue;
    }

    public void setPeerDiscoveryMode(boolean peerDiscoveryMode) {
        this.peerDiscoveryMode = peerDiscoveryMode;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public CetVersion getVersion() {
        return version;
    }

}