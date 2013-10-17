package org.gytheio.messaging.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QpidBodyCleanerObjectMapper extends ObjectMapper
{
    private static final long serialVersionUID = 2568701685293341501L;
    
    private static final String DEFAULT_ENCODING = "utf8";

    public <T> T readValue(InputStream inputStream, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException
    {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, DEFAULT_ENCODING);
        String content = writer.toString();
        content = content.substring(content.indexOf("{"), content.length());
        return readValue(content, valueType);
    }
}
