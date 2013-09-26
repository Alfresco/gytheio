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
package org.alfresco.messaging.camel;

import org.alfresco.messaging.MessageProducer;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An Apache Camel implementation of a message producer
 * 
 * @author Ray Gauss II
 */
public class CamelMessageProducer implements MessageProducer
{
    private static final Log logger = LogFactory.getLog(CamelMessageProducer.class);
    
    protected static final String HEADER_JMS_AMQP_MESSAGE_FORMAT = "JMS_AMQP_MESSAGE_FORMAT";
    protected static final Long HEADER_JMS_AMQP_MESSAGE_FORMAT_VALUE = 0L;
    
    protected ProducerTemplate producer;
    protected String endpoint;

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
    
    public void send(Object message)
    {
        try
        {
            // Hack for broken JMS to AMQP conversion
            producer.sendBodyAndHeader(endpoint, message, 
                    HEADER_JMS_AMQP_MESSAGE_FORMAT, HEADER_JMS_AMQP_MESSAGE_FORMAT_VALUE);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void send(Object message, String queueName)
    {
        try
        {
            // Hack for broken JMS to AMQP conversion
            producer.sendBodyAndHeader(queueName, message, 
                    HEADER_JMS_AMQP_MESSAGE_FORMAT, HEADER_JMS_AMQP_MESSAGE_FORMAT_VALUE);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

}
