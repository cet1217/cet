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

import static org.cetereum.util.BIUtil.max;

/**
 * EIPs included in the Hard Fork:
 * <ul>
 *     <li>100 - Change difficulty adjustment to target mean block time including uncles</li>
 *     <li>140 - REVERT instruction in the Cetereum Virtual Machine</li>
 *     <li>196 - Precompiled contracts for addition and scalar multiplication on the elliptic curve alt_bn128</li>
 *     <li>197 - Precompiled contracts for optimal Ate pairing check on the elliptic curve alt_bn128</li>
 *     <li>198 - Precompiled contract for bigint modular exponentiation</li>
 *     <li>211 - New opcodes: RETURNDATASIZE and RETURNDATACOPY</li>
 *     <li>214 - New opcode STATICCALL</li>
 *     <li>658 - Embedding transaction return data in receipts</li>
 * </ul>
 *
 * @author Mikhail Kalinin
 * @since 14.08.2017
 */
public class ByzantiumConfig extends Eip160HFConfig {

    private final Constants constants;

    public ByzantiumConfig(BlockchainConfig parent) {
        super(parent);
        constants = new ConstantsAdapter(super.getConstants()) {
            private final BigInteger BLOCK_REWARD = CeterUtil.convert(3, CeterUtil.Unit.CETER);

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
    public BigInteger calcDifficulty(BlockHeader curBlock, BlockHeader parent) {
        BigInteger pd = parent.getDifficultyBI();
        BigInteger quotient = pd.divide(getConstants().getDIFFICULTY_BOUND_DIVISOR());

        BigInteger sign = getCalcDifficultyMultiplier(curBlock, parent);

        BigInteger fromParent = pd.add(quotient.multiply(sign));
        BigInteger difficulty = max(getConstants().getMINIMUM_DIFFICULTY(), fromParent);

        int explosion = getExplosion(curBlock, parent);

        if (explosion >= 0) {
            difficulty = max(getConstants().getMINIMUM_DIFFICULTY(), difficulty.add(BigInteger.ONE.shiftLeft(explosion)));
        }

        return difficulty;
    }

    protected int getExplosion(BlockHeader curBlock, BlockHeader parent) {
        int periodCount = (int) (Math.max(0, curBlock.getNumber() - 3_000_000) / getConstants().getEXP_DIFFICULTY_PERIOD());
        return periodCount - 2;
    }

    @Override
    public BigInteger getCalcDifficultyMultiplier(BlockHeader curBlock, BlockHeader parent) {
        long unclesAdj = parent.hasUncles() ? 2 : 1;
        return BigInteger.valueOf(Math.max(unclesAdj - (curBlock.getTimestamp() - parent.getTimestamp()) / 9, -99));
    }

    @Override
    public boolean eip198() {
        return true;
    }

    @Override
    public boolean eip206() {
        return true;
    }

    @Override
    public boolean eip211() {
        return true;
    }

    @Override
    public boolean eip212() {
        return true;
    }

    @Override
    public boolean eip213() {
        return true;
    }

    @Override
    public boolean eip214() {
        return true;
    }

    @Override
    public boolean eip658() {
        return true;
    }
}
