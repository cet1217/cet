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

import org.junit.FixMcetodOrder;
import org.junit.Test;
import org.junit.runners.McetodSorters;

import java.io.IOException;

import static org.cetereum.jsontestsuite.GitHubJSONTestSuite.runABITest;

/**
 * @author Mikhail Kalinin
 * @since 28.09.2017
 */
@FixMcetodOrder(McetodSorters.NAME_ASCENDING)
public class GithubABITest {

    String commitSHA = "253e99861fe406c7b1daf3d6a0c40906e8a8fd8f";

    @Test
    public void basicAbiTests() throws IOException {
        runABITest("ABITests/basic_abi_tests.json", commitSHA);
    }
}
