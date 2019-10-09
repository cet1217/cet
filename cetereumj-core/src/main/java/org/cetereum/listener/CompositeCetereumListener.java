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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Roman Mandeleil
 * @since 12.11.2014
 */
public class CompositeCetereumListener implements CetereumListener {

    private static abstract class RunnableInfo implements Runnable {
        private CetereumListener listener;
        private String info;

        public RunnableInfo(CetereumListener listener, String info) {
            this.listener = listener;
            this.info = info;
        }

        @Override
        public String toString() {
            return "RunnableInfo: " + info + " [listener: " + listener.getClass() + "]";
        }
    }

    @Autowired
    EventDispatchThread eventDispatchThread = EventDispatchThread.getDefault();
    
    protected List<CetereumListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(CetereumListener listener) {
        listeners.add(listener);
    }
    public void removeListener(CetereumListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void trace(final String output) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "trace") {
                @Override
                public void run() {
                    listener.trace(output);
                }
            });
        }
    }

    @Override
    public void onBlock(final BlockSummary blockSummary) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onBlock") {
                @Override
                public void run() {
                    listener.onBlock(blockSummary);
                }
            });
        }
    }

    @Override
    public void onBlock(final BlockSummary blockSummary, final boolean best) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onBlock") {
                @Override
                public void run() {
                    listener.onBlock(blockSummary, best);
                }
            });
        }
    }

    @Override
    public void onRecvMessage(final Channel channel, final Message message) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onRecvMessage") {
                @Override
                public void run() {
                    listener.onRecvMessage(channel, message);
                }
            });
        }
    }

    @Override
    public void onSendMessage(final Channel channel, final Message message) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onSendMessage") {
                @Override
                public void run() {
                    listener.onSendMessage(channel, message);
                }
            });
        }
    }

    @Override
    public void onPeerDisconnect(final String host, final long port) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onPeerDisconnect") {
                @Override
                public void run() {
                    listener.onPeerDisconnect(host, port);
                }
            });
        }
    }

    @Override
    public void onPendingTransactionsReceived(final List<Transaction> transactions) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onPendingTransactionsReceived") {
                @Override
                public void run() {
                    listener.onPendingTransactionsReceived(transactions);
                }
            });
        }
    }

    @Override
    public void onPendingStateChanged(final PendingState pendingState) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onPendingStateChanged") {
                @Override
                public void run() {
                    listener.onPendingStateChanged(pendingState);
                }
            });
        }
    }

    @Override
    public void onSyncDone(final SyncState state) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onSyncDone") {
                @Override
                public void run() {
                    listener.onSyncDone(state);
                }
            });
        }
    }

    @Override
    public void onNoConnections() {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onNoConnections") {
                @Override
                public void run() {
                    listener.onNoConnections();
                }
            });
        }
    }

    @Override
    public void onHandShakePeer(final Channel channel, final HelloMessage helloMessage) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onHandShakePeer") {
                @Override
                public void run() {
                    listener.onHandShakePeer(channel, helloMessage);
                }
            });
        }
    }

    @Override
    public void onVMTraceCreated(final String transactionHash, final String trace) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onVMTraceCreated") {
                @Override
                public void run() {
                    listener.onVMTraceCreated(transactionHash, trace);
                }
            });
        }
    }

    @Override
    public void onNodeDiscovered(final Node node) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onNodeDiscovered") {
                @Override
                public void run() {
                    listener.onNodeDiscovered(node);
                }
            });
        }
    }

    @Override
    public void onCetStatusUpdated(final Channel channel, final StatusMessage status) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onCetStatusUpdated") {
                @Override
                public void run() {
                    listener.onCetStatusUpdated(channel, status);
                }
            });
        }
    }

    @Override
    public void onTransactionExecuted(final TransactionExecutionSummary summary) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onTransactionExecuted") {
                @Override
                public void run() {
                    listener.onTransactionExecuted(summary);
                }
            });
        }
    }

    @Override
    public void onPeerAddedToSyncPool(final Channel peer) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onPeerAddedToSyncPool") {
                @Override
                public void run() {
                    listener.onPeerAddedToSyncPool(peer);
                }
            });
        }
    }

    @Override
    public void onPendingTransactionUpdate(final TransactionReceipt txReceipt, final PendingTransactionState state,
                                           final Block block) {
        for (final CetereumListener listener : listeners) {
            eventDispatchThread.invokeLater(new RunnableInfo(listener, "onPendingTransactionUpdate") {
                @Override
                public void run() {
                    listener.onPendingTransactionUpdate(txReceipt, state, block);
                }
            });
        }
    }
}
