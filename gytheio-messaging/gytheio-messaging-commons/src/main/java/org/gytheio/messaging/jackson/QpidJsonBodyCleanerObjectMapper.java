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
package org.gytheio.messaging.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Extension of ObjectMapper which cleans erroneous characters apparently
 * added by the Qpid library before the start of a JSON object.
 */
public class QpidJsonBodyCleanerObjectMapper extends ObjectMapper
{
    private static final long serialVersionUID = 2568701685293341501L;
    
    private static final String DEFAULT_ENCODING = "utf8";
    
    public <T> T readValue(InputStream inputStream, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException
    {
        try
        {
            // Try to unmarshal normally
            if (inputStream.markSupported())
            {
                inputStream.mark(1024 * 512);
            }
            return super.readValue(inputStream, valueType);
        }
        catch (JsonParseException e)
        {
            if (!inputStream.markSupported())
            {
                // We can't reset this stream, bail out
                throw e;
            }
            // Reset the stream
            inputStream.reset();
        }
        // Clean the message body and try again
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, DEFAULT_ENCODING);
        String content = writer.toString();
        content = content.substring(content.indexOf("{"), content.length());
        return readValue(content, valueType);
    }
}
