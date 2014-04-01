package org.gytheio.messaging.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class ObjectMapperFactory
{

    public static ObjectMapper createInstance()
    {
        
        QpidJsonBodyCleanerObjectMapper mapper = new QpidJsonBodyCleanerObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        SimpleModule module = new SimpleModule("GytheioJackson", 
                new Version(0, 1, 0, "SNAPSHOT", "org.gytheio", "gytheio-messaging-commons"));
        module.addKeyDeserializer(Class.class, new JsonClassKeyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

}
