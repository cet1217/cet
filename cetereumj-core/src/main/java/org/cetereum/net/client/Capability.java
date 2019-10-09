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
package org.cetereum.net.client;

/**
 * The protocols and versions of those protocols that this peer support
 */
public class Capability implements Comparable<Capability> {

    public final static String P2P = "p2p";
    public final static String CET = "cet";
    public final static String SHH = "shh";
    public final static String BZZ = "bzz";

    private String name;
    private byte version;

    public Capability(String name, byte version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public byte getVersion() {
        return version;
    }

    public boolean isCet() {
        return CET.equals(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Capability)) return false;

        Capability other = (Capability) obj;
        if (this.name == null)
            return other.name == null;
        else
            return this.name.equals(other.name) && this.version == other.version;
    }

    @Override
    public int compareTo(Capability o) {
        int cmp = this.name.compareTo(o.name);
        if (cmp != 0) {
            return cmp;
        } else {
            return Byte.valueOf(this.version).compareTo(o.version);
        }
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) version;
        return result;
    }

    public String toString() {
        return name + ":" + version;
    }
}