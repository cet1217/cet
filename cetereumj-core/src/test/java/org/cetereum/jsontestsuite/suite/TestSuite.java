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

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Roman Mandeleil
 * @since 10.07.2014
 */
public class TestSuite {

    List<TestCase> testList = new ArrayList<>();

    public TestSuite(JSONObject testCaseJSONObj) throws ParseException {

        for (Object key : testCaseJSONObj.keySet()) {

            Object testCaseJSON = testCaseJSONObj.get(key);
            TestCase testCase = new TestCase(key.toString(), (JSONObject) testCaseJSON);
            testList.add(testCase);
        }
    }

    public List<TestCase> getAllTests(){
        return testList;
    }

    public Iterator<TestCase> iterator() {
        return testList.iterator();
    }
}
