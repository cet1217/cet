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
package org.cetereum;

import org.cetereum.core.Block;
import org.cetereum.vm.DataWord;
import org.spongycastle.util.BigIntegers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.cetereum.crypto.HashUtil.EMPTY_TRIE_HASH;
import static org.cetereum.crypto.HashUtil.randomHash;

public final class TestUtils {

    private TestUtils() {
    }

    public static byte[] randomBytes(int length) {
        byte[] result = new byte[length];
        new Random().nextBytes(result);
        return result;
    }

    public static DataWord randomDataWord() {
        return DataWord.of(randomBytes(32));
    }

    public static byte[] randomAddress() {
        return randomBytes(20);
    }

    public static List<Block> getRandomChain(byte[] startParentHash, long startNumber, long length){

        List<Block> result = new ArrayList<>();

        byte[] lastHash = startParentHash;
        long lastIndex = startNumber;


        for (int i = 0; i < length; ++i){

            byte[] difficulty = BigIntegers.asUnsignedByteArray(new BigInteger(8, new Random()));
            byte[] newHash = randomHash();

            Block block = new Block(lastHash, newHash,  null, null, difficulty, lastIndex, new byte[] {0}, 0, 0, null, null,
                    null, null, EMPTY_TRIE_HASH, randomHash(), null, null);

            ++lastIndex;
            lastHash = block.getHash();
            result.add(block);
        }

        return result;
    }

    // Generates chain with alternative sub-chains, maxHeight blocks on each level
    public static List<Block> getRandomAltChain(byte[] startParentHash, long startNumber, long length, int maxHeight){

        List<Block> result = new ArrayList<>();

        List<byte[]> lastHashes = new ArrayList<>();
        lastHashes.add(startParentHash);
        long lastIndex = startNumber;
        Random rnd = new Random();

        for (int i = 0; i < length; ++i){
            List<byte[]> currentHashes = new ArrayList<>();
            int curMaxHeight = maxHeight;
            if (i == 0) curMaxHeight = 1;

            for (int j = 0; j < curMaxHeight; ++j){
                byte[] parentHash = lastHashes.get(rnd.nextInt(lastHashes.size()));
                byte[] difficulty = BigIntegers.asUnsignedByteArray(new BigInteger(8, new Random()));
                byte[] newHash = randomHash();

                Block block = new Block(parentHash, newHash, null, null, difficulty, lastIndex, new byte[]{0}, 0, 0, null, null,
                        null, null, EMPTY_TRIE_HASH, randomHash(), null, null);
                currentHashes.add(block.getHash());
                result.add(block);
            }

            ++lastIndex;
            lastHashes.clear();
            lastHashes.addAll(currentHashes);
        }

        return result;
    }
}
