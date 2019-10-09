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
package org.cetereum.util.blockchain;

import org.cetereum.core.TransactionExecutionSummary;
import org.cetereum.core.TransactionReceipt;

/**
 * Created by Anton Nashatyrev on 26.07.2016.
 */
public class TransactionResult {
    TransactionReceipt receipt;
    TransactionExecutionSummary executionSummary;

    public boolean isIncluded() {
        return receipt != null;
    }

    public TransactionReceipt getReceipt() {
        return receipt;
    }

    public TransactionExecutionSummary getExecutionSummary() {
        return executionSummary;
    }
}
