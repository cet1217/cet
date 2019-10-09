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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Default factory implementation
 *
 * @author Mikhail Kalinin
 * @since 20.08.2015
 */
@Component
public class CetHandlerFactoryImpl implements CetHandlerFactory {

    @Autowired
    private ApplicationContext ctx;

    @Override
    public CetHandler create(CetVersion version) {
        switch (version) {
            case V62:   return (CetHandler) ctx.getBean("Cet62");
            case V63:   return (CetHandler) ctx.getBean("Cet63");
            default:    throw new IllegalArgumentException("Cet " + version + " is not supported");
        }
    }
}
