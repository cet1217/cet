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

import org.cetereum.core.BlockHeader;
import org.cetereum.jsontestsuite.suite.Utils;
import org.cetereum.jsontestsuite.suite.model.BlockHeaderTck;

import java.math.BigInteger;

public class BlockHeaderBuilder {


    public static BlockHeader  build(BlockHeaderTck headerTck){

        BlockHeader header = new BlockHeader(
                Utils.parseData(headerTck.getParentHash()),
                Utils.parseData(headerTck.getUncleHash()),
                Utils.parseData(headerTck.getCoinbase()),
                Utils.parseData(headerTck.getBloom()),
                Utils.parseNumericData(headerTck.getDifficulty()),
                new BigInteger(1, Utils.parseData(headerTck.getNumber())).longValue(),
                Utils.parseData(headerTck.getGasLimit()),
                new BigInteger(1, Utils.parseData(headerTck.getGasUsed())).longValue(),
                new BigInteger(1, Utils.parseData(headerTck.getTimestamp())).longValue(),
                Utils.parseData(headerTck.getExtraData()),
                Utils.parseData(headerTck.getMixHash()),
                Utils.parseData(headerTck.getNonce())
        );

        header.setReceiptsRoot(Utils.parseData(headerTck.getReceiptTrie()));
        header.setTransactionsRoot(Utils.parseData(headerTck.getTransactionsTrie()));
        header.setStateRoot(Utils.parseData(headerTck.getStateRoot()));

        return header;
    }

}
