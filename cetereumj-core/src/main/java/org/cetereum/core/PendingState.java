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

import java.util.List;

/**
 * @author Mikhail Kalinin
 * @since 28.09.2015
 */
public interface PendingState extends org.cetereum.facade.PendingState {

    /**
     * Adds transactions received from the net to the list of wire transactions <br>
     * Triggers an update of pending state
     *
     * @param transactions txs received from the net
     * @return sublist of transactions with NEW_PENDING status
     */
    List<Transaction> addPendingTransactions(List<Transaction> transactions);

    /**
     * Adds transaction to the list of pending state txs  <br>
     * For the moment this list is populated with txs sent by our peer only <br>
     * Triggers an update of pending state
     *
     * @param tx transaction
     */
    void addPendingTransaction(Transaction tx);

    /**
     * It should be called on each block imported as <b>BEST</b> <br>
     * Does several things:
     * <ul>
     *     <li>removes block's txs from pending state and wire lists</li>
     *     <li>removes outdated wire txs</li>
     *     <li>updates pending state</li>
     * </ul>
     *
     * @param block block imported into blockchain as a <b>BEST</b> one
     */
    void processBest(Block block, List<TransactionReceipt> receipts);
}
