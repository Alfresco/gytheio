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

public class CamelMessageProducer implements MessageProducer
{
    private static final Log logger = LogFactory.getLog(CamelMessageProducer.class);
    
//    private static final String ENDPOINT_URI_AMQP = "amqp";
    
    private ProducerTemplate producer;
    private String endpoint;

    public void setProducer(ProducerTemplate producer)
    {
        this.producer = producer;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }


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
//            if (endpoint.startsWith(ENDPOINT_URI_AMQP))
//            {
                // Hack for broken JMS to AMQP conversion
                producer.sendBodyAndHeader(endpoint, message, "JMS_AMQP_MESSAGE_FORMAT", 0L);
//            }
//            else
//            {
//                producer.sendBody(endpoint, stringMessage);
//            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

}
