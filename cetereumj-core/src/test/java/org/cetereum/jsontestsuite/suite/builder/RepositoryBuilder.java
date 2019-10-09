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

import org.cetereum.core.AccountState;
import org.cetereum.core.Repository;
import org.cetereum.datasource.inmem.HashMapDB;
import org.cetereum.datasource.NoDeleteSource;
import org.cetereum.jsontestsuite.suite.IterableTestRepository;
import org.cetereum.db.RepositoryRoot;
import org.cetereum.db.ByteArrayWrapper;
import org.cetereum.db.ContractDetails;
import org.cetereum.jsontestsuite.suite.ContractDetailsCacheImpl;
import org.cetereum.jsontestsuite.suite.model.AccountTck;

import java.util.HashMap;
import java.util.Map;

import static org.cetereum.jsontestsuite.suite.Utils.parseData;
import static org.cetereum.util.ByteUtil.wrap;

public class RepositoryBuilder {

    public static Repository build(Map<String, AccountTck> accounts){
        HashMap<ByteArrayWrapper, AccountState> stateBatch = new HashMap<>();
        HashMap<ByteArrayWrapper, ContractDetails> detailsBatch = new HashMap<>();

        for (String address : accounts.keySet()) {

            AccountTck accountTCK = accounts.get(address);
            AccountBuilder.StateWrap stateWrap = AccountBuilder.build(accountTCK);

            AccountState state = stateWrap.getAccountState();
            ContractDetails details = stateWrap.getContractDetails();

            stateBatch.put(wrap(parseData(address)), state);

            ContractDetailsCacheImpl detailsCache = new ContractDetailsCacheImpl(details);
            detailsCache.setDirty(true);

            detailsBatch.put(wrap(parseData(address)), detailsCache);
        }

        Repository repositoryDummy = new IterableTestRepository(new RepositoryRoot(new NoDeleteSource<>(new HashMapDB<byte[]>())));
        Repository track = repositoryDummy.startTracking();

        track.updateBatch(stateBatch, detailsBatch);
        track.commit();
        repositoryDummy.commit();

        return repositoryDummy;
    }
}
