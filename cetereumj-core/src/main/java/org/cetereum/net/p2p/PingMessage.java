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
package org.cetereum.net.p2p;

import org.spongycastle.util.encoders.Hex;

/**
 * Wrapper around an Cetereum Ping message on the network
 *
 * @see org.cetereum.net.p2p.P2pMessageCodes#PING
 */
public class PingMessage extends P2pMessage {

    /**
     * Ping message is always a the same single command payload
     */
    private final static byte[] FIXED_PAYLOAD = Hex.decode("C0");

    public byte[] getEncoded() {
        return FIXED_PAYLOAD;
    }

    @Override
    public Class<PongMessage> getAnswerMessage() {
        return PongMessage.class;
    }

    @Override
    public P2pMessageCodes getCommand() {
        return P2pMessageCodes.PING;
    }

    @Override
    public String toString() {
        return "[" + getCommand().name() + "]";
    }
}