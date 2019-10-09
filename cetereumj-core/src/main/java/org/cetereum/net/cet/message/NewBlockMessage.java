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

import org.cetereum.core.Block;
import org.cetereum.util.RLP;
import org.cetereum.util.RLPList;

import java.math.BigInteger;

import static org.cetereum.util.ByteUtil.toHexString;

/**
 * Wrapper around an Cetereum Blocks message on the network
 *
 * @see CetMessageCodes#NEW_BLOCK
 */
public class NewBlockMessage extends CetMessage {

    private Block block;
    private byte[] difficulty;

    public NewBlockMessage(byte[] encoded) {
        super(encoded);
    }

    public NewBlockMessage(Block block, byte[] difficulty) {
        this.block = block;
        this.difficulty = difficulty;
        this.parsed = true;
        encode();
    }

    private void encode() {
        byte[] block = this.block.getEncoded();
        byte[] diff = RLP.encodeElement(this.difficulty);

        this.encoded = RLP.encodeList(block, diff);
    }

    private synchronized void parse() {
        if (parsed) return;
        RLPList paramsList = RLP.unwrapList(encoded);

        block = new Block(paramsList.get(0).getRLPData());
        difficulty = paramsList.get(1).getRLPData();

        parsed = true;
    }

    public Block getBlock() {
        parse();
        return block;
    }

    public byte[] getDifficulty() {
        parse();
        return difficulty;
    }

    public BigInteger getDifficultyAsBigInt() {
        return new BigInteger(1, difficulty);
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }

    @Override
    public CetMessageCodes getCommand() {
        return CetMessageCodes.NEW_BLOCK;
    }

    @Override
    public Class<?> getAnswerMessage() {
        return null;
    }

    public String toString() {
        parse();

        String hash = this.getBlock().getShortHash();
        long number = this.getBlock().getNumber();
        return "NEW_BLOCK [ number: " + number + " hash:" + hash + " difficulty: " + toHexString(difficulty) + " ]";
    }
}