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
package org.cetereum.net.rlpx;

import org.cetereum.crypto.ECKey;
import org.cetereum.util.ByteUtil;
import org.cetereum.util.RLP;
import org.cetereum.util.RLPList;
import org.spongycastle.math.ec.ECPoint;

import static org.cetereum.util.ByteUtil.toHexString;

/**
 * Auth Response message defined by EIP-8
 *
 * @author mkalinin
 * @since 17.02.2016
 */
public class AuthResponseMessageV4 {

    ECPoint ephemeralPublicKey; // 64 bytes - uncompressed and no type byte
    byte[] nonce; // 32 bytes
    int version = 4; // 4 bytes

    static AuthResponseMessageV4 decode(byte[] wire) {

        AuthResponseMessageV4 message = new AuthResponseMessageV4();

        RLPList params = (RLPList) RLP.decode2OneItem(wire, 0);

        byte[] pubKeyBytes = params.get(0).getRLPData();

        byte[] bytes = new byte[65];
        System.arraycopy(pubKeyBytes, 0, bytes, 1, 64);
        bytes[0] = 0x04; // uncompressed
        message.ephemeralPublicKey = ECKey.CURVE.getCurve().decodePoint(bytes);

        message.nonce = params.get(1).getRLPData();

        byte[] versionBytes = params.get(2).getRLPData();
        message.version = ByteUtil.byteArrayToInt(versionBytes);

        return message;
    }

    public byte[] encode() {

        byte[] publicKey = new byte[64];
        System.arraycopy(ephemeralPublicKey.getEncoded(false), 1, publicKey, 0, publicKey.length);

        byte[] publicBytes = RLP.encode(publicKey);
        byte[] nonceBytes = RLP.encode(nonce);
        byte[] versionBytes = RLP.encodeInt(version);

        return RLP.encodeList(publicBytes, nonceBytes, versionBytes);
    }

    @Override
    public String toString() {
        return "AuthResponseMessage{" +
                "\n  ephemeralPublicKey=" + ephemeralPublicKey +
                "\n  nonce=" + toHexString(nonce) +
                "\n  version=" + version +
                '}';
    }
}
