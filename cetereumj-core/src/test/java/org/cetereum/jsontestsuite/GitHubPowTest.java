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

import org.cetereum.core.BlockHeader;
import org.cetereum.jsontestsuite.suite.CetashTestCase;
import org.cetereum.jsontestsuite.suite.CetashTestSuite;
import org.cetereum.jsontestsuite.suite.JSONReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Mikhail Kalinin
 * @since 03.09.2015
 */
public class GitHubPowTest {

    private static final Logger logger = LoggerFactory.getLogger("TCK-Test");
    public String shacommit = "253e99861fe406c7b1daf3d6a0c40906e8a8fd8f";

    @Test
    public void runCetashTest() throws IOException {

        String json = JSONReader.loadJSONFromCommit("PoWTests/cetash_tests.json", shacommit);

        CetashTestSuite testSuite = new CetashTestSuite(json);

        for (CetashTestCase testCase : testSuite.getTestCases()) {

            logger.info("Running {}\n", testCase.getName());

            BlockHeader header = testCase.getBlockHeader();

            assertArrayEquals(testCase.getResultBytes(), header.calcPowValue());
        }

    }
}
