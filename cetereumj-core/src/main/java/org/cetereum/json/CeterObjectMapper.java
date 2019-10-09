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
package org.cetereum.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * An extended {@link com.fasterxml.jackson.databind.ObjectMapper ObjectMapper} class to
 * customize cetereum state dumps.
 *
 * @author Alon Muroch
 */
public class CeterObjectMapper extends ObjectMapper {

    @Override
    public String writeValueAsString(Object value)
            throws JsonProcessingException {
        // alas, we have to pull the recycler directly here...
        SegmentedStringWriter sw = new SegmentedStringWriter(_jsonFactory._getBufferRecycler());
        try {
            JsonGenerator ge = _jsonFactory.createGenerator(sw);
            // set cetereum custom pretty printer
            CeterPrettyPrinter pp = new CeterPrettyPrinter();
            ge.setPrettyPrinter(pp);

            _configAndWriteValue(ge, value);
        } catch (JsonProcessingException e) { // to support [JACKSON-758]
            throw e;
        } catch (IOException e) { // shouldn't really happen, but is declared as possibility so:
            throw JsonMappingException.fromUnexpectedIOE(e);
        }
        return sw.getAndClear();
    }

    /**
     * An extended {@link com.fasterxml.jackson.core.util.DefaultPrettyPrinter} class to customize
     * an cetereum {@link com.fasterxml.jackson.core.PrettyPrinter Pretty Printer} Generator
     *
     * @author Alon Muroch
     */
    public class CeterPrettyPrinter extends DefaultPrettyPrinter {

        public CeterPrettyPrinter() {
            super();
        }

        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jg)
                throws IOException, JsonGenerationException {
            /**
             * Custom object separator (Default is " : ") to make it easier to compare state dumps with other
             * cetereum client implementations
             */
            jg.writeRaw(": ");
        }
    }
}
