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
package org.gytheio.content.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.gytheio.content.transform.TransformationReply;
import org.gytheio.content.transform.TransformationRequest;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.MessageProducer;
import org.gytheio.messaging.MessagingException;

/**
 * A base implementation of a transform node which receives messages, uses a {@link ContentTransformerNodeWorker}
 * to perform the transformation, then uses a {@link MessageProducer} to send the reply.
 * 
 * @author Ray Gauss II
 */
public class BaseContentTransformerNode implements MessageConsumer
{
    private static final Log logger = LogFactory.getLog(BaseContentTransformerNode.class);

    protected ContentTransformerNodeWorker transformerWorker;
    protected MessageProducer messageProducer;
    
    /**
     * Sets the transformer worker which does the actual work of the transformation
     * 
     * @param transformerWorker
     */
    public void setWorker(ContentTransformerNodeWorker transformerWorker)
    {
        this.transformerWorker = transformerWorker;
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
        TransformationRequest request = (TransformationRequest) message;
        logger.info("Processing transformation request " + request.getRequestId());
        ContentTransformerNodeWorkerProgressReporterImpl progressReporter =
                new ContentTransformerNodeWorkerProgressReporterImpl(request);
        try
        {
            progressReporter.onTransformationStarted();
            
            transformerWorker.transform(
                    request.getSourceContentReference(), 
                    request.getTargetContentReference(), 
                    request.getOptions(),
                    progressReporter);
            
            progressReporter.onTransformationComplete();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            // TODO send error reply
        }
    }
    
    public Class<?> getConsumingMessageBodyClass()
    {
        return TransformationRequest.class;
    }
    
    /**
     * Implementation of the progress reporter which sends reply messages with
     * progress on the transformation.
     */
    public class ContentTransformerNodeWorkerProgressReporterImpl implements ContentTransformerNodeWorkerProgressReporter
    {
        private TransformationRequest request;
        
        public ContentTransformerNodeWorkerProgressReporterImpl(TransformationRequest request)
        {
            this.request = request;
        }
        
        public void onTransformationStarted() throws MessagingException
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Starting transformation of " +
                        "requestId=" + request.getRequestId());
            }
            TransformationReply reply = 
                    new TransformationReply(request);
            reply.setStatus(TransformationReply.STATUS_IN_PROGRESS);
            messageProducer.send(reply, request.getReplyTo());
        }
        
        public void onTransformationProgress(float progress) throws MessagingException
        {
            if (logger.isDebugEnabled())
            {
                logger.debug(progress*100 + "% progress on transformation of " +
                        "requestId=" + request.getRequestId());
            }
            TransformationReply reply = new TransformationReply(request);
            reply.setStatus(TransformationReply.STATUS_IN_PROGRESS);
            reply.setProgress(progress);
            messageProducer.send(reply, request.getReplyTo());
        }
        
        public void onTransformationComplete() throws MessagingException
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Completed transformation of " +
                        "requestId=" + request.getRequestId());
            }
            TransformationReply reply = new TransformationReply(request);
            reply.setStatus(TransformationReply.STATUS_COMPLETE);
            messageProducer.send(reply, request.getReplyTo());
        }
    }

}
