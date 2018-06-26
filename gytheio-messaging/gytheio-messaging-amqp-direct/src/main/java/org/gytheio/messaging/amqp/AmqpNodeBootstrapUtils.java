/*
 * Copyright (C) 2005-2018 Alfresco Software Limited.
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
package org.gytheio.messaging.amqp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.jackson.ObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A utility class for helping to bootstrap AMQP nodes from command line arguments
 * 
 * @author Ray Gauss II
 */
public class AmqpNodeBootstrapUtils
{

    public static final String PROP_MESSAGING_BROKER_URL = "gytheio.messaging.broker.url";
    public static final String PROP_MESSAGING_BROKER_USERNAME = "gytheio.messaging.broker.username";
    public static final String PROP_MESSAGING_BROKER_PASSWORD = "gytheio.messaging.broker.password";
    public static final String PROP_MESSAGING_QUEUE_REQUEST = "gytheio.messaging.queue.request";
    public static final String PROP_MESSAGING_QUEUE_REPLY = "gytheio.messaging.queue.reply";

    /**
     * Creates an AMQP endpoint (sender and receiver) from the given arguments
     * 
     * @param messageConsumer the processor received messages are sent to
     * @param args
     * @return
     */
    public static AmqpDirectEndpoint createEndpoint(MessageConsumer messageConsumer,
            String brokerUrl,
            String brokerUsername, String brokerPassword,
            String requestEndpoint, String replyEndpoint)
    {
        validate(brokerUrl, requestEndpoint, replyEndpoint);
        
        AmqpDirectEndpoint messageProducer = new AmqpDirectEndpoint();
        ObjectMapper objectMapper = ObjectMapperFactory.createInstance();
        
        URI broker = null;
        try
        {
            broker = new URI(brokerUrl);
        }
        catch (URISyntaxException e)
        {
            // This would have been caught by validation above
        }
        
        messageProducer.setHost(broker.getHost());
        Integer brokerPort = broker.getPort();
        if (brokerPort != null)
        {
            messageProducer.setPort(brokerPort);
        }
        
        String brokerScheme = broker.getScheme();
        if ((brokerScheme != null) && (brokerScheme.equals("amqps")||brokerScheme.equals("amqp+ssl")))
        {
            messageProducer.setIsSSL(true);
        }
        
        if (brokerUsername != null)
        {
            messageProducer.setUsername(brokerUsername);
        }
        
        if (brokerPassword != null)
        {
            messageProducer.setPassword(brokerPassword);
        }
        
        messageProducer.setReceiveEndpoint(requestEndpoint);
        messageProducer.setSendEndpoint(replyEndpoint);
        
        messageProducer.setMessageConsumer(messageConsumer);
        messageProducer.setObjectMapper(objectMapper);
        
        return messageProducer;
    }
    
    public static AmqpDirectEndpoint createEndpoint(MessageConsumer messageConsumer, Properties properties)
    {
        String brokerUrl = properties.getProperty(PROP_MESSAGING_BROKER_URL);
        String brokerUsername = properties.getProperty(PROP_MESSAGING_BROKER_USERNAME);
        String brokerPassword = properties.getProperty(PROP_MESSAGING_BROKER_PASSWORD);
        String receiveQueueName = properties.getProperty(PROP_MESSAGING_QUEUE_REQUEST);
        String replyQueueName = properties.getProperty(PROP_MESSAGING_QUEUE_REPLY);
        validate(brokerUrl, receiveQueueName, replyQueueName);
        return createEndpoint(messageConsumer, 
                brokerUrl, brokerUsername, brokerPassword,
                receiveQueueName, replyQueueName);
    }
    
    public static void validate(
            String brokerUrl, String requestQueueName, String replyQueueName)
    {
        if (StringUtils.isEmpty(brokerUrl) || 
                (StringUtils.isEmpty(requestQueueName) &&
                StringUtils.isEmpty(replyQueueName)))
        {
            throw new IllegalArgumentException(
                    "brokerUrl, requestQueueName, and replyQueueName must not be empty");
        }
        try
        {
            new URI(brokerUrl);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
    
}
