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
package org.gytheio.content.hash;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.gytheio.content.hash.HashReply;
import org.gytheio.content.hash.HashRequest;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.MessageProducer;

/**
 * A base implementation of a hash node which receives messages, uses a {@link ContentHashNodeWorker}
 * to perform the hash computation, then uses a {@link MessageProducer} to send the reply.
 * 
 * @author Ray Gauss II
 */
public class BaseContentHashNode implements MessageConsumer
{
    private static final Log logger = LogFactory.getLog(BaseContentHashNode.class);

    protected ContentHashNodeWorker hashWorker;
    protected MessageProducer messageProducer;
    
    /**
     * Sets the hash worker which does the actual work of the hash computation
     * 
     * @param hasherWorker
     */
    public void setWorker(ContentHashNodeWorker hasherWorker)
    {
        this.hashWorker = hasherWorker;
    }

    /**
     * Sets the message producer used to send replies
     * 
     * @param messageProducer
     */
    public void setMessageProducer(MessageProducer messageProducer)
    {
        this.messageProducer = messageProducer;
    }
    
    public void onReceive(Object message)
    {
        HashRequest request = (HashRequest) message;
        if (logger.isDebugEnabled())
        {
            logger.info("Processing hash requestId=" + request.getRequestId());
        }
        try
        {
            
            String value = hashWorker.generateHash(
                    request.getSourceContentReference(), 
                    request.getHashAlgorithm());
            
            HashReply reply = new HashReply(request);
            reply.setHexValue(value);
            
            if (logger.isDebugEnabled())
            {
                logger.debug("Sending reply");
            }
            messageProducer.send(reply, request.getReplyTo());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            // TODO send error reply
        }
    }
    
    public Class<?> getConsumingMessageBodyClass()
    {
        return HashRequest.class;
    }

}
