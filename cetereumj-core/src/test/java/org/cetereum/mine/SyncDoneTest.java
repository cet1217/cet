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

import org.cetereum.config.NoAutoscan;
import org.cetereum.config.SystemProperties;
import org.cetereum.config.net.MainNetConfig;
import org.cetereum.core.Block;
import org.cetereum.core.BlockSummary;
import org.cetereum.core.Blockchain;
import org.cetereum.core.ImportResult;
import org.cetereum.core.Transaction;
import org.cetereum.crypto.ECKey;
import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.facade.CetereumImpl;
import org.cetereum.facade.SyncStatus;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.net.cet.handler.Cet62;
import org.cetereum.net.rlpx.Node;
import org.cetereum.net.server.Channel;
import org.cetereum.util.FastByteComparisons;
import org.cetereum.util.blockchain.CeterUtil;
import org.cetereum.util.blockchain.StandaloneBlockchain;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.cetereum.crypto.HashUtil.sha3;
import static org.cetereum.util.FileUtil.recursiveDelete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.spongycastle.util.encoders.Hex.decode;


/**
 * If Miner is started manually, sync status is not changed in any manner,
 * so if miner is started while the peer is in Long Sync mode, miner creates
 * new blocks but ignores any txs, because they are dropped by {@link org.cetereum.core.PendingStateImpl}
 * While automatic detection of long sync works correctly in any live network
 * with big number of peers, automatic detection of Short Sync condition in
 * detached or small private networks looks not doable.
 *
 * To resolve this and any other similar issues manual switch to Short Sync mode
 * was added: {@link CetereumImpl#switchToShortSync()}
 * This test verifies that manual switching to Short Sync works correctly
 * and solves miner issue.
 */
@Ignore("Long network tests")
public class SyncDoneTest {

    private static Node nodeA;
    private static List<Block> mainB1B10;

    private Cetereum cetereumA;
    private Cetereum cetereumB;
    private String testDbA;
    private String testDbB;

    private final static int MAX_SECONDS_WAIT = 60;

    @BeforeClass
    public static void setup() throws IOException, URISyntaxException {
        nodeA = new Node("enode://3973cb86d7bef9c96e5d589601d788370f9e24670dcba0480c0b3b1b0647d13d0f0fffed115dd2d4b5ca1929287839dcd4e77bdc724302b44ae48622a8766ee6@localhost:30334");

        SysPropConfigA.props.overrideParams(
                "peer.listen.port", "30334",
                "peer.privateKey", "3ec771c31cac8c0dba77a69e503765701d3c2bb62435888d4ffa38fed60c445c",
                "genesis", "genesis-light-old.json",
                "sync.enabled", "true",
                "mine.fullDataSet", "false"
        );

        SysPropConfigB.props.overrideParams(
                "peer.listen.port", "30335",
                "peer.privateKey", "6ef8da380c27cea8fdf7448340ea99e8e2268fc2950d79ed47cbf6f85dc977ec",
                "genesis", "genesis-light-old.json",
                "sync.enabled", "true",
                "mine.fullDataSet", "false"
        );

        mainB1B10 = loadBlocks("sync/main-b1-b10.dmp");
    }

    private static List<Block> loadBlocks(String path) throws URISyntaxException, IOException {

        URL url = ClassLoader.getSystemResource(path);
        File file = new File(url.toURI());
        List<String> strData = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<Block> blocks = new ArrayList<>(strData.size());
        for (String rlp : strData) {
            blocks.add(new Block(decode(rlp)));
        }

        return blocks;
    }

    @AfterClass
    public static void cleanup() {
        SystemProperties.getDefault().setBlockchainConfig(MainNetConfig.INSTANCE);
    }

    @Before
    public void setupTest() throws InterruptedException {
        testDbA = "test_db_" + new BigInteger(32, new Random());
        testDbB = "test_db_" + new BigInteger(32, new Random());

        SysPropConfigA.props.setDataBaseDir(testDbA);
        SysPropConfigB.props.setDataBaseDir(testDbB);
    }

    @After
    public void cleanupTest() {
        recursiveDelete(testDbA);
        recursiveDelete(testDbB);
        SysPropConfigA.cet62 = null;
    }

