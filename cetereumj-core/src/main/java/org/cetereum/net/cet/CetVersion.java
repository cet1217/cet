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
package org.cetereum.net.cet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents supported Cet versions
 *
 * @author Mikhail Kalinin
 * @since 14.08.2015
 */
public enum CetVersion {

    V62((byte) 62),
    V63((byte) 63);

    public static final byte LOWER = V62.getCode();
    public static final byte UPPER = V63.getCode();

    private byte code;

    CetVersion(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static CetVersion fromCode(int code) {
        for (CetVersion v : values()) {
            if (v.code == code) {
                return v;
            }
        }

        return null;
    }

    public static boolean isSupported(byte code) {
        return code >= LOWER && code <= UPPER;
    }

    public static List<CetVersion> supported() {
        List<CetVersion> supported = new ArrayList<>();
        for (CetVersion v : values()) {
            if (isSupported(v.code)) {
                supported.add(v);
            }
        }

        return supported;
    }

    public boolean isCompatible(CetVersion version) {

        if (version.getCode() >= V62.getCode()) {
            return this.getCode() >= V62.getCode();
        } else {
            return this.getCode() < V62.getCode();
        }
    }
}
