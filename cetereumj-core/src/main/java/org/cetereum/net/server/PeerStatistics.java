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
package org.cetereum.net.server;

/**
 * @author Mikhail Kalinin
 * @since 29.02.2016
 */
public class PeerStatistics {

    private double avgLatency = 0;
    private long pingCount = 0;

    public void pong(long pingStamp) {
        long latency = System.currentTimeMillis() - pingStamp;
        avgLatency = ((avgLatency * pingCount) + latency) / ++pingCount;
    }

    public double getAvgLatency() {
        return avgLatency;
    }
}
