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
package org.cetereum.jsontestsuite.suite.builder;

import org.cetereum.jsontestsuite.suite.Env;
import org.cetereum.jsontestsuite.suite.model.EnvTck;

import static org.cetereum.jsontestsuite.suite.Utils.parseData;
import static org.cetereum.jsontestsuite.suite.Utils.parseNumericData;
import static org.cetereum.jsontestsuite.suite.Utils.parseVarData;

public class EnvBuilder {

    public static Env build(EnvTck envTck){
        byte[] coinbase = parseData(envTck.getCurrentCoinbase());
        byte[] difficulty = parseVarData(envTck.getCurrentDifficulty());
        byte[] gasLimit = parseVarData(envTck.getCurrentGasLimit());
        byte[] number = parseNumericData(envTck.getCurrentNumber());
        byte[] timestamp = parseNumericData(envTck.getCurrentTimestamp());
        byte[] hash = parseData(envTck.getPreviousHash());

        return new Env(coinbase, difficulty, gasLimit, number, timestamp, hash);
    }

}
