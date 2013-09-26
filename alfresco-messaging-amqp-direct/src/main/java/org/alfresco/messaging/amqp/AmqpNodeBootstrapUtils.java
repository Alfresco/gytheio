/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.messaging.amqp;

import org.alfresco.messaging.MessageConsumer;
import org.alfresco.messaging.amqp.AmqpDirectEndpoint;
import org.alfresco.messaging.jackson.ObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A utility class for helping to bootstrap AMQP nodes from command line arguments
 * 
 * @author Ray GAuss II
 */
public class AmqpNodeBootstrapUtils
{

    /**
     * Creates an AMQP endpoint (sender and receiver) from the given arguments
     * 
     * @param messageConsumer the processor received messages are sent to
     * @param args
     * @return
     */
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
            throw new IllegalArgumentException("USAGE: host receiveQueueName sendQueueName");
        }
    }
    
}
