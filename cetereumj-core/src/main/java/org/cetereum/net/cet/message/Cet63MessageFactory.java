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
package org.cetereum.net.cet.message;

import org.cetereum.net.message.Message;
import org.cetereum.net.message.MessageFactory;

import static org.cetereum.net.cet.CetVersion.V63;

/**
 * Fast synchronization (PV63) message factory
 */
public class Cet63MessageFactory implements MessageFactory {

    @Override
    public Message create(byte code, byte[] encoded) {

        CetMessageCodes receivedCommand = CetMessageCodes.fromByte(code, V63);
        switch (receivedCommand) {
            case STATUS:
                return new StatusMessage(encoded);
            case NEW_BLOCK_HASHES:
                return new NewBlockHashesMessage(encoded);
            case TRANSACTIONS:
                return new TransactionsMessage(encoded);
            case GET_BLOCK_HEADERS:
                return new GetBlockHeadersMessage(encoded);
            case BLOCK_HEADERS:
                return new BlockHeadersMessage(encoded);
            case GET_BLOCK_BODIES:
                return new GetBlockBodiesMessage(encoded);
            case BLOCK_BODIES:
                return new BlockBodiesMessage(encoded);
            case NEW_BLOCK:
                return new NewBlockMessage(encoded);
            case GET_NODE_DATA:
                return new GetNodeDataMessage(encoded);
            case NODE_DATA:
                return new NodeDataMessage(encoded);
            case GET_RECEIPTS:
                return new GetReceiptsMessage(encoded);
            case RECEIPTS:
                return new ReceiptsMessage(encoded);
            default:
                throw new IllegalArgumentException("No such message");
        }
    }
}
