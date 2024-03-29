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

import org.cetereum.config.blockchain.*;
import org.cetereum.config.net.MainNetConfig;
import org.cetereum.config.net.RopstenNetConfig;
import org.json.simple.parser.ParseException;
import org.junit.FixMcetodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.McetodSorters;

import java.io.IOException;

import static org.cetereum.jsontestsuite.GitHubJSONTestSuite.runCryptoTest;
import static org.cetereum.jsontestsuite.GitHubJSONTestSuite.runDifficultyTest;

/**
 * @author Mikhail Kalinin
 * @since 02.09.2015
 */
@FixMcetodOrder(McetodSorters.NAME_ASCENDING)
public class GitHubBasicTest {

    String commitSHA = "253e99861fe406c7b1daf3d6a0c40906e8a8fd8f";

    @Test
    public void btCrypto() throws IOException {
        runCryptoTest("BasicTests/crypto.json", commitSHA);
    }

    @Test
    public void btDifficulty() throws IOException, ParseException {
        runDifficultyTest(MainNetConfig.INSTANCE, "BasicTests/difficulty.json", commitSHA);
    }

    @Test
    public void btDifficultyByzantium() throws IOException, ParseException {
        runDifficultyTest(new ByzantiumConfig(new DaoHFConfig()), "BasicTests/difficultyByzantium.json", commitSHA);
    }

    @Test
    public void btDifficultyConstantinople() throws IOException, ParseException {
        runDifficultyTest(new ConstantinopleConfig(new DaoHFConfig()), "BasicTests/difficultyConstantinople.json", commitSHA);
    }

    @Test
    @Ignore // due to CPP minimumDifficulty issue
    public void btDifficultyCustomHomestead() throws IOException, ParseException {
        runDifficultyTest(new HomesteadConfig(), "BasicTests/difficultyCustomHomestead.json", commitSHA);
    }

    @Test
    @Ignore // due to CPP minimumDifficulty issue
    public void btDifficultyCustomMainNetwork() throws IOException, ParseException {
        runDifficultyTest(MainNetConfig.INSTANCE, "BasicTests/difficultyCustomMainNetwork.json", commitSHA);
    }

    @Test
    public void btDifficultyFrontier() throws IOException, ParseException {
        runDifficultyTest(new FrontierConfig(), "BasicTests/difficultyFrontier.json", commitSHA);
    }

    @Test
    public void btDifficultyHomestead() throws IOException, ParseException {
        runDifficultyTest(new HomesteadConfig(), "BasicTests/difficultyHomestead.json", commitSHA);
    }

    @Test
    public void btDifficultyMainNetwork() throws IOException, ParseException {
        runDifficultyTest(MainNetConfig.INSTANCE, "BasicTests/difficultyMainNetwork.json", commitSHA);
    }

    @Test
    @Ignore("Disable Ropsten until cached tests commit is updated to commitSHA")
    public void btDifficultyRopsten() throws IOException, ParseException {
        runDifficultyTest(new RopstenNetConfig(), "BasicTests/difficultyRopsten.json", commitSHA);
    }
}
