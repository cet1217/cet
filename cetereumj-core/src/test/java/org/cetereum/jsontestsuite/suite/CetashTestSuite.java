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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Kalinin
 * @since 03.09.2015
 */
public class CetashTestSuite {

    List<CetashTestCase> testCases = new ArrayList<>();

    public CetashTestSuite(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().
                constructMapType(HashMap.class, String.class, CetashTestCase.class);

        Map<String, CetashTestCase> caseMap = new ObjectMapper().readValue(json, type);

        for (Map.Entry<String, CetashTestCase> e : caseMap.entrySet()) {
            e.getValue().setName(e.getKey());
            testCases.add(e.getValue());
        }
    }

    public List<CetashTestCase> getTestCases() {
        return testCases;
    }

    @Override
    public String toString() {
        return "CetashTestSuite{" +
                "testCases=" + testCases +
                '}';
    }
}
