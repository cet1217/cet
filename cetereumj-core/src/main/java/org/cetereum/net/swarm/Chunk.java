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
package org.cetereum.net.swarm;

/**
 *  Any binary data with its key
 *  The key is normally SHA3(data)
 */
public class Chunk {

    protected Key key;
    protected byte[] data;

    public Chunk(Key key, byte[] data) {
        this.key = key;
        this.data = data;
    }

    public Key getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

}
