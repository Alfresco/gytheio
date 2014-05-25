/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Gytheio
 *
 * Gytheio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gytheio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Gytheio. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.messaging.camel.dataformat;

import static junit.framework.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.gytheio.messaging.camel.dataformat.SimplePojo.EnumValue;
import org.gytheio.messaging.jackson.ObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDataFormatTest
{
    private static final String EXPECTED_FIELD1_VALUE = "value1";
    private static final Integer EXPECTED_FIELD2_VALUE = new Integer(2);
    private static final EnumValue EXPECTED_FIELD3_VALUE = EnumValue.VALUE_1;
    private static final String EXPECTED_JSON = "{" +
            "\"@class\":\"" + SimplePojo.class.getCanonicalName() + "\"," +
            "\"field1\":\"" + EXPECTED_FIELD1_VALUE + "\"," +
            "\"field2\":" + EXPECTED_FIELD2_VALUE + "," +
            "\"field3\":\"" + EXPECTED_FIELD3_VALUE + "\"" +
            "}";
    
    private JacksonDataFormat dataFormat;
    
    @Before
    public void init()
    {
        ObjectMapper mapper = ObjectMapperFactory.createInstance();
        dataFormat = new JacksonDataFormat(mapper, Object.class);
    }
    
    @Test
    public void testMarshalPojoToJson() throws Exception
    {
        SimplePojo simplePojo = new SimplePojo(
                EXPECTED_FIELD1_VALUE, 
                EXPECTED_FIELD2_VALUE,
                EXPECTED_FIELD3_VALUE);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dataFormat.marshal(null, simplePojo, os);
        
        String result = new String(os.toByteArray(), "UTF-8");
        
        assertEquals(EXPECTED_JSON, result);
    }
    
    @Test
    public void testUnmarshalJsonToPojo() throws Exception
    {
        InputStream is = new ByteArrayInputStream(EXPECTED_JSON.getBytes("UTF-8"));
        SimplePojo simplePojo = (SimplePojo) dataFormat.unmarshal(null, is);
        assertEquals(EXPECTED_FIELD1_VALUE, simplePojo.getField1());
        assertEquals(EXPECTED_FIELD2_VALUE, simplePojo.getField2());
        assertEquals(EXPECTED_FIELD3_VALUE, simplePojo.getField3());
    }
    
    @Test
    public void testUnmarshalQpidBodyJsonToPojo() throws Exception
    {
        String messageBody = "sfjh09434" + EXPECTED_JSON;
        InputStream is = new ByteArrayInputStream(messageBody.getBytes("UTF-8"));
        SimplePojo simplePojo = (SimplePojo) dataFormat.unmarshal(null, is);
        assertEquals(EXPECTED_FIELD1_VALUE, simplePojo.getField1());
        assertEquals(EXPECTED_FIELD2_VALUE, simplePojo.getField2());
        assertEquals(EXPECTED_FIELD3_VALUE, simplePojo.getField3());
    }

}
