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
package org.gytheio.content.transform;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorkResult;
import org.gytheio.content.transform.TransformationReply;
import org.gytheio.content.transform.TransformationRequest;
import org.gytheio.messaging.MessageProducer;

/**
 * A base implementation of a transform node which receives messages, uses a {@link ContentTransformerWorker}
 * to perform the transformation, then uses a {@link MessageProducer} to send the reply.
 * 
 * @author Ray Gauss II
 */
public class BaseContentTransformerComponent 
    extends AbstractComponent<ContentTransformerWorker>
{
    private static final Log logger = LogFactory.getLog(BaseContentTransformerComponent.class);
    
    protected TransformationRequest lastRequest;
    
    /**
     * Gets the last transformation request received.
     * <p>
     * This is simply the last request, stored in-memory, with no indication
     * as to it's status, it may have been completed days ago.
     * 
     * @return the last request received
     */
    public TransformationRequest getLastRequest()
    {
        return lastRequest;
    }
    
    @Override
    protected void onReceiveImpl(Object message)
    {
        TransformationRequest request = (TransformationRequest) message;
        lastRequest = request;
        logger.info("Processing transformation requestId=" + request.getRequestId());
        ContentTransformerWorkerProgressReporterImpl progressReporter =
                new ContentTransformerWorkerProgressReporterImpl(request);
        try
        {
            progressReporter.onTransformationStarted();
            
            List<ContentWorkResult> results = worker.transform(
                    request.getSourceContentReferences(), 
                    request.getTargetContentReferences(), 
                    request.getOptions(),
                    progressReporter);
            
            progressReporter.onTransformationComplete(results);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            progressReporter.onTransformationError(e.getMessage());
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
    public class ContentTransformerWorkerProgressReporterImpl implements ContentTransformerWorkerProgressReporter
    {
        private TransformationRequest request;
        
        public ContentTransformerWorkerProgressReporterImpl(TransformationRequest request)
        {
            this.request = request;
        }
        
        public void onTransformationStarted()
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
        
        public void onTransformationProgress(float progress)
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
        
        public void onTransformationComplete(List<ContentWorkResult> results)
        {
            if (logger.isInfoEnabled())
            {
                logger.info("Completed transformation of " +
                        "requestId=" + request.getRequestId());
            }
            TransformationReply reply = new TransformationReply(request);
            reply.setStatus(TransformationReply.STATUS_COMPLETE);
            reply.setResults(results);
            
            messageProducer.send(reply, request.getReplyTo());
        }

        @Override
        public void onTransformationError(String errorMessage)
        {
            TransformationReply reply = new TransformationReply(request);
            reply.setStatus(TransformationReply.STATUS_ERROR);
            reply.setStatusDetail(errorMessage);
            
            messageProducer.send(reply, request.getReplyTo());
        }
    }

}
