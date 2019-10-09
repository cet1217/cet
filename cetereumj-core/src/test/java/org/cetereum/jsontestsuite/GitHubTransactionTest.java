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
package org.cetereum.jsontestsuite;

import org.cetereum.config.SystemProperties;
import org.cetereum.config.net.MainNetConfig;
import org.cetereum.jsontestsuite.suite.TxTestSuite;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.runners.McetodSorters;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@FixMcetodOrder(McetodSorters.NAME_ASCENDING)
public class GitHubTransactionTest {

    static String commitSHA = "253e99861fe406c7b1daf3d6a0c40906e8a8fd8f";
    static String treeSHA = "8c0d8131cb772b572862f0d88e778830bfddb006";  // https://github.com/cetereum/tests/tree/develop/TransactionTests/

    static TxTestSuite suite;

    @BeforeClass
    public static void setup() throws IOException {
        suite = new TxTestSuite(treeSHA, commitSHA);
        SystemProperties.getDefault().setBlockchainConfig(new MainNetConfig());
    }

    @After
    public void recover() {
        SystemProperties.getDefault().setBlockchainConfig(new MainNetConfig());
    }

    @Test
    public void ttAddress() throws IOException, ParseException {
        suite.runAll("ttAddress");
    }

    @Test
    public void ttData() throws IOException, ParseException {
        suite.run("ttData", new HashSet<>(Arrays.asList(
                "String10MbData"    // too big to run it each time
        )));
    }

    @Test
    public void ttGasLimit() throws IOException, ParseException {
        suite.runAll("ttGasLimit");
    }

    @Test
    public void ttGasPrice() throws IOException, ParseException {
        suite.runAll("ttGasPrice");
    }

    @Test
    public void ttNonce() throws IOException, ParseException {
        suite.runAll("ttNonce");
    }

    @Test
    public void ttRSValue() throws IOException, ParseException {
        suite.runAll("ttRSValue");
    }

    @Test
    public void ttVValue() throws IOException, ParseException {
        suite.runAll("ttVValue");
    }

    @Test
    public void ttSignature() throws IOException, ParseException {
        suite.runAll("ttSignature");
    }

    @Test
    public void ttValue() throws IOException, ParseException {
        suite.runAll("ttValue");
    }

    @Test
    public void ttWrongRLP() throws IOException, ParseException {
        suite.runAll("ttWrongRLP");
    }
}
