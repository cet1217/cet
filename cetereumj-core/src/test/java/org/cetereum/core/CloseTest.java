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
package org.cetereum.core;

import org.cetereum.facade.Cetereum;
import org.cetereum.facade.CetereumFactory;
import org.cetereum.listener.CetereumListenerAdapter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Anton Nashatyrev on 24.06.2016.
 */
public class CloseTest {

    @Ignore
    @Test
    public void relaunchTest() throws InterruptedException {

        while (true) {
            Cetereum cetereum = CetereumFactory.createCetereum();
            Block bestBlock = cetereum.getBlockchain().getBestBlock();
            Assert.assertNotNull(bestBlock);
            final CountDownLatch latch = new CountDownLatch(1);
            cetereum.addListener(new CetereumListenerAdapter() {
                int counter = 0;
                @Override
                public void onBlock(Block block, List<TransactionReceipt> receipts) {
                    counter++;
                    if (counter > 1100) latch.countDown();
                }
            });
            System.out.println("### Waiting for some blocks to be imported...");
            latch.await();
            System.out.println("### Closing Cetereum instance");
            cetereum.close();
            Thread.sleep(5000);
            System.out.println("### Closed.");
        }
    }
}
