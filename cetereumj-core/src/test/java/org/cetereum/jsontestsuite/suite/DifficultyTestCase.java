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
package org.cetereum.jsontestsuite.suite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.cetereum.core.BlockHeader;

import java.math.BigInteger;

import static org.cetereum.jsontestsuite.suite.Utils.parseData;
import static org.cetereum.util.ByteUtil.EMPTY_BYTE_ARRAY;

/**
 * @author Mikhail Kalinin
 * @since 02.09.2015
 */
public class DifficultyTestCase {

    @JsonIgnore
    private String name;

    // Test data
    private String parentTimestamp;
    private String parentDifficulty;
    private String currentTimestamp;
    private String currentBlockNumber;
    private String currentDifficulty;
    private String parentUncles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentTimestamp() {
        return parentTimestamp;
    }

    public void setParentTimestamp(String parentTimestamp) {
        this.parentTimestamp = parentTimestamp;
    }

    public String getParentDifficulty() {
        return parentDifficulty;
    }

    public void setParentDifficulty(String parentDifficulty) {
        this.parentDifficulty = parentDifficulty;
    }

    public String getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(String currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public String getCurrentBlockNumber() {
        return currentBlockNumber;
    }

    public void setCurrentBlockNumber(String currentBlockNumber) {
        this.currentBlockNumber = currentBlockNumber;
    }

    public String getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(String currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public String getParentUncles() {
        return parentUncles;
    }

    public void setParentUncles(String parentUncles) {
        this.parentUncles = parentUncles;
    }

    public BlockHeader getCurrent() {
        return new BlockHeader(
                EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY,
                Utils.parseLong(currentBlockNumber), new byte[] {0}, 0,
                Utils.parseLong(currentTimestamp),
                EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY
        );
    }

    public BlockHeader getParent() {
        return new BlockHeader(
                EMPTY_BYTE_ARRAY, parseData(parentUncles), EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY,
                Utils.parseNumericData(parentDifficulty),
                Utils.parseLong(currentBlockNumber) - 1, new byte[] {0}, 0,
                Utils.parseLong(parentTimestamp),
                EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY, EMPTY_BYTE_ARRAY
        );
    }

    public BigInteger getExpectedDifficulty() {
        return new BigInteger(1, Utils.parseNumericData(currentDifficulty));
    }

    @Override
    public String toString() {
        return "DifficultyTestCase{" +
                "name='" + name + '\'' +
                ", parentTimestamp='" + parentTimestamp + '\'' +
                ", parentDifficulty='" + parentDifficulty + '\'' +
                ", currentTimestamp='" + currentTimestamp + '\'' +
                ", currentBlockNumber='" + currentBlockNumber + '\'' +
                ", currentDifficulty='" + currentDifficulty + '\'' +
                '}';
    }
}
