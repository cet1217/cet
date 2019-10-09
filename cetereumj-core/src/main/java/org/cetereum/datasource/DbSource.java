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
package org.cetereum.datasource;

import java.util.Set;

/**
 * Interface represents DB source which is normally the final Source in the chain
 */
public interface DbSource<V> extends BatchSource<byte[], V> {

    /**
     * Sets the DB name.
     * This could be the underlying DB table/dir name
     */
    void setName(String name);

    /**
     * @return DB name
     */
    String getName();

    /**
     * Initializes DB (open table, connection, etc)
     * with default {@link DbSettings#DEFAULT}
     */
    void init();

    /**
     * Initializes DB (open table, connection, etc)
     * @param settings  DB settings
     */
    void init(DbSettings settings);

    /**
     * @return true if DB connection is alive
     */
    boolean isAlive();

    /**
     * Closes the DB table/connection
     */
    void close();

    /**
     * @return DB keys if this option is available
     * @throws RuntimeException if the mcetod is not supported
     */
    Set<byte[]> keys() throws RuntimeException;

    /**
     * Closes database, destroys its data and finally runs init()
     */
    void reset();

    /**
     * If supported, retrieves a value using a key prefix.
     * Prefix extraction is meant to be done on the implementing side.<br>
     *
     * @param key a key for the lookup
     * @param prefixBytes prefix length in bytes
     * @return first value picked by prefix lookup over DB or null if there is no match
     * @throws RuntimeException if operation is not supported
     */
    V prefixLookup(byte[] key, int prefixBytes);
}