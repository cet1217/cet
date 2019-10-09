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
package org.cetereum.jsontestsuite.suite.builder;

import org.cetereum.core.Transaction;
import org.cetereum.jsontestsuite.suite.model.TransactionTck;

import static org.cetereum.jsontestsuite.suite.Utils.*;

public class TransactionBuilder {

    public static Transaction build(TransactionTck transactionTck) {

        Transaction transaction;
        if (transactionTck.getSecretKey() != null){

            transaction = new Transaction(
                    parseVarData(transactionTck.getNonce()),
                    parseVarData(transactionTck.getGasPrice()),
                    parseVarData(transactionTck.getGasLimit()),
                    parseData(transactionTck.getTo()),
                    parseVarData(transactionTck.getValue()),
                    parseData(transactionTck.getData()));
            transaction.sign(parseData(transactionTck.getSecretKey()));

        } else {

            transaction = new Transaction(
                    parseNumericData(transactionTck.getNonce()),
                    parseVarData(transactionTck.getGasPrice()),
                    parseVarData(transactionTck.getGasLimit()),
                    parseData(transactionTck.getTo()),
                    parseNumericData(transactionTck.getValue()),
                    parseData(transactionTck.getData()),
                    parseData(transactionTck.getR()),
                    parseData(transactionTck.getS()),
                    parseByte(transactionTck.getV())
            );
        }

        return transaction;
    }
}
