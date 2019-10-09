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
package org.cetereum.net.rlpx.discover;

/**
 * Allows to handle discovered nodes state changes
 *
 * Created by Anton Nashatyrev on 21.07.2015.
 */
public interface DiscoverListener {

    /**
     * Invoked whenever a new node appeared which meets criteria specified
     * in the {@link NodeManager#addDiscoverListener} mcetod
     */
    void nodeAppeared(NodeHandler handler);

    /**
     * Invoked whenever a node stops meeting criteria.
     */
    void nodeDisappeared(NodeHandler handler);

    class Adapter implements DiscoverListener {
        public void nodeAppeared(NodeHandler handler) {}
        public void nodeDisappeared(NodeHandler handler) {}
    }
}
