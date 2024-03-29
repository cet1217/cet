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
package org.cetereum.net.cet.message;

import org.cetereum.core.Transaction;
import org.cetereum.util.RLP;
import org.cetereum.util.RLPElement;
import org.cetereum.util.RLPList;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around an Cetereum Transactions message on the network
 *
 * @see CetMessageCodes#TRANSACTIONS
 */
public class TransactionsMessage extends CetMessage {

    private List<Transaction> transactions;

    public TransactionsMessage(byte[] encoded) {
        super(encoded);
    }

    public TransactionsMessage(Transaction transaction) {

        transactions = new ArrayList<>();
        transactions.add(transaction);
        parsed = true;
    }

    public TransactionsMessage(List<Transaction> transactionList) {
        this.transactions = transactionList;
        parsed = true;
    }

    private synchronized void parse() {
        if (parsed) return;
        RLPList paramsList = RLP.unwrapList(encoded);

        transactions = new ArrayList<>();
        for (int i = 0; i < paramsList.size(); ++i) {
            RLPElement rlpTxData = paramsList.get(i);
            Transaction tx = new Transaction(rlpTxData.getRLPData());
            transactions.add(tx);
        }
        parsed = true;
    }

    private void encode() {
        List<byte[]> encodedElements = new ArrayList<>();
        for (Transaction tx : transactions)
            encodedElements.add(tx.getEncoded());
        byte[][] encodedElementArray = encodedElements.toArray(new byte[encodedElements.size()][]);
        this.encoded = RLP.encodeList(encodedElementArray);
    }

    @Override
    public byte[] getEncoded() {
        if (encoded == null) encode();
        return encoded;
    }


    public List<Transaction> getTransactions() {
        parse();
        return transactions;
    }

    @Override
    public CetMessageCodes getCommand() {
        return CetMessageCodes.TRANSACTIONS;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    public String toString() {
        parse();
        final StringBuilder sb = new StringBuilder();
        if (transactions.size() < 4) {
            for (Transaction transaction : transactions)
                sb.append("\n   ").append(transaction.toString(128));
        } else {
            for (int i = 0; i < 3; i++) {
                sb.append("\n   ").append(transactions.get(i).toString(128));
            }
            sb.append("\n   ").append("[Skipped ").append(transactions.size() - 3).append(" transactions]");
        }
        return "[" + getCommand().name() + " num:"
                + transactions.size() + " " + sb.toString() + "]";
    }
}