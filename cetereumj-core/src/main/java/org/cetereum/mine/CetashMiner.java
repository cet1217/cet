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
package org.cetereum.mine;

import com.google.common.util.concurrent.ListenableFuture;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.Block;
import org.cetereum.core.BlockHeader;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The adapter of Cetash for MinerIfc
 *
 * Created by Anton Nashatyrev on 26.02.2016.
 */
public class CetashMiner implements MinerIfc {

    SystemProperties config;

    private int cpuThreads;
    private boolean fullMining = true;
    private Set<CetashListener> listeners = new CopyOnWriteArraySet<>();

    public CetashMiner(SystemProperties config) {
        this.config = config;
        cpuThreads = config.getMineCpuThreads();
        fullMining = config.isMineFullDataset();
    }

    @Override
    public ListenableFuture<MiningResult> mine(Block block) {
        return fullMining ?
                Cetash.getForBlock(config, block.getNumber(), listeners).mine(block, cpuThreads) :
                Cetash.getForBlock(config, block.getNumber(), listeners).mineLight(block, cpuThreads);
    }

    @Override
    public boolean validate(BlockHeader blockHeader) {
        return Cetash.getForBlock(config, blockHeader.getNumber(), listeners).validate(blockHeader);
    }

    /**
     * Listeners changes affects only future {@link #mine(Block)} and
     * {@link #validate(BlockHeader)} calls
     * Only instances of {@link CetashListener} are used, because CetashMiner
     * produces only events compatible with it
     */
    @Override
    public void setListeners(Collection<MinerListener> listeners) {
        this.listeners.clear();
        listeners.stream()
                .filter(listener -> listener instanceof CetashListener)
                .map(listener -> (CetashListener) listener)
                .forEach(this.listeners::add);
    }
}
