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
package org.cetereum.config.blockchain;

import org.cetereum.config.BlockchainConfig;
import org.cetereum.config.Constants;
import org.cetereum.config.ConstantsAdapter;
import org.cetereum.core.BlockHeader;
import org.cetereum.util.blockchain.CeterUtil;

import java.math.BigInteger;

/**
 * EIPs included in the Constantinople Hard Fork:
 * <ul>
 *     <li>1234 - Constantinople Difficulty Bomb Delay and Block Reward Adjustment (2 CET)</li>
 *     <li>145  - Bitwise shifting instructions in EVM</li>
 *     <li>1014 - Skinny CREATE2</li>
 *     <li>1052 - EXTCODEHASH opcode</li>
 *     <li>1283 - Net gas metering for SSTORE without dirty maps</li>
 * </ul>
 */
public class ConstantinopleConfig extends ByzantiumConfig {

    private final Constants constants;

    public ConstantinopleConfig(BlockchainConfig parent) {
        super(parent);
        constants = new ConstantsAdapter(super.getConstants()) {
            private final BigInteger BLOCK_REWARD = CeterUtil.convert(2, CeterUtil.Unit.CETER);

            @Override
            public BigInteger getBLOCK_REWARD() {
                return BLOCK_REWARD;
            }
        };
    }

    @Override
    public Constants getConstants() {
        return constants;
    }

    @Override
    protected int getExplosion(BlockHeader curBlock, BlockHeader parent) {
        int periodCount = (int) (Math.max(0, curBlock.getNumber() - 5_000_000) / getConstants().getEXP_DIFFICULTY_PERIOD());
        return periodCount - 2;
    }

    @Override
    public boolean eip1052() {
        return true;
    }

    @Override
    public boolean eip145() {
        return true;
    }

    @Override
    public boolean eip1283() {
        return true;
    }

    @Override
    public boolean eip1014() {
        return true;
    }
}
