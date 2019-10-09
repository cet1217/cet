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
package org.cetereum.net.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract message class for all messages on the Cetereum network
 *
 * @author Roman Mandeleil
 * @since 06.04.14
 */
public abstract class Message {

    protected static final Logger logger = LoggerFactory.getLogger("net");

    protected boolean parsed;
    protected byte[] encoded;
    protected byte code;

    public Message() {
    }

    public Message(byte[] encoded) {
        this.encoded = encoded;
        parsed = false;
    }

    /**
     * Gets the RLP encoded byte array of this message
     *
     * @return RLP encoded byte array representation of this message
     */
    public abstract byte[] getEncoded();

    public abstract Class<?> getAnswerMessage();

    /**
     * Returns the message in String format
     *
     * @return A string with all attributes of the message
     */
    public abstract String toString();

    public abstract Enum getCommand();

    public byte getCode() {
            return code;
    }

}
