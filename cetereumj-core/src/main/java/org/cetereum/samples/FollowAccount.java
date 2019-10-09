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
import org.cetereum.core.TransactionReceipt;
import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.facade.Repository;
import org.cetereum.listener.CetereumListenerAdapter;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;

public class FollowAccount extends CetereumListenerAdapter {


    Cetereum cetereum = null;

    public FollowAccount(Cetereum cetereum) {
        this.cetereum = cetereum;
    }

    public static void main(String[] args) {

        Cetereum cetereum = CetereumFactory.createCetereum();
        cetereum.addListener(new FollowAccount(cetereum));
    }

    @Override
    public void onBlock(Block block, List<TransactionReceipt> receipts) {

        byte[] cow = Hex.decode("cd2a3d9f938e13cd947ec05abc7fe734df8dd826");

        // Get snapshot some time ago - 10% blocks ago
        long bestNumber = cetereum.getBlockchain().getBestBlock().getNumber();
        long oldNumber = (long) (bestNumber * 0.9);

        Block oldBlock = cetereum.getBlockchain().getBlockByNumber(oldNumber);

        Repository repository = cetereum.getRepository();
        Repository snapshot = cetereum.getSnapshotTo(oldBlock.getStateRoot());

        BigInteger nonce_ = snapshot.getNonce(cow);
        BigInteger nonce = repository.getNonce(cow);

        System.err.println(" #" + block.getNumber() + " [cd2a3d9] => snapshot_nonce:" +  nonce_ + " latest_nonce:" + nonce);
    }
}
