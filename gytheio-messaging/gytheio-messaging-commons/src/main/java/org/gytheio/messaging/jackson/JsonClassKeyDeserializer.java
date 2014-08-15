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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class JsonClassKeyDeserializer extends KeyDeserializer
{

    public static Object deserializeKeyToClass(String key) throws IOException,
            JsonProcessingException
    {
        if (!key.startsWith("class "))
        {
            throw new IllegalArgumentException("Invalid key format");
        }
        String classname = key.replaceFirst("class ", "");
        try
        {
            return JsonClassKeyDeserializer.class.getClassLoader().loadClass(classname);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException,
            JsonProcessingException
    {
        return deserializeKeyToClass(key);
    }

}
