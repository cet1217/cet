/*
 * Copyright (c) [2017] [ <ceter.camp> ]
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
 *
 *
 */

package org.cetereum.config.net;

import org.cetereum.config.BlockchainConfig;
import org.cetereum.config.BlockchainNetConfig;
import org.cetereum.config.blockchain.FrontierConfig;
import org.cetereum.config.blockchain.HomesteadConfig;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestNetConfigTest {
    @Test
    public void verifyETCNetConfigConstruction() {
        TestNetConfig config = new TestNetConfig();

        assertBlockchainConfigExistsAt(config, 0, FrontierConfig.class);
        assertBlockchainConfigExistsAt(config, 1_150_000, HomesteadConfig.class);
    }

    private <T extends BlockchainConfig> void assertBlockchainConfigExistsAt(BlockchainNetConfig netConfig, long blockNumber, Class<T> configType) {
        BlockchainConfig block = netConfig.getConfigForBlock(blockNumber);
        assertTrue(configType.isAssignableFrom(block.getClass()));
    }

}