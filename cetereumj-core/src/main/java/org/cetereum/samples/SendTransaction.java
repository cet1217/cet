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
package org.cetereum.samples;

import org.cetereum.core.*;
import org.cetereum.crypto.ECKey;
import org.cetereum.crypto.HashUtil;
import org.cetereum.db.ByteArrayWrapper;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.listener.CetereumListenerAdapter;
import org.cetereum.util.ByteUtil;
import org.cetereum.util.blockchain.CeterUtil;
import org.spongycastle.util.encoders.Hex;
import org.springframework.context.annotation.Bean;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * With this simple example you can send transaction from address to address in live public network
 * To make it work you just need to set sender's private key and receiver's address
 *
 * Created by Alexander Samtsov on 12.08.16.
 */
public class SendTransaction extends BasicSample {

    private Map<ByteArrayWrapper, TransactionReceipt> txWaiters =
            Collections.synchronizedMap(new HashMap<ByteArrayWrapper, TransactionReceipt>());

    @Override
    public void onSyncDone() throws Exception {
        cetereum.addListener(new CetereumListenerAdapter() {
            // when block arrives look for our included transactions
            @Override
            public void onBlock(Block block, List<TransactionReceipt> receipts) {
                SendTransaction.this.onBlock(block, receipts);
            }
        });


        String toAddress = "";
        logger.info("Sending transaction to net and waiting for inclusion");
        sendTxAndWait(Hex.decode(toAddress), new byte[0]);
        logger.info("Transaction included!");}


    private void onBlock(Block block, List<TransactionReceipt> receipts) {
        for (TransactionReceipt receipt : receipts) {
            ByteArrayWrapper txHashW = new ByteArrayWrapper(receipt.getTransaction().getHash());
            if (txWaiters.containsKey(txHashW)) {
                txWaiters.put(txHashW, receipt);
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }


    private TransactionReceipt sendTxAndWait(byte[] receiveAddress, byte[] data) throws InterruptedException {

        byte[] senderPrivateKey = HashUtil.sha3("cow".getBytes());
        byte[] fromAddress = ECKey.fromPrivate(senderPrivateKey).getAddress();
        BigInteger nonce = cetereum.getRepository().getNonce(fromAddress);
        Transaction tx = new Transaction(
                ByteUtil.bigIntegerToBytes(nonce),
                ByteUtil.longToBytesNoLeadZeroes(cetereum.getGasPrice()),
                ByteUtil.longToBytesNoLeadZeroes(200000),
                receiveAddress,
                ByteUtil.bigIntegerToBytes(CeterUtil.convert(1, CeterUtil.Unit.WEI)),  // Use CeterUtil.convert for easy value unit conversion
                data,
                cetereum.getChainIdForNextBlock());

        tx.sign(ECKey.fromPrivate(senderPrivateKey));
        logger.info("<=== Sending transaction: " + tx);
        cetereum.submitTransaction(tx);

        return waitForTx(tx.getHash());
    }


    private TransactionReceipt waitForTx(byte[] txHash) throws InterruptedException {
        ByteArrayWrapper txHashW = new ByteArrayWrapper(txHash);
        txWaiters.put(txHashW, null);
        long startBlock = cetereum.getBlockchain().getBestBlock().getNumber();

        while(true) {
            TransactionReceipt receipt = txWaiters.get(txHashW);
            if (receipt != null) {
                return receipt;
            } else {
                long curBlock = cetereum.getBlockchain().getBestBlock().getNumber();
                if (curBlock > startBlock + 16) {
                    throw new RuntimeException("The transaction was not included during last 16 blocks: " + txHashW.toString().substring(0,8));
                } else {
                    logger.info("Waiting for block with transaction 0x" + txHashW.toString().substring(0,8) +
                            " included (" + (curBlock - startBlock) + " blocks received so far) ...");

                }
            }
            synchronized (this) {
                wait(20000);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        sLogger.info("Starting CetereumJ!");

        class Config {
            @Bean
            public BasicSample sampleBean() {
                return new SendTransaction();
            }
        }

        // Based on Config class the BasicSample would be created by Spring
        // and its springInit() mcetod would be called as an entry point
        CetereumFactory.createCetereum(Config.class);

    }

}
