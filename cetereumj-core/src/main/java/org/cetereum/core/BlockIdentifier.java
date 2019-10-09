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

import org.cetereum.util.RLP;
import org.cetereum.util.RLPList;

import java.math.BigInteger;
import java.util.Arrays;

import static org.cetereum.util.ByteUtil.byteArrayToLong;
import static org.cetereum.util.ByteUtil.toHexString;


/**
 * Block identifier holds block hash and number <br>
 * This tuple is used in some places of the core,
 * like by {@link org.cetereum.net.cet.message.CetMessageCodes#NEW_BLOCK_HASHES} message wrapper
 *
 * @author Mikhail Kalinin
 * @since 04.09.2015
 */
public class BlockIdentifier {

    /**
     * Block hash
     */
    private byte[] hash;

    /**
     * Block number
     */
    private long number;

    public BlockIdentifier(RLPList rlp) {
        this.hash = rlp.get(0).getRLPData();
        this.number = byteArrayToLong(rlp.get(1).getRLPData());
    }

    public BlockIdentifier(byte[] hash, long number) {
        this.hash = hash;
        this.number = number;
    }

    public byte[] getHash() {
        return hash;
    }

    public long getNumber() {
        return number;
    }

    public byte[] getEncoded() {
        byte[] hash = RLP.encodeElement(this.hash);
        byte[] number = RLP.encodeBigInteger(BigInteger.valueOf(this.number));

        return RLP.encodeList(hash, number);
    }

    @Override
    public String toString() {
        return "BlockIdentifier {" +
                "hash=" + toHexString(hash) +
                ", number=" + number +
                '}';
    }
}
