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
package org.cetereum.config.net;

import org.cetereum.config.blockchain.Eip150HFConfig;
import org.cetereum.config.blockchain.Eip160HFConfig;
import org.cetereum.config.blockchain.MordenConfig;

/**
 * Created by Anton Nashatyrev on 25.02.2016.
 */
public class MordenNetConfig extends BaseNetConfig {

    public MordenNetConfig() {
        add(0, new MordenConfig.Frontier());
        add(494_000, new MordenConfig.Homestead());
        add(1_783_000, new Eip150HFConfig(new MordenConfig.Homestead()));
        add(1_885_000, new Eip160HFConfig(new MordenConfig.Homestead()));

    }
}
