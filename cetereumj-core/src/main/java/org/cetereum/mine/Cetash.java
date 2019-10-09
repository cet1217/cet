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
package org.cetereum.mine;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.Block;
import org.cetereum.core.BlockHeader;
import org.cetereum.util.ByteUtil;
import org.cetereum.util.FastByteComparisons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.cetereum.crypto.HashUtil.sha3;
import static org.cetereum.mine.CetashListener.DatasetStatus.DATASET_READY;
import static org.cetereum.mine.CetashListener.DatasetStatus.DATASET_PREPARE;
import static org.cetereum.mine.CetashListener.DatasetStatus.FULL_DATASET_GENERATED;
import static org.cetereum.mine.CetashListener.DatasetStatus.FULL_DATASET_GENERATE_START;
import static org.cetereum.mine.CetashListener.DatasetStatus.FULL_DATASET_LOADED;
import static org.cetereum.mine.CetashListener.DatasetStatus.FULL_DATASET_LOAD_START;
import static org.cetereum.mine.CetashListener.DatasetStatus.LIGHT_DATASET_GENERATED;
import static org.cetereum.mine.CetashListener.DatasetStatus.LIGHT_DATASET_GENERATE_START;
import static org.cetereum.mine.CetashListener.DatasetStatus.LIGHT_DATASET_LOADED;
import static org.cetereum.mine.CetashListener.DatasetStatus.LIGHT_DATASET_LOAD_START;
import static org.cetereum.util.ByteUtil.longToBytes;
import static org.cetereum.mine.MinerIfc.MiningResult;

/**
 * More high level validator/miner class which keeps a cache for the last requested block epoch
 *
 * Created by Anton Nashatyrev on 04.12.2015.
 */
public class Cetash {
    private static final Logger logger = LoggerFactory.getLogger("mine");
    static CetashParams cetashParams = new CetashParams();

