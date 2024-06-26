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
package org.gytheio.content.hash;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorkResult;
import org.gytheio.content.hash.HashReply;
import org.gytheio.content.hash.HashRequest;
import org.gytheio.messaging.MessageProducer;

/**
 * A base implementation of a hash node which receives messages, uses a {@link ContentHashWorker}
 * to perform the hash computation, then uses a {@link MessageProducer} to send the reply.
 * 
 * @author Ray Gauss II
 */
public class BaseContentHashComponent extends AbstractComponent<ContentHashWorker>
{
    private static final Log logger = LogFactory.getLog(BaseContentHashComponent.class);
    
    protected void onReceiveImpl(Object message)
    {
        HashRequest request = (HashRequest) message;
        if (logger.isDebugEnabled())
        {
            logger.info("Processing hash requestId=" + request.getRequestId());
        }
        try
        {
            
            List<ContentWorkResult> results = worker.generateHashes(
                    request.getSourceContentReferences(), 
                    request.getHashAlgorithm());
            
            HashReply reply = new HashReply(request);
            reply.setResults(results);
            
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
