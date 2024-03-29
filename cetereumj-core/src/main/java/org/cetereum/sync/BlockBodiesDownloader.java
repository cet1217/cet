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
package org.cetereum.sync;

import org.cetereum.config.SystemProperties;
import org.cetereum.core.*;
import org.cetereum.crypto.HashUtil;
import org.cetereum.db.DbFlushManager;
import org.cetereum.db.HeaderStore;
import org.cetereum.db.IndexedBlockStore;
import org.cetereum.net.server.Channel;
import org.cetereum.util.FastByteComparisons;
import org.cetereum.validator.BlockHeaderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anton Nashatyrev on 27.10.2016.
 */
@Component
@Scope("prototype")
public class BlockBodiesDownloader extends BlockDownloader {
    private final static Logger logger = LoggerFactory.getLogger("sync");

    public final static byte[] EMPTY_BODY = new byte[] {-62, -64, -64};

    @Autowired
    SyncPool syncPool;

    @Autowired
    IndexedBlockStore blockStore;

    @Autowired
    HeaderStore headerStore;

    @Autowired
    DbFlushManager dbFlushManager;

    long t;

    SyncQueueIfc syncQueue;
    int curBlockIdx = 1;
    BigInteger curTotalDiff;

    Thread headersThread;
    int downloadCnt = 0;

    private long blockBytesLimit = 32 * 1024 * 1024;

    @Autowired
    public BlockBodiesDownloader(final SystemProperties config, BlockHeaderValidator headerValidator) {
        super(headerValidator);
        blockBytesLimit = config.blockQueueSize();
    }

    public void startImporting() {
        Block genesis = blockStore.getChainBlockByNumber(0);
        syncQueue = new SyncQueueImpl(Collections.singletonList(genesis));
        curTotalDiff = genesis.getDifficultyBI();

        headersThread = new Thread(this::headerLoop, "FastsyncHeadersFetchThread");
        headersThread.start();

        setHeadersDownload(false);

        init(syncQueue, syncPool, "BlockBodiesDownloader");
    }

    private void headerLoop() {
        while (curBlockIdx < headerStore.size() && !Thread.currentThread().isInterrupted()) {
            List<BlockHeaderWrapper> wrappers = new ArrayList<>();
            List<BlockHeader> emptyBodyHeaders =  new ArrayList<>();
            for (int i = 0; i < getMaxHeadersInQueue() - syncQueue.getHeadersCount() && curBlockIdx < headerStore.size(); i++) {
                BlockHeader header = headerStore.getHeaderByNumber(curBlockIdx);
                ++curBlockIdx;
                wrappers.add(new BlockHeaderWrapper(header, new byte[0]));

                // Skip bodies download for blocks with empty body
                boolean emptyBody = FastByteComparisons.equal(header.getTxTrieRoot(), HashUtil.EMPTY_TRIE_HASH);
                emptyBody &= FastByteComparisons.equal(header.getUnclesHash(), HashUtil.EMPTY_LIST_HASH);
                if (emptyBody) emptyBodyHeaders.add(header);
            }

            synchronized (this) {
                syncQueue.addHeaders(wrappers);
                if (!emptyBodyHeaders.isEmpty()) {
                    addEmptyBodyBlocks(emptyBodyHeaders);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        headersDownloadComplete = true;
    }

    private void addEmptyBodyBlocks(List<BlockHeader> blockHeaders) {
        logger.debug("Adding {} empty body blocks to sync queue: {} ... {}", blockHeaders.size(),
                blockHeaders.get(0).getShortDescr(), blockHeaders.get(blockHeaders.size() - 1).getShortDescr());

        List<Block> finishedBlocks = new ArrayList<>();
        for (BlockHeader header : blockHeaders) {
            Block block = new Block.Builder()
                    .withHeader(header)
                    .withBody(EMPTY_BODY)
                    .create();
            finishedBlocks.add(block);
        }

        List<Block> startTrimmedBlocks = syncQueue.addBlocks(finishedBlocks);
        List<BlockWrapper> trimmedBlockWrappers = new ArrayList<>();
        for (Block b : startTrimmedBlocks) {
            trimmedBlockWrappers.add(new BlockWrapper(b, null));
        }

        pushBlocks(trimmedBlockWrappers);
    }

    @Override
    protected void pushBlocks(List<BlockWrapper> blockWrappers) {
        if (!blockWrappers.isEmpty()) {

            for (BlockWrapper blockWrapper : blockWrappers) {
                curTotalDiff = curTotalDiff.add(blockWrapper.getBlock().getDifficultyBI());
                blockStore.saveBlock(blockWrapper.getBlock(), curTotalDiff, true);
                downloadCnt++;
            }
            dbFlushManager.commit();

            estimateBlockSize(blockWrappers);
            logger.debug("{}: header queue size {} (~{}mb)", name, syncQueue.getHeadersCount(),
                    syncQueue.getHeadersCount() * getEstimatedBlockSize() / 1024 / 1024);

            long c = System.currentTimeMillis();
            if (c - t > 5000) {
                t = c;
                logger.info("FastSync: downloaded blocks. Last: " + blockWrappers.get(blockWrappers.size() - 1).getBlock().getShortDescr());
            }
        }
    }

    /**
     * Download could block chain synchronization occupying all peers
     * Prevents this by leaving one peer without work
     * Fallbacks to any peer when low number of active peers available
     */
    @Override
    Channel getAnyPeer() {
        return syncPool.getActivePeersCount() > 2 ? syncPool.getNotLastIdle() : syncPool.getAnyIdle();
    }

    @Override
    protected void pushHeaders(List<BlockHeaderWrapper> headers) {}

    @Override
    protected int getBlockQueueFreeSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaxHeadersInQueue() {
        if (getEstimatedBlockSize() == 0) {
            return getHeaderQueueLimit();
        }

        int slotsLeft = Math.max((int) (blockBytesLimit / getEstimatedBlockSize()), MAX_IN_REQUEST);
        return Math.min(slotsLeft + MAX_IN_REQUEST, getHeaderQueueLimit());
    }

    public int getDownloadedCount() {
        return downloadCnt;
    }

    @Override
    public void stop() {
        headersThread.interrupt();
        super.stop();
    }

    @Override
    protected void finishDownload() {
        stop();
    }
}
