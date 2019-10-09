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
package org.cetereum.listener;

import org.cetereum.core.*;
import org.cetereum.net.cet.message.StatusMessage;
import org.cetereum.net.message.Message;
import org.cetereum.net.p2p.HelloMessage;
import org.cetereum.net.rlpx.Node;
import org.cetereum.net.server.Channel;

import java.util.List;

/**
 * @author Roman Mandeleil
 * @since 08.08.2014
 */
public class CetereumListenerAdapter implements CetereumListener {

    @Override
    public void trace(String output) {
    }

    public void onBlock(Block block, List<TransactionReceipt> receipts) {
    }

    @Override
    public void onBlock(BlockSummary blockSummary) {
        onBlock(blockSummary.getBlock(), blockSummary.getReceipts());
    }

    @Override
    public void onRecvMessage(Channel channel, Message message) {
    }

    @Override
    public void onSendMessage(Channel channel, Message message) {
    }

    @Override
    public void onPeerDisconnect(String host, long port) {
    }

    @Override
    public void onPendingTransactionsReceived(List<Transaction> transactions) {
    }

    @Override
    public void onPendingStateChanged(PendingState pendingState) {
    }

    @Override
    public void onSyncDone(SyncState state) {

    }

    @Override
    public void onHandShakePeer(Channel channel, HelloMessage helloMessage) {

    }

    @Override
    public void onNoConnections() {

    }


    @Override
    public void onVMTraceCreated(String transactionHash, String trace) {

    }

    @Override
    public void onNodeDiscovered(Node node) {

    }

    @Override
    public void onCetStatusUpdated(Channel channel, StatusMessage statusMessage) {

    }

    @Override
    public void onTransactionExecuted(TransactionExecutionSummary summary) {

    }

    @Override
    public void onPeerAddedToSyncPool(Channel peer) {

    }

    @Override
    public void onPendingTransactionUpdate(TransactionReceipt txReceipt, PendingTransactionState state, Block block) {

    }
}
