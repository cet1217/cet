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
package org.cetereum.facade;

import org.cetereum.vm.DataWord;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Repository {

    /**
     * @param addr - account to check
     * @return - true if account exist,
     *           false otherwise
     */
    boolean isExist(byte[] addr);


    /**
     * Retrieve balance of an account
     *
     * @param addr of the account
     * @return balance of the account as a <code>BigInteger</code> value
     */
    BigInteger getBalance(byte[] addr);


    /**
     * Get current nonce of a given account
     *
     * @param addr of the account
     * @return value of the nonce
     */
    BigInteger getNonce(byte[] addr);


    /**
     * Retrieve the code associated with an account
     *
     * @param addr of the account
     * @return code in byte-array format
     */
    byte[] getCode(byte[] addr);


    /**
     * Retrieve storage value from an account for a given key
     *
     * @param addr of the account
     * @param key associated with this value
     * @return data in the form of a <code>DataWord</code>
     */
    DataWord getStorageValue(byte[] addr, DataWord key);

    /**
     * Retrieve storage size for a given account
     *
     * @param addr of the account
     * @return storage entries count
     */
    int getStorageSize(byte[] addr);

    /**
     * Retrieve all storage keys for a given account
     *
     * @param addr of the account
     * @return set of storage keys or empty set if account with specified address not exists
     */
    Set<DataWord> getStorageKeys(byte[] addr);

    /**
     * Retrieve storage entries from an account for given keys
     *
     * @param addr of the account
     * @param keys
     * @return storage entries for specified keys, or full storage if keys parameter is <code>null</code>
     */
    Map<DataWord, DataWord> getStorage(byte[] addr, @Nullable Collection<DataWord> keys);
}