    static Cetash cachedInstance = null;
    long epoch = 0;
    //    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static ListeningExecutorService executor = MoreExecutors.listeningDecorator(
            new ThreadPoolExecutor(8, 8, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("cetash-pool-%d").build()));

    public static boolean fileCacheEnabled = true;

    private Set<CetashListener> listeners = new CopyOnWriteArraySet <>();

    /**
     * Returns instance for the specified block number
     * either from cache or calculates a new one
     */
    public static Cetash getForBlock(SystemProperties config, long blockNumber) {
        long epoch = blockNumber / cetashParams.getEPOCH_LENGTH();
        if (cachedInstance == null || epoch != cachedInstance.epoch) {
            cachedInstance = new Cetash(config, epoch * cetashParams.getEPOCH_LENGTH());
        }
        return cachedInstance;
    }

    /**
     * Returns instance for the specified block number
     * either from cache or calculates a new one
     * and adds listeners to Cetash
     */
    public static Cetash getForBlock(SystemProperties config, long blockNumber, Collection<CetashListener> listeners) {
        Cetash cetash = getForBlock(config, blockNumber);
        cetash.listeners.clear();
        cetash.listeners.addAll(listeners);
        return cetash;
    }

    private CetashAlgo cetashAlgo = new CetashAlgo(cetashParams);

    private long blockNumber;
    private int[] cacheLight = null;
    private int[] fullData = null;
    private SystemProperties config;
    private long startNonce = -1;

    public Cetash(SystemProperties config, long blockNumber) {
        this.config = config;
        this.blockNumber = blockNumber;
        this.epoch = blockNumber / cetashAlgo.getParams().getEPOCH_LENGTH();
        if (config.getConfig().hasPath("mine.startNonce")) {
            startNonce = config.getConfig().getLong("mine.startNonce");
        }
    }

    public synchronized int[] getCacheLight() {
        if (cacheLight == null) {
            fireDatatasetStatusUpdate(DATASET_PREPARE);
            getCacheLightImpl();
            fireDatatasetStatusUpdate(DATASET_READY);
        }

        return cacheLight;
    }

    /**
     * Checks whceter light DAG is already generated and loads it
     * from cache, otherwise generates it
     * @return  Light DAG
     */
    private synchronized int[] getCacheLightImpl() {
        if (cacheLight == null) {
            File file = new File(config.cetashDir(), "mine-dag-light.dat");
            if (fileCacheEnabled && file.canRead()) {
                fireDatatasetStatusUpdate(LIGHT_DATASET_LOAD_START);
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    logger.info("Loading light dataset from " + file.getAbsolutePath());
                    long bNum = ois.readLong();
                    if (bNum == blockNumber) {
                        cacheLight = (int[]) ois.readObject();
                        fireDatatasetStatusUpdate(LIGHT_DATASET_LOADED);
                        logger.info("Dataset loaded.");
                    } else {
                        logger.info("Dataset block number miss: " + bNum + " != " + blockNumber);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if (cacheLight == null) {
                logger.info("Calculating light dataset...");
                fireDatatasetStatusUpdate(LIGHT_DATASET_GENERATE_START);
                cacheLight = getCetashAlgo().makeCache(getCetashAlgo().getParams().getCacheSize(blockNumber),
                        getCetashAlgo().getSeedHash(blockNumber));
                logger.info("Light dataset calculated.");

                if (fileCacheEnabled) {
                    file.getParentFile().mkdirs();
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
                        logger.info("Writing light dataset to " + file.getAbsolutePath());
                        oos.writeLong(blockNumber);
                        oos.writeObject(cacheLight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                fireDatatasetStatusUpdate(LIGHT_DATASET_GENERATED);
            }
        }
        return cacheLight;
    }

    public synchronized int[] getFullDataset() {
        if (fullData == null) {
            fireDatatasetStatusUpdate(DATASET_PREPARE);
            File file = new File(config.cetashDir(), "mine-dag.dat");
            if (fileCacheEnabled && file.canRead()) {
                fireDatatasetStatusUpdate(FULL_DATASET_LOAD_START);
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    logger.info("Loading dataset from " + file.getAbsolutePath());
                    long bNum = ois.readLong();
                    if (bNum == blockNumber) {
                        fullData = (int[]) ois.readObject();
                        logger.info("Dataset loaded.");
                        fireDatatasetStatusUpdate(FULL_DATASET_LOADED);
                    } else {
                        logger.info("Dataset block number miss: " + bNum + " != " + blockNumber);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fullData == null){

                logger.info("Calculating full dataset...");
                fireDatatasetStatusUpdate(FULL_DATASET_GENERATE_START);
                int[] cacheLight = getCacheLightImpl();
                fullData = getCetashAlgo().calcDataset(getFullSize(), cacheLight);
                logger.info("Full dataset calculated.");

                if (fileCacheEnabled) {
                    file.getParentFile().mkdirs();
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                        logger.info("Writing dataset to " + file.getAbsolutePath());
                        oos.writeLong(blockNumber);
                        oos.writeObject(fullData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                fireDatatasetStatusUpdate(FULL_DATASET_GENERATED);
            }
            fireDatatasetStatusUpdate(DATASET_READY);
        }
        return fullData;
    }

    int[] getFullData() {
        return fullData;
    }

    private long getFullSize() {
        return getCetashAlgo().getParams().getFullSize(blockNumber);
    }

    private CetashAlgo getCetashAlgo() {
        return cetashAlgo;
    }

    /**
     *  See {@link CetashAlgo#hashimotoLight}
     */
    public Pair<byte[], byte[]> hashimotoLight(BlockHeader header, long nonce) {
        return hashimotoLight(header, longToBytes(nonce));
    }

    private  Pair<byte[], byte[]> hashimotoLight(BlockHeader header, byte[] nonce) {
        return getCetashAlgo().hashimotoLight(getFullSize(), getCacheLight(),
                sha3(header.getEncodedWithoutNonce()), nonce);
    }

    /**
     *  See {@link CetashAlgo#hashimotoFull}
     */
    public Pair<byte[], byte[]> hashimotoFull(BlockHeader header, long nonce) {
        return getCetashAlgo().hashimotoFull(getFullSize(), getFullDataset(), sha3(header.getEncodedWithoutNonce()),
                longToBytes(nonce));
    }

    public ListenableFuture<MiningResult> mine(final Block block) {
        return mine(block, 1);
    }

    /**
     *  Mines the nonce for the specified Block with difficulty BlockHeader.getDifficulty()
     *  When mined the Block 'nonce' and 'mixHash' fields are updated
     *  Uses the full dataset i.e. it faster but takes > 1Gb of memory and may
     *  take up to 10 mins for starting up (depending on whceter the dataset was cached)
     *
     *  @param block The block to mine. The difficulty is taken from the block header
     *               This block is updated when mined
     *  @param nThreads CPU threads to mine on
     *  @return the task which may be cancelled. On success returns nonce
     */
    public ListenableFuture<MiningResult> mine(final Block block, int nThreads) {
        return new MineTask(block, nThreads,  new Callable<MiningResult>() {
            AtomicLong taskStartNonce = new AtomicLong(startNonce >= 0 ? startNonce : new Random().nextLong());
            @Override
            public MiningResult call() throws Exception {
                long threadStartNonce = taskStartNonce.getAndAdd(0x100000000L);
                long nonce = getCetashAlgo().mine(getFullSize(), getFullDataset(),
                        sha3(block.getHeader().getEncodedWithoutNonce()),
                        ByteUtil.byteArrayToLong(block.getHeader().getDifficulty()), threadStartNonce);
                final Pair<byte[], byte[]> pair = hashimotoLight(block.getHeader(), nonce);
                return new MiningResult(nonce, pair.getLeft(), block);
            }
        }).submit();
    }

    public ListenableFuture<MiningResult> mineLight(final Block block) {
        return mineLight(block, 1);
    }

    /**
     *  Mines the nonce for the specified Block with difficulty BlockHeader.getDifficulty()
     *  When mined the Block 'nonce' and 'mixHash' fields are updated
     *  Uses the light cache i.e. it slower but takes only ~16Mb of memory and takes less
     *  time to start up
     *
     *  @param block The block to mine. The difficulty is taken from the block header
     *               This block is updated when mined
     *  @param nThreads CPU threads to mine on
     *  @return the task which may be cancelled. On success returns nonce
     */
    public ListenableFuture<MiningResult> mineLight(final Block block, int nThreads) {
        return new MineTask(block, nThreads,  new Callable<MiningResult>() {
            AtomicLong taskStartNonce = new AtomicLong(startNonce >= 0 ? startNonce : new Random().nextLong());
            @Override
            public MiningResult call() throws Exception {
                long threadStartNonce = taskStartNonce.getAndAdd(0x100000000L);
                final long nonce = getCetashAlgo().mineLight(getFullSize(), getCacheLight(),
                        sha3(block.getHeader().getEncodedWithoutNonce()),
                        ByteUtil.byteArrayToLong(block.getHeader().getDifficulty()), threadStartNonce);
                final Pair<byte[], byte[]> pair = hashimotoLight(block.getHeader(), nonce);
                return new MiningResult(nonce, pair.getLeft(), block);
            }
        }).submit();
    }

    /**
     *  Validates the BlockHeader against its getDifficulty() and getNonce()
     */
    public boolean validate(BlockHeader header) {
        byte[] boundary = header.getPowBoundary();
        byte[] hash = hashimotoLight(header, header.getNonce()).getRight();

        return FastByteComparisons.compareTo(hash, 0, 32, boundary, 0, 32) < 0;
    }

    private void fireDatatasetStatusUpdate(CetashListener.DatasetStatus status) {
        for (CetashListener l : listeners) {
            l.onDatasetUpdate(status);
        }
    }

    class MineTask extends AnyFuture<MiningResult> {
        Block block;
        int nThreads;
        Callable<MiningResult> miner;

        public MineTask(Block block, int nThreads, Callable<MiningResult> miner) {
            this.block = block;
            this.nThreads = nThreads;
            this.miner = miner;
        }

        public MineTask submit() {
            for (int i = 0; i < nThreads; i++) {
                ListenableFuture<MiningResult> f = executor.submit(miner);
                add(f);
            }
            return this;
        }

        @Override
        protected void postProcess(MiningResult result) {
            Pair<byte[], byte[]> pair = hashimotoLight(block.getHeader(), result.nonce);
            block.setNonce(longToBytes(result.nonce));
            block.setMixHash(pair.getLeft());
        }
    }
}
