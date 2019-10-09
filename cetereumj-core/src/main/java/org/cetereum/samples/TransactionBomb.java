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

import org.cetereum.core.Block;
import org.cetereum.core.Transaction;
import org.cetereum.core.TransactionReceipt;
import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.listener.CetereumListenerAdapter;
import org.spongycastle.util.encoders.Hex;

import java.util.Collections;
import java.util.List;

import static org.cetereum.crypto.HashUtil.sha3;
import static org.cetereum.util.ByteUtil.longToBytesNoLeadZeroes;
import static org.cetereum.util.ByteUtil.toHexString;

public class TransactionBomb extends CetereumListenerAdapter {


    Cetereum cetereum = null;
    boolean startedTxBomb = false;

    public TransactionBomb(Cetereum cetereum) {
        this.cetereum = cetereum;
    }

    public static void main(String[] args) {

        Cetereum cetereum = CetereumFactory.createCetereum();
        cetereum.addListener(new TransactionBomb(cetereum));
    }


    @Override
    public void onSyncDone(SyncState state) {

        // We will send transactions only
        // after we have the full chain syncs
        // - in order to prevent old nonce usage
        startedTxBomb = true;
        System.err.println(" ~~~ SYNC DONE ~~~ ");
    }

    @Override
    public void onBlock(Block block, List<TransactionReceipt> receipts) {

        if (startedTxBomb){
            byte[] sender = Hex.decode("cd2a3d9f938e13cd947ec05abc7fe734df8dd826");
            long nonce = cetereum.getRepository().getNonce(sender).longValue();;

            for (int i=0; i < 20; ++i){
                sendTx(nonce);
                ++nonce;
                sleep(10);
            }
        }
    }

    private void sendTx(long nonce){

        byte[] gasPrice = longToBytesNoLeadZeroes(1_000_000_000_000L);
        byte[] gasLimit = longToBytesNoLeadZeroes(21000);

        byte[] toAddress = Hex.decode("9f598824ffa7068c1f2543f04efb58b6993db933");
        byte[] value = longToBytesNoLeadZeroes(10_000);

        Transaction tx = new Transaction(longToBytesNoLeadZeroes(nonce),
                gasPrice,
                gasLimit,
                toAddress,
                value,
                null,
                cetereum.getChainIdForNextBlock());

        byte[] privKey = sha3("cow".getBytes());
        tx.sign(privKey);

        cetereum.getChannelManager().sendTransaction(Collections.singletonList(tx), null);
        System.err.println("Sending tx: " + toHexString(tx.getHash()));
    }

    private void sleep(int millis){
        try {Thread.sleep(millis);}
        catch (InterruptedException e) {e.printStackTrace();}
    }
}
