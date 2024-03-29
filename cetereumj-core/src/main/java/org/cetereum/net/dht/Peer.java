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
package org.cetereum.net.dht;

import org.cetereum.crypto.HashUtil;
import org.spongycastle.util.BigIntegers;

import java.math.BigInteger;
import static org.cetereum.util.ByteUtil.toHexString;

public class Peer {
    byte[] id;
    String host = "127.0.0.1";
    int port = 0;

    public Peer(byte[] id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public Peer(byte[] ip) {
        this.id= ip;
    }

    public Peer() {
        HashUtil.randomPeerId();
    }

    public byte nextBit(String startPattern) {

        if (this.toBinaryString().startsWith(startPattern + "1"))
            return 1;
        else
            return 0;
    }

    public byte[] calcDistance(Peer toPeer) {

        BigInteger aPeer = new BigInteger(getId());
        BigInteger bPeer = new BigInteger(toPeer.getId());

        BigInteger distance = aPeer.xor(bPeer);
        return BigIntegers.asUnsignedByteArray(distance);
    }


    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Peer {\n id=%s, \n host=%s, \n port=%d\n}", toHexString(id), host, port);
    }

    public String toBinaryString() {

        BigInteger bi = new BigInteger(1, id);
        String out = String.format("%512s", bi.toString(2));
        out = out.replace(' ', '0');

        return out;
    }

}
