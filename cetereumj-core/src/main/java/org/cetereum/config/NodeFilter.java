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
package org.cetereum.config;

import org.cetereum.net.rlpx.Node;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Nashatyrev on 14.01.2016.
 */
public class NodeFilter {
    private List<Entry> entries = new ArrayList<>();

    public void add(byte[] nodeId, String hostIpPattern) {
        entries.add(new Entry(nodeId, hostIpPattern));
    }

    public boolean accept(Node node) {
        for (Entry entry : entries) {
            if (entry.accept(node)) return true;
        }
        return false;
    }

    public boolean accept(InetAddress nodeAddr) {
        for (Entry entry : entries) {
            if (entry.accept(nodeAddr)) return true;
        }
        return false;
    }

    private class Entry {
        byte[] nodeId;
        String hostIpPattern;

        public Entry(byte[] nodeId, String hostIpPattern) {
            this.nodeId = nodeId;
            if (hostIpPattern != null) {
                int idx = hostIpPattern.indexOf("*");
                if (idx > 0) {
                    hostIpPattern = hostIpPattern.substring(0, idx);
                }
            }
            this.hostIpPattern = hostIpPattern;
        }

        public boolean accept(InetAddress nodeAddr) {
            if (hostIpPattern == null) return true;
            String ip = nodeAddr.getHostAddress();
            return hostIpPattern != null && ip.startsWith(hostIpPattern);
        }

        public boolean accept(Node node) {
            try {
                boolean shouldAcceptNodeId = nodeId == null || Arrays.equals(node.getId(), nodeId);
                if (!shouldAcceptNodeId) {
                    return false;
                }
                InetAddress nodeAddress = InetAddress.getByName(node.getHost());
                return (hostIpPattern == null || accept(nodeAddress));
            } catch (UnknownHostException e) {
                return false;
            }
        }
    }
}
