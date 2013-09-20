package org.alfresco.messaging.amqp;

import org.alfresco.messaging.MessageConsumer;
import org.alfresco.messaging.amqp.AmqpDirectEndpoint;
import org.alfresco.messaging.jackson.ObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AmqpNodeBootstrapUtils
{

    public static AmqpDirectEndpoint createEndpoint(MessageConsumer messageConsumer, String... args)
    {
        validateArguments(args);
        
        AmqpDirectEndpoint messageProducer = new AmqpDirectEndpoint();
        ObjectMapper objectMapper = ObjectMapperFactory.createInstance();
        
        ((AmqpDirectEndpoint) messageProducer).setHost(args[0]);
        ((AmqpDirectEndpoint) messageProducer).setReceiveQueueName(args[1]);
        ((AmqpDirectEndpoint) messageProducer).setSendQueueName(args[2]);
        
        
        ((AmqpDirectEndpoint) messageProducer).setMessageConsumer(messageConsumer);
        ((AmqpDirectEndpoint) messageProducer).setObjectMapper(objectMapper);
        
        return messageProducer;
    }
    
    public static void validateArguments(String[] args)
    {
        if (args.length < 3)
        {
            throw new IllegalArgumentException("USAGE: host receiveQueueName replyQueueName");
        }
    }
    
}
