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
package org.cetereum.validator;

import org.cetereum.core.Block;
import org.junit.Ignore;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Mikhail Kalinin
 * @since 02.09.2015
 */
public class ProofOfWorkRuleTest {

    private ProofOfWorkRule rule = new ProofOfWorkRule();

    @Test // check exact values
    public void test_0() {
        byte[] rlp = Hex.decode("f9021af90215a0809870664d9a43cf1827aa515de6374e2fad1bf64290a9f261dd49c525d6a0efa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794f927a40c8b7f6e07c5af7fa2155b4864a4112b13a010c8ec4f62ecea600c616443bcf527d97e5b1c5bb4a9769c496d1bf32636c95da056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000086015a1c28ae5e82bf958302472c808455c4e47b99476574682f76312e302e312f6c696e75782f676f312e342e32a0788ac534cb2f6a226a01535e29b11a96602d447aed972463b5cbcc7dd5d633f288e2ff1b6435006517c0c0");
        Block b = new Block(rlp);

        byte[] boundary = b.getHeader().getPowBoundary();
        byte[] pow = b.getHeader().calcPowValue();

        assertArrayEquals(Hex.decode("0000000000bd59a74a8619f14c3d793747f1989a29ed6c83a5a488bac185679b"), boundary);
        assertArrayEquals(Hex.decode("000000000017f78925469f2f18fe7866ef6d3ed28d36fb013bc93d081e05809c"), pow);
    }

    @Test // valid block
    public void test_1() {
        byte[] rlp = Hex.decode("f9021af90215a0809870664d9a43cf1827aa515de6374e2fad1bf64290a9f261dd49c525d6a0efa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794f927a40c8b7f6e07c5af7fa2155b4864a4112b13a010c8ec4f62ecea600c616443bcf527d97e5b1c5bb4a9769c496d1bf32636c95da056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000086015a1c28ae5e82bf958302472c808455c4e47b99476574682f76312e302e312f6c696e75782f676f312e342e32a0788ac534cb2f6a226a01535e29b11a96602d447aed972463b5cbcc7dd5d633f288e2ff1b6435006517c0c0");
        Block b = new Block(rlp);
        assertTrue(rule.validate(b.getHeader()).success);
    }

    @Test // invalid block
    public void test_2() {
        byte[] rlp = Hex.decode("f90219f90214a0809870664d9a43cf1827aa515de6374e2fad1bf64290a9f261dd49c525d6a0efa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794f927a40c8b7f6e07c5af7fa2155b4864a4112b13a010c8ec4f62ecea600c616443bcf527d97e5b1c5bb4a9769c496d1bf32636c95da056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b9010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000857147839e5e82bf958302472c808455c4e47b99476574682f76312e302e312f6c696e75782f676f312e342e32a0788ac534cb2f6a226a01535e29b11a96602d447aed972463b5cbcc7dd5d633f288e2ff1b6435006517c0c0");
        Block b = new Block(rlp);
        assertFalse(rule.validate(b.getHeader()).success);
    }

    @Ignore
    @Test // stress test
    public void test_3() {
        int iterCnt = 1_000_000;

        byte[] rlp = Hex.decode("f9021af90215a0809870664d9a43cf1827aa515de6374e2fad1bf64290a9f261dd49c525d6a0efa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794f927a40c8b7f6e07c5af7fa2155b4864a4112b13a010c8ec4f62ecea600c616443bcf527d97e5b1c5bb4a9769c496d1bf32636c95da056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b901000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000086015a1c28ae5e82bf958302472c808455c4e47b99476574682f76312e302e312f6c696e75782f676f312e342e32a0788ac534cb2f6a226a01535e29b11a96602d447aed972463b5cbcc7dd5d633f288e2ff1b6435006517c0c0");
        Block b = new Block(rlp);
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterCnt; i++)
            rule.validate(b.getHeader());

        long total = System.currentTimeMillis() - start;

        System.out.println(String.format("Time: total = %d ms, per block = %.2f ms", total, (double) total / iterCnt));
    }

}
