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
package org.cetereum.jsontestsuite.suite;

import org.cetereum.core.*;
import org.cetereum.db.RepositoryImpl;

import java.math.BigInteger;

/**
 * Repository for running GitHubVMTest.
 * The slightly modified behavior from original impl:
 * it creates empty account whenever it 'touched': getCode() or getBalance()
 * Created by Anton Nashatyrev on 03.11.2016.
 */
public class EnvTestRepository extends IterableTestRepository {

    public EnvTestRepository(Repository src) {
        super(src);
    }

    public BigInteger setNonce(byte[] addr, BigInteger nonce) {
        if (!(src instanceof RepositoryImpl)) throw new RuntimeException("Not supported");
        return ((RepositoryImpl) src).setNonce(addr, nonce);
    }


    @Override
    public byte[] getCode(byte[] addr) {
        addAccount(addr);
        return src.getCode(addr);
    }

    @Override
    public BigInteger getBalance(byte[] addr) {
        addAccount(addr);
        return src.getBalance(addr);
    }
}
