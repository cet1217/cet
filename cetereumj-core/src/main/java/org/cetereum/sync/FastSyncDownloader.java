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
import org.cetereum.core.BlockHeader;
import org.cetereum.core.BlockHeaderWrapper;
import org.cetereum.core.BlockWrapper;
import org.cetereum.db.IndexedBlockStore;
import org.cetereum.validator.BlockHeaderValidator;
import org.cetereum.validator.CetashRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Anton Nashatyrev on 27.10.2016.
 */
@Component
@Scope("prototype")
public class FastSyncDownloader extends BlockDownloader {
    private final static Logger logger = LoggerFactory.getLogger("sync");

    @Autowired
    SyncPool syncPool;

    @Autowired
    IndexedBlockStore blockStore;

    private SyncQueueReverseImpl syncQueueReverse;

    private CetashRule reverseCetashRule;

    int counter;
    int maxCount;
    long t;

    @Autowired
    public FastSyncDownloader(BlockHeaderValidator headerValidator, SystemProperties systemProperties) {
        super(headerValidator);
        reverseCetashRule = CetashRule.createReverse(systemProperties);
    }

    public void startImporting(BlockHeader start, int count) {
        this.maxCount = count <= 0 ? Integer.MAX_VALUE : count;
        setHeaderQueueLimit(maxCount);
        setBlockQueueLimit(maxCount);

        syncQueueReverse = new SyncQueueReverseImpl(start.getHash(), start.getNumber() - count);
        init(syncQueueReverse, syncPool, "FastSync");
    }

    @Override
    protected void pushBlocks(List<BlockWrapper> blockWrappers) {
        if (!blockWrappers.isEmpty()) {

            for (BlockWrapper blockWrapper : blockWrappers) {
                blockStore.saveBlock(blockWrapper.getBlock(), BigInteger.ZERO, true);
                counter++;
                if (counter >= maxCount) {
                    logger.info("FastSync: All requested " + counter + " blocks are downloaded. (last " +
                            blockWrapper.getBlock().getShortDescr() + ")");
                    stop();
                    break;
                }
            }

            long c = System.currentTimeMillis();
            if (c - t > 5000) {
                t = c;
                logger.info("FastSync: downloaded " + counter + " blocks so far. Last: " +
                        blockWrappers.get(blockWrappers.size() - 1).getBlock().getShortDescr());
                blockStore.flush();
            }
        }
    }

    @Override
    protected void pushHeaders(List<BlockHeaderWrapper> headers) {}

    @Override
    protected int getBlockQueueFreeSize() {
        return Math.max(maxCount - counter, MAX_IN_REQUEST);
    }

    @Override
    protected int getMaxHeadersInQueue() {
        return Math.max(maxCount - syncQueueReverse.getValidatedHeadersCount(), 0);
    }

    // TODO: receipts loading here

    public int getDownloadedBlocksCount() {
        return counter;
    }

    @Override
    protected void finishDownload() {
        blockStore.flush();
    }

    @Override
    protected boolean isValid(BlockHeader header) {
        return super.isValid(header) && reverseCetashRule.validateAndLog(header, logger);
    }
}
