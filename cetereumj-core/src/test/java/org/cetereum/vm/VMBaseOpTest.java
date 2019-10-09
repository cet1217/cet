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
package org.cetereum.vm;

import org.cetereum.vm.program.Program;
import org.cetereum.vm.program.invoke.ProgramInvokeMockImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMcetodOrder;
import org.junit.runners.McetodSorters;

/**
 * Base Op test structure
 * Use {@link #compile(String)} with VM code to compile it,
 * then new Program(compiledCode) to run
 */
@FixMcetodOrder(McetodSorters.NAME_ASCENDING)
public abstract class VMBaseOpTest {

    protected ProgramInvokeMockImpl invoke;
    protected Program program;


    @Before
    public void setup() {
        invoke = new ProgramInvokeMockImpl();
    }

    @After
    public void tearDown() {
        invoke.getRepository().close();
    }


    protected byte[] compile(String code) {
        return new BytecodeCompiler().compile(code);
    }
}
