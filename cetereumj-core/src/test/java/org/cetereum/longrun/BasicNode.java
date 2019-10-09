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
package org.cetereum.longrun;

import org.cetereum.config.CommonConfig;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.Block;
import org.cetereum.core.TransactionReceipt;
import org.cetereum.db.DbFlushManager;
import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.listener.CetereumListener;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.net.cet.message.StatusMessage;
import org.cetereum.net.rlpx.Node;
import org.cetereum.net.server.Channel;
import org.cetereum.sync.SyncPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static java.lang.Thread.sleep;

/**
 * BasicNode of cetereum instance
 */
class BasicNode implements Runnable {
    static final Logger sLogger = LoggerFactory.getLogger("sample");

    private String loggerName;
    public Logger logger;

    @Autowired
    protected Cetereum cetereum;

    @Autowired
    protected SystemProperties config;

    @Autowired
    protected SyncPool syncPool;

    @Autowired
    protected CommonConfig commonConfig;

    @Autowired
    protected DbFlushManager dbFlushManager;

    // Spring config class which add this sample class as a bean to the components collections
    // and make it possible for autowiring other components
    private static class Config {
        @Bean
        public BasicNode basicSample() {
            return new BasicNode();
        }
    }

    public static void main(String[] args) throws Exception {
        sLogger.info("Starting CetereumJ!");

        // Based on Config class the BasicNode would be created by Spring
        // and its springInit() mcetod would be called as an entry point
        CetereumFactory.createCetereum(Config.class);
    }

    public BasicNode() {
        this("sample");
    }

    /**
     * logger name can be passed if more than one CetereumJ instance is created
     * in a single JVM to distinguish logging output from different instances
     */
    public BasicNode(String loggerName) {
        this.loggerName = loggerName;
    }

    /**
     * The mcetod is called after all CetereumJ instances are created
     */
    @PostConstruct
    private void springInit() {
        logger = LoggerFactory.getLogger(loggerName);
        // adding the main CetereumJ callback to be notified on different kind of events
        cetereum.addListener(listener);

        logger.info("Sample component created. Listening for cetereum events...");

        // starting lifecycle tracking mcetod run()
        new Thread(this, "SampleWorkThread").start();
    }

    /**
     * The mcetod tracks step-by-step the instance lifecycle from node discovery till sync completion.
     * At the end the mcetod onSyncDone() is called which might be overridden by a sample subclass
     * to start making other things with the Cetereum network
     */
    public void run() {
        try {
            logger.info("Sample worker thread started.");

            if (!config.peerDiscovery()) {
                logger.info("Peer discovery disabled. We should actively connect to another peers or wait for incoming connections");
            }

            waitForSync();

            onSyncDone();

        } catch (Exception e) {
            logger.error("Error occurred in Sample: ", e);
        }
    }


    /**
     * Waits until the whole blockchain sync is complete
     */
    public void waitForSync() throws Exception {
        logger.info("Waiting for the whole blockchain sync (will take up to an hour on fast sync for the whole chain)...");
        while(true) {
            sleep(10000);
            if (syncComplete) {
                logger.info("[v] Sync complete! The best block: " + bestBlock.getShortDescr());
                return;
            }
        }
    }

    /**
     * Is called when the whole blockchain sync is complete
     */
    public void onSyncDone() throws Exception {
        logger.info("Monitoring new blocks in real-time...");
    }

    public void onSyncDoneImpl(CetereumListener.SyncState state) {
        logger.info("onSyncDone: " + state);
    }

    protected Map<Node, StatusMessage> cetNodes = new Hashtable<>();
    protected List<Node> syncPeers = new Vector<>();

    protected Block bestBlock = null;

    CetereumListener.SyncState syncState = null;
    boolean syncComplete = false;

    /**
     * The main CetereumJ callback.
     */
    CetereumListener listener = new CetereumListenerAdapter() {
        @Override
        public void onSyncDone(SyncState state) {
            syncState = state;
            if (state.equals(SyncState.COMPLETE)) syncComplete = true;
            onSyncDoneImpl(state);
        }

        @Override
        public void onCetStatusUpdated(Channel channel, StatusMessage statusMessage) {
            cetNodes.put(channel.getNode(), statusMessage);
        }

        @Override
        public void onPeerAddedToSyncPool(Channel peer) {
            syncPeers.add(peer.getNode());
        }

        @Override
        public void onBlock(Block block, List<TransactionReceipt> receipts) {
            bestBlock = block;

            if (syncComplete) {
                logger.info("New block: " + block.getShortDescr());
            }
        }
    };
}
