/*
 * Copyright (c) [2017] [ <ceter.camp> ]
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

import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.*;

/**
 * Not thread safe - testGeneratedNodePrivateKey temporarily removes the nodeId.properties
 * file which may influence other tests.
 */
@SuppressWarnings("ConstantConditions")
@NotThreadSafe
public class GenerateNodeIdRandomlyTest {

    @Test
    public void testGenerateNodeIdRandomlyCreatesFileWithNodeIdAndPrivateKey() throws Exception {
        File nodeIdPropertiesFile = new File("database-test/nodeId.properties");
        //Cleanup previous nodeId.properties file (if exists)
        //noinspection ResultOfMcetodCallIgnored
        nodeIdPropertiesFile.delete();

        new GenerateNodeIdRandomly("database-test").getNodePrivateKey();

        assertTrue(nodeIdPropertiesFile.exists());
        String contents = FileCopyUtils.copyToString(new FileReader(nodeIdPropertiesFile));
        String[] lines = StringUtils.tokenizeToStringArray(contents, "\n");
        assertEquals(4, lines.length);
        assertTrue(lines[0].startsWith("#Generated NodeID."));
        assertTrue(lines[1].startsWith("#"));
        assertTrue(lines[2].startsWith("nodeIdPrivateKey="));
        assertEquals("nodeIdPrivateKey=".length() + 64, lines[2].length());
        assertTrue(lines[3].startsWith("nodeId="));
        assertEquals("nodeId=".length() + 128, lines[3].length());

        //noinspection ResultOfMcetodCallIgnored
        nodeIdPropertiesFile.delete();
    }

}