    // positive gap, A on main, B on main
    // expected: B downloads missed blocks from A => B on main
    @Test
    public void test1() throws InterruptedException {

        setupPeers();

        // A == B == genesis

        Blockchain blockchainA = (Blockchain) cetereumA.getBlockchain();

        for (Block b : mainB1B10) {
            ImportResult result = blockchainA.tryToConnect(b);
            System.out.println(result.isSuccessful());
        }

        long loadedBlocks = blockchainA.getBestBlock().getNumber();

        // Check that we are synced and on the same block
        assertTrue(loadedBlocks > 0);
        final CountDownLatch semaphore = new CountDownLatch(1);
        cetereumB.addListener(new CetereumListenerAdapter() {
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks) {
                    semaphore.countDown();
                }
            }
        });
        semaphore.await(MAX_SECONDS_WAIT, SECONDS);
        Assert.assertEquals(0, semaphore.getCount());
        assertEquals(loadedBlocks, cetereumB.getBlockchain().getBestBlock().getNumber());

        cetereumA.getBlockMiner().startMining();

        final CountDownLatch semaphore2 = new CountDownLatch(2);
        cetereumB.addListener(new CetereumListenerAdapter() {
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 2) {
                    semaphore2.countDown();
                    cetereumA.getBlockMiner().stopMining();
                }
            }
        });
        cetereumA.addListener(new CetereumListenerAdapter(){
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 2) {
                    semaphore2.countDown();
                }
            }
        });
        semaphore2.await(MAX_SECONDS_WAIT, SECONDS);
        Assert.assertEquals(0, semaphore2.getCount());

        assertFalse(cetereumA.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete));
        assertTrue(cetereumB.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete)); // Receives NEW_BLOCKs from CetereumA

        // Trying to include txs while miner is on long sync
        // Txs should be dropped as peer is not on short sync
        ECKey sender = ECKey.fromPrivate(Hex.decode("3ec771c31cac8c0dba77a69e503765701d3c2bb62435888d4ffa38fed60c445c"));
        Transaction tx = Transaction.create(
                Hex.toHexString(ECKey.fromPrivate(sha3("cow".getBytes())).getAddress()),
                CeterUtil.convert(1, CeterUtil.Unit.CETER),
                BigInteger.ZERO,
                BigInteger.valueOf(cetereumA.getGasPrice()),
                new BigInteger("3000000"),
                null
        );
        tx.sign(sender);
        final CountDownLatch txSemaphore = new CountDownLatch(1);
        cetereumA.addListener(new CetereumListenerAdapter(){
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (!blockSummary.getBlock().getTransactionsList().isEmpty() &&
                        FastByteComparisons.equal(blockSummary.getBlock().getTransactionsList().get(0).getSender(), sender.getAddress()) &&
                        blockSummary.getReceipts().get(0).isSuccessful()) {
                    txSemaphore.countDown();
                }
            }
        });
        cetereumB.submitTransaction(tx);

        final CountDownLatch semaphore3 = new CountDownLatch(2);
        cetereumB.addListener(new CetereumListenerAdapter() {
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 5) {
                    semaphore3.countDown();
                }
            }
        });
        cetereumA.addListener(new CetereumListenerAdapter(){
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 5) {
                    semaphore3.countDown();
                    cetereumA.getBlockMiner().stopMining();
                }
            }
        });
        cetereumA.getBlockMiner().startMining();

        semaphore3.await(MAX_SECONDS_WAIT, SECONDS);
        Assert.assertEquals(0, semaphore3.getCount());

        Assert.assertEquals(loadedBlocks + 5, cetereumA.getBlockchain().getBestBlock().getNumber());
        Assert.assertEquals(loadedBlocks + 5, cetereumB.getBlockchain().getBestBlock().getNumber());
        assertFalse(cetereumA.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete));
        // Tx was not included, because miner is on long sync
        assertFalse(txSemaphore.getCount() == 0);

        cetereumA.getBlockMiner().startMining();
        try {
            cetereumA.switchToShortSync().get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        assertTrue(cetereumA.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete));
        Transaction tx2 = Transaction.create(
                Hex.toHexString(ECKey.fromPrivate(sha3("cow".getBytes())).getAddress()),
                CeterUtil.convert(1, CeterUtil.Unit.CETER),
                BigInteger.ZERO,
                BigInteger.valueOf(cetereumA.getGasPrice()).add(BigInteger.TEN),
                new BigInteger("3000000"),
                null
        );
        tx2.sign(sender);
        cetereumB.submitTransaction(tx2);

        final CountDownLatch semaphore4 = new CountDownLatch(2);
        cetereumB.addListener(new CetereumListenerAdapter() {
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 9) {
                    semaphore4.countDown();
                    cetereumA.getBlockMiner().stopMining();
                }
            }
        });
        cetereumA.addListener(new CetereumListenerAdapter(){
            @Override
            public void onBlock(BlockSummary blockSummary) {
                if (blockSummary.getBlock().getNumber() == loadedBlocks + 9) {
                    semaphore4.countDown();
                }
            }
        });

        semaphore4.await(MAX_SECONDS_WAIT, SECONDS);
        Assert.assertEquals(0, semaphore4.getCount());
        Assert.assertEquals(loadedBlocks + 9, cetereumA.getBlockchain().getBestBlock().getNumber());
        Assert.assertEquals(loadedBlocks + 9, cetereumB.getBlockchain().getBestBlock().getNumber());
        assertTrue(cetereumA.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete));
        assertTrue(cetereumB.getSyncStatus().getStage().equals(SyncStatus.SyncStage.Complete));
        // Tx is included!
        assertTrue(txSemaphore.getCount() == 0);
    }

    private void setupPeers() throws InterruptedException {

        cetereumA = CetereumFactory.createCetereum(SysPropConfigA.props, SysPropConfigA.class);
        cetereumB = CetereumFactory.createCetereum(SysPropConfigB.props, SysPropConfigB.class);

        final CountDownLatch semaphore = new CountDownLatch(1);

        cetereumB.addListener(new CetereumListenerAdapter() {
            @Override
            public void onPeerAddedToSyncPool(Channel peer) {
                semaphore.countDown();
            }
        });

        cetereumB.connect(nodeA);

        semaphore.await(10, SECONDS);
        if(semaphore.getCount() > 0) {
            fail("Failed to set up peers");
        }
    }

    @Configuration
    @NoAutoscan
    public static class SysPropConfigA {
        static SystemProperties props = new SystemProperties();
        static Cet62 cet62 = null;

        @Bean
        public SystemProperties systemProperties() {
            props.setBlockchainConfig(StandaloneBlockchain.getEasyMiningConfig());
            return props;
        }

        @Bean
        @Scope("prototype")
        public Cet62 cet62() throws IllegalAccessException, InstantiationException {
            if (cet62 != null) return cet62;
            return new Cet62();
        }
    }

    @Configuration
    @NoAutoscan
    public static class SysPropConfigB {
        static SystemProperties props = new SystemProperties();

        @Bean
        public SystemProperties systemProperties() {
            props.setBlockchainConfig(StandaloneBlockchain.getEasyMiningConfig());
            return props;
        }
    }
}
