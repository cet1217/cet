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
package org.cetereum.config.blockchain;

import org.apache.commons.lang3.tuple.Pair;
import org.cetereum.config.BlockchainConfig;
import org.cetereum.config.BlockchainNetConfig;
import org.cetereum.config.Constants;
import org.cetereum.config.SystemProperties;
import org.cetereum.core.Block;
import org.cetereum.core.BlockHeader;
import org.cetereum.core.Repository;
import org.cetereum.core.Transaction;
import org.cetereum.db.BlockStore;
import org.cetereum.mine.MinerIfc;
import org.cetereum.util.Utils;
import org.cetereum.validator.BlockHeaderValidator;
import org.cetereum.vm.DataWord;
import org.cetereum.vm.GasCost;
import org.cetereum.vm.OpCode;
import org.cetereum.vm.program.Program;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Anton Nashatyrev on 14.10.2016.
 */
public class Eip150HFConfig implements BlockchainConfig, BlockchainNetConfig {
    protected BlockchainConfig parent;


    static class GasCostEip150HF extends GasCost {
        public int getBALANCE()             {     return 400;     }
        public int getEXT_CODE_SIZE()       {     return 700;     }
        public int getEXT_CODE_COPY()       {     return 700;     }
        public int getSLOAD()               {     return 200;     }
        public int getCALL()                {     return 700;     }
        public int getSUICIDE()             {     return 5000;    }
        public int getNEW_ACCT_SUICIDE()    {     return 25000;   }
    };

    private static final GasCost NEW_GAS_COST = new GasCostEip150HF();

    public Eip150HFConfig(BlockchainConfig parent) {
        this.parent = parent;
    }

    @Override
    public DataWord getCallGas(OpCode op, DataWord requestedGas, DataWord availableGas) throws Program.OutOfGasException {
        DataWord maxAllowed = Utils.allButOne64th(availableGas);
        return requestedGas.compareTo(maxAllowed) > 0 ? maxAllowed : requestedGas;
    }

    @Override
    public DataWord getCreateGas(DataWord availableGas) {
        return Utils.allButOne64th(availableGas);
    }

    @Override
    public Constants getConstants() {
        return parent.getConstants();
    }

    @Override
    public MinerIfc getMineAlgorithm(SystemProperties config) {
        return parent.getMineAlgorithm(config);
    }

    @Override
    public BigInteger calcDifficulty(BlockHeader curBlock, BlockHeader parent) {
        return this.parent.calcDifficulty(curBlock, parent);
    }

    @Override
    public BigInteger getCalcDifficultyMultiplier(BlockHeader curBlock, BlockHeader parent) {
        return this.parent.getCalcDifficultyMultiplier(curBlock, parent);
    }

    @Override
    public long getTransactionCost(Transaction tx) {
        return parent.getTransactionCost(tx);
    }

    @Override
    public boolean acceptTransactionSignature(Transaction tx) {
        return parent.acceptTransactionSignature(tx) && tx.getChainId() == null;
    }

    @Override
    public String validateTransactionChanges(BlockStore blockStore, Block curBlock, Transaction tx, Repository repository) {
        return parent.validateTransactionChanges(blockStore, curBlock, tx, repository);
    }

    @Override
    public void hardForkTransfers(Block block, Repository repo) {
        parent.hardForkTransfers(block, repo);
    }

    @Override
    public byte[] getExtraData(byte[] minerExtraData, long blockNumber) {
        return parent.getExtraData(minerExtraData, blockNumber);
    }

    @Override
    public List<Pair<Long, BlockHeaderValidator>> headerValidators() {
        return parent.headerValidators();
    }

    @Override
    public boolean eip161() {
        return parent.eip161();
    }

    @Override
    public GasCost getGasCost() {
        return NEW_GAS_COST;
    }

    @Override
    public BlockchainConfig getConfigForBlock(long blockNumber) {
        return this;
    }

    @Override
    public Constants getCommonConstants() {
        return getConstants();
    }

    @Override
    public Integer getChainId() {
        return null;
    }

    @Override
    public boolean eip198() {
        return parent.eip198();
    }

    @Override
    public boolean eip206() {
        return false;
    }

    @Override
    public boolean eip211() {
        return false;
    }

    @Override
    public boolean eip212() {
        return parent.eip212();
    }

    @Override
    public boolean eip213() {
        return parent.eip213();
    }

    @Override
    public boolean eip214() {
        return false;
    }

    @Override
    public boolean eip658() {
        return false;
    }

    @Override
    public boolean eip145() {
        return false;
    }

    @Override
    public boolean eip1052() {
        return false;
    }

    @Override
    public boolean eip1283() {
        return false;
    }

    @Override
    public boolean eip1014() {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
