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
package org.cetereum.util.blockchain;

import java.math.BigInteger;

/**
 * Created by Anton Nashatyrev on 22.06.2016.
 */
public class CeterUtil {
    public enum Unit {
        WEI(BigInteger.valueOf(1)),
        GWEI(BigInteger.valueOf(1_000_000_000)),
        SZABO(BigInteger.valueOf(1_000_000_000_000L)),
        FINNEY(BigInteger.valueOf(1_000_000_000_000_000L)),
        CETER(BigInteger.valueOf(1_000_000_000_000_000_000L));

        BigInteger i;
        Unit(BigInteger i) {
            this.i = i;
        }
    }

    public static BigInteger convert(long amount, Unit unit) {
        return BigInteger.valueOf(amount).multiply(unit.i);
    }
}
