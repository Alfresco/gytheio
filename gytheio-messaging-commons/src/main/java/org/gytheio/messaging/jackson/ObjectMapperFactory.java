package org.gytheio.messaging.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperFactory
{

    public static ObjectMapper createInstance()
    {
        
        QpidBodyCleanerObjectMapper mapper = new QpidBodyCleanerObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enableDefaultTyping();
        SimpleModule module = new SimpleModule("AlfrescoJackson", 
                new Version(4, 2, 0, "SNAPSHOT", "org.alfresco", "alfresco-messaging-commons"));
        module.addKeyDeserializer(Class.class, new JsonClassKeyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

}
