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

import org.cetereum.config.SystemProperties;
import org.cetereum.core.AccountState;
import org.cetereum.jsontestsuite.suite.ContractDetailsImpl;
import org.cetereum.jsontestsuite.suite.model.AccountTck;
import org.cetereum.vm.DataWord;

import java.util.HashMap;
import java.util.Map;

import static org.cetereum.crypto.HashUtil.sha3;
import static org.cetereum.jsontestsuite.suite.Utils.parseData;
import static org.cetereum.util.Utils.unifiedNumericToBigInteger;

public class AccountBuilder {

    public static StateWrap build(AccountTck account) {

        ContractDetailsImpl details = new ContractDetailsImpl();
        details.setCode(parseData(account.getCode()));
        details.setStorage(convertStorage(account.getStorage()));

        AccountState state = new AccountState(SystemProperties.getDefault())
                .withBalanceIncrement(unifiedNumericToBigInteger(account.getBalance()))
                .withNonce(unifiedNumericToBigInteger(account.getNonce()))
                .withStateRoot(details.getStorageHash())
                .withCodeHash(sha3(details.getCode()));

        return new StateWrap(state, details);
    }


    private static Map<DataWord, DataWord> convertStorage(Map<String, String> storageTck) {

        Map<DataWord, DataWord> storage = new HashMap<>();

        for (String keyTck : storageTck.keySet()) {
            String valueTck = storageTck.get(keyTck);

            DataWord key = DataWord.of(parseData(keyTck));
            DataWord value = DataWord.of(parseData(valueTck));

            storage.put(key, value);
        }

        return storage;
    }


    public static class StateWrap {

        AccountState accountState;
        ContractDetailsImpl contractDetails;

        public StateWrap(AccountState accountState, ContractDetailsImpl contractDetails) {
            this.accountState = accountState;
            this.contractDetails = contractDetails;
        }

        public AccountState getAccountState() {
            return accountState;
        }

        public ContractDetailsImpl getContractDetails() {
            return contractDetails;
        }
    }
}
