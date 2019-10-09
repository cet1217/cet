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
 * Interface similar to ByteBuffer for reading large streaming or random access data
 *
 * Created by Anton Nashatyrev on 18.06.2015.
 */
public interface SectionReader {

    long seek(long offset, int whence /* ??? */);

    int read(byte[] dest, int destOff);

    int readAt(byte[] dest, int destOff, long readerOffset);

    long getSize();
}
