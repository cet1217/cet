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

import org.cetereum.Start;
import org.junit.Ignore;
import org.junit.Test;

import static org.cetereum.crypto.HashUtil.sha3;

/**
 * Created by Admin on 06.07.2015.
 */
public class GoPeerTest {

    @Ignore
    @Test
    // TODO to be done at some point: run Go peer and connect to it
    public void putTest() throws Exception {
        System.out.println("Starting Java peer...");
        Start.main(new String[]{});
        System.out.println("Warming up...");
        Thread.sleep(5000);
        System.out.println("Sending a chunk...");

        Key key = new Key(sha3(new byte[]{0x22, 0x33}));
//            stdout.setFilter(Hex.toHexString(key.getBytes()));
        Chunk chunk = new Chunk(key, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 77, 88});

        NetStore.getInstance().put(chunk);
    }
}
