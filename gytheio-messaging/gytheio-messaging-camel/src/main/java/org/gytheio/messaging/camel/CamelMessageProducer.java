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
package org.gytheio.messaging.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.gytheio.messaging.MessageProducer;
import org.gytheio.messaging.MessagingException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An Apache Camel implementation of a message producer
 * 
 * @author Ray Gauss II
 */
public class CamelMessageProducer implements MessageProducer
{
    protected static final String HEADER_JMS_AMQP_MESSAGE_FORMAT = "JMS_AMQP_MESSAGE_FORMAT";
    protected static final Long HEADER_JMS_AMQP_MESSAGE_FORMAT_VALUE = 0L;
    protected static final String ERROR_SENDING = "Could not send message";
    
    protected ProducerTemplate producer;
    protected String endpoint;
    protected ObjectMapper objectMapper;

    /**
     * The Camel producer template
     * 
     * @param producer
     */
    public void setProducer(ProducerTemplate producer)
    {
        this.producer = producer;
    }

    /**
     * The Camel endpoint for initial delivery of the messages into the Camel context which
     * can then be routed as needed
     * 
     * @param endpoint
     */
    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }
    
    /**
     * Optional object mapper to be used programatically in {@link #send(Object, String)}
     * or {@link #send(Object, String, Map)} methods when an alternate endpoint is specified.
     * 
     * @param objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    /**
     * Checks that the given endpoint is valid
     * 
     * @param endpoint
     */
    protected void validateEndpoint(String endpoint)
    {
        if (endpoint == null)
        {
            throw new IllegalArgumentException("endpoint must not be null");
        }
    }
    
    protected Map<String, Object> addHeaders(Map<String, Object> origHeaders)
    {
        if (origHeaders == null)
        {
            origHeaders = new HashMap<String, Object>();
        }
        // Hack for broken JMS to AMQP conversion
        origHeaders.put(HEADER_JMS_AMQP_MESSAGE_FORMAT, HEADER_JMS_AMQP_MESSAGE_FORMAT_VALUE);
        return origHeaders;
    }
    
    public void send(Object message)
    {
        try
        {
            producer.sendBodyAndHeaders(endpoint, message, addHeaders(null));
        }
        catch (Exception e)
        {
            throw new MessagingException(ERROR_SENDING, e);
        }
    }
    
    public void send(Object message, Map<String, Object> headers)
    {
        try
        {
            producer.sendBodyAndHeaders(endpoint, message, addHeaders(headers));
        }
        catch (Exception e)
        {
            throw new MessagingException(ERROR_SENDING, e);
        }
    }
    
    public void send(Object message, String queueName)
    {
        try
        {
            if (StringUtils.isEmpty(queueName))
            {
                queueName = endpoint;
            }
            else
            {
                if (objectMapper != null && !(message instanceof String))
                {
                    message = objectMapper.writeValueAsString(message);
                }
            }
            producer.sendBodyAndHeaders(queueName, message, addHeaders(null));
        }
        catch (Exception e)
        {
            throw new MessagingException(ERROR_SENDING, e);
        }
    }
    
    public void send(Object message, String queueName, Map<String, Object> headers)
    {
        try
        {
            if (StringUtils.isEmpty(queueName))
            {
                queueName = endpoint;
            }
            else
            {
                if (objectMapper != null && !(message instanceof String))
                {
                    message = objectMapper.writeValueAsString(message);
                }
            }
            producer.sendBodyAndHeaders(queueName, message, addHeaders(headers));
        }
        catch (Exception e)
        {
            throw new MessagingException(ERROR_SENDING, e);
        }
    }

}
