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

import org.cetereum.net.client.Capability;
import org.cetereum.net.cet.CetVersion;
import org.cetereum.net.cet.message.CetMessageCodes;
import org.cetereum.net.p2p.P2pMessageCodes;
import org.cetereum.net.shh.ShhMessageCodes;
import org.cetereum.net.swarm.bzz.BzzMessageCodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cetereum.net.cet.CetVersion.*;

/**
 * @author Mikhail Kalinin
 * @since 14.07.2015
 */
public class MessageCodesResolver {

    private Map<String, Integer> offsets = new HashMap<>();

    public MessageCodesResolver() {
    }

    public MessageCodesResolver(List<Capability> caps) {
        init(caps);
    }

    public void init(List<Capability> caps) {
        Collections.sort(caps);
        int offset = P2pMessageCodes.USER.asByte() + 1;

        for (Capability capability : caps) {
            if (capability.getName().equals(Capability.CET)) {
                setCetOffset(offset);
                CetVersion v = fromCode(capability.getVersion());
                offset += CetMessageCodes.maxCode(v) + 1; // +1 is essential cause STATUS code starts from 0x0
            }

            if (capability.getName().equals(Capability.SHH)) {
                setShhOffset(offset);
                offset += ShhMessageCodes.values().length;
            }

            if (capability.getName().equals(Capability.BZZ)) {
                setBzzOffset(offset);
                offset += BzzMessageCodes.values().length + 4;
                // FIXME: for some reason Go left 4 codes between BZZ and CET message codes
            }
        }
    }

    public byte withP2pOffset(byte code) {
        return withOffset(code, Capability.P2P);
    }

    public byte withBzzOffset(byte code) {
        return withOffset(code, Capability.BZZ);
    }

    public byte withCetOffset(byte code) {
        return withOffset(code, Capability.CET);
    }

    public byte withShhOffset(byte code) {
        return withOffset(code, Capability.SHH);
    }

    public byte withOffset(byte code, String cap) {
        byte offset = getOffset(cap);
        return (byte)(code + offset);
    }

    public byte resolveP2p(byte code) {
        return resolve(code, Capability.P2P);
    }

    public byte resolveBzz(byte code) {
        return resolve(code, Capability.BZZ);
    }

    public byte resolveCet(byte code) {
        return resolve(code, Capability.CET);
    }

    public byte resolveShh(byte code) {
        return resolve(code, Capability.SHH);
    }

    private byte resolve(byte code, String cap) {
        byte offset = getOffset(cap);
        return (byte)(code - offset);
    }

    private byte getOffset(String cap) {
        Integer offset = offsets.get(cap);
        return offset == null ? 0 : offset.byteValue();
    }

    public void setBzzOffset(int offset) {
        setOffset(Capability.BZZ, offset);
    }

    public void setCetOffset(int offset) {
        setOffset(Capability.CET, offset);
    }

    public void setShhOffset(int offset) {
        setOffset(Capability.SHH, offset);
    }

    private void setOffset(String cap, int offset) {
        offsets.put(cap, offset);
    }
}
