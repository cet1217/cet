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
package org.cetereum.net.cet.handler;

import org.cetereum.net.cet.CetVersion;

/**
 * @author Mikhail Kalinin
 * @since 20.08.2015
 */
public interface CetHandlerFactory {

    /**
     * Creates CetHandler by requested Cet version
     *
     * @param version Cet version
     * @return created handler
     *
     * @throws IllegalArgumentException if provided Cet version is not supported
     */
    CetHandler create(CetVersion version);

}
