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
package org.cetereum.net.wire;

import org.cetereum.net.client.Capability;
import org.cetereum.net.cet.CetVersion;
import org.cetereum.net.cet.message.CetMessageCodes;
import org.cetereum.net.p2p.P2pMessageCodes;
import org.cetereum.net.rlpx.MessageCodesResolver;
import org.cetereum.net.shh.ShhHandler;
import org.cetereum.net.shh.ShhMessageCodes;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.cetereum.net.cet.CetVersion.*;

/**
 * @author Roman Mandeleil
 * @since 15.10.2014
 */
public class AdaptiveMessageIdsTest {

    private MessageCodesResolver messageCodesResolver;

    @Before
    public void setUp() {
        messageCodesResolver = new MessageCodesResolver();
    }

    @Test
    public void test1() {

        assertEquals(7, P2pMessageCodes.values().length);

        assertEquals(0, messageCodesResolver.withP2pOffset(P2pMessageCodes.HELLO.asByte()));
        assertEquals(1, messageCodesResolver.withP2pOffset(P2pMessageCodes.DISCONNECT.asByte()));
        assertEquals(2, messageCodesResolver.withP2pOffset(P2pMessageCodes.PING.asByte()));
        assertEquals(3, messageCodesResolver.withP2pOffset(P2pMessageCodes.PONG.asByte()));
        assertEquals(4, messageCodesResolver.withP2pOffset(P2pMessageCodes.GET_PEERS.asByte()));
        assertEquals(5, messageCodesResolver.withP2pOffset(P2pMessageCodes.PEERS.asByte()));
        assertEquals(15, messageCodesResolver.withP2pOffset(P2pMessageCodes.USER.asByte()));
    }

    @Test
    public void test2() {

        assertEquals(8, CetMessageCodes.values(V62).length);

        assertEquals(0, CetMessageCodes.STATUS.asByte());
        assertEquals(1, CetMessageCodes.NEW_BLOCK_HASHES.asByte());
        assertEquals(2, CetMessageCodes.TRANSACTIONS.asByte());
        assertEquals(3, CetMessageCodes.GET_BLOCK_HEADERS.asByte());
        assertEquals(4, CetMessageCodes.BLOCK_HEADERS.asByte());
        assertEquals(5, CetMessageCodes.GET_BLOCK_BODIES.asByte());
        assertEquals(6, CetMessageCodes.BLOCK_BODIES.asByte());
        assertEquals(7, CetMessageCodes.NEW_BLOCK.asByte());

        messageCodesResolver.setCetOffset(0x10);

        assertEquals(0x10 + 0, messageCodesResolver.withCetOffset(CetMessageCodes.STATUS.asByte()));
        assertEquals(0x10 + 1, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK_HASHES.asByte()));
        assertEquals(0x10 + 2, messageCodesResolver.withCetOffset(CetMessageCodes.TRANSACTIONS.asByte()));
        assertEquals(0x10 + 3, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 4, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 5, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 6, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 7, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK.asByte()));
    }

    @Test
    public void test3() {

        assertEquals(3, ShhMessageCodes.values().length);

        assertEquals(0, ShhMessageCodes.STATUS.asByte());
        assertEquals(1, ShhMessageCodes.MESSAGE.asByte());
        assertEquals(2, ShhMessageCodes.FILTER.asByte());

        messageCodesResolver.setShhOffset(0x20);

        assertEquals(0x20 + 0, messageCodesResolver.withShhOffset(ShhMessageCodes.STATUS.asByte()));
        assertEquals(0x20 + 1, messageCodesResolver.withShhOffset(ShhMessageCodes.MESSAGE.asByte()));
        assertEquals(0x20 + 2, messageCodesResolver.withShhOffset(ShhMessageCodes.FILTER.asByte()));
    }

    @Test
    public void test4() {

        List<Capability> capabilities = Arrays.asList(
                new Capability(Capability.CET, CetVersion.V62.getCode()),
                new Capability(Capability.SHH, ShhHandler.VERSION));

        messageCodesResolver.init(capabilities);

        assertEquals(0x10 + 0, messageCodesResolver.withCetOffset(CetMessageCodes.STATUS.asByte()));
        assertEquals(0x10 + 1, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK_HASHES.asByte()));
        assertEquals(0x10 + 2, messageCodesResolver.withCetOffset(CetMessageCodes.TRANSACTIONS.asByte()));
        assertEquals(0x10 + 3, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 4, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 5, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 6, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 7, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK.asByte()));

        assertEquals(0x18 + 0, messageCodesResolver.withShhOffset(ShhMessageCodes.STATUS.asByte()));
        assertEquals(0x18 + 1, messageCodesResolver.withShhOffset(ShhMessageCodes.MESSAGE.asByte()));
        assertEquals(0x18 + 2, messageCodesResolver.withShhOffset(ShhMessageCodes.FILTER.asByte()));
    }

    @Test // Capabilities should be read in alphabetical order
    public void test5() {

        List<Capability> capabilities = Arrays.asList(
                new Capability(Capability.SHH, ShhHandler.VERSION),
                new Capability(Capability.CET, CetVersion.V62.getCode()));

        messageCodesResolver.init(capabilities);

        assertEquals(0x10 + 0, messageCodesResolver.withCetOffset(CetMessageCodes.STATUS.asByte()));
        assertEquals(0x10 + 1, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK_HASHES.asByte()));
        assertEquals(0x10 + 2, messageCodesResolver.withCetOffset(CetMessageCodes.TRANSACTIONS.asByte()));
        assertEquals(0x10 + 3, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 4, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_HEADERS.asByte()));
        assertEquals(0x10 + 5, messageCodesResolver.withCetOffset(CetMessageCodes.GET_BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 6, messageCodesResolver.withCetOffset(CetMessageCodes.BLOCK_BODIES.asByte()));
        assertEquals(0x10 + 7, messageCodesResolver.withCetOffset(CetMessageCodes.NEW_BLOCK.asByte()));

        assertEquals(0x18 + 0, messageCodesResolver.withShhOffset(ShhMessageCodes.STATUS.asByte()));
        assertEquals(0x18 + 1, messageCodesResolver.withShhOffset(ShhMessageCodes.MESSAGE.asByte()));
        assertEquals(0x18 + 2, messageCodesResolver.withShhOffset(ShhMessageCodes.FILTER.asByte()));
    }
}
