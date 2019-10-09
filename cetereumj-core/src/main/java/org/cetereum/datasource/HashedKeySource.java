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

/**
 * Indicator interface which narrows the Source contract:
 * the same Key always maps to the same Value,
 * there could be no put() with the same Key and different Value
 * Normally the Key is the hash of the Value
 * Usually such kind of sources are Merkle Trie backing stores
 *
 * Created by Anton Nashatyrev on 08.11.2016.
 */
public interface HashedKeySource<Key, Value> extends Source<Key, Value> {
}
