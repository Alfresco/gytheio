package org.alfresco.repo.content.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.gytheio.content.ContentReference;
import org.gytheio.content.transform.TransformationReply;
import org.gytheio.content.transform.TransformationRequest;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.MessageProducer;
import org.gytheio.messaging.MessagingException;

public class MessagingContentTransformerWorkerImpl 
        extends AbstractRemoteContentTransformerWorker 
        implements MessageConsumer, MessagingContentTransformerWorker
{
    private static final Log logger = LogFactory.getLog(MessagingContentTransformerWorkerImpl.class);

    private static final long PENDING_TRANSFORM_POLLING_INTERVAL_MS = 500;

    // TODO: In a clustered env this would have to be distributed (Hazelcast) or persisted
    protected Map<String, PendingMessagingTransformation> pendingTransformations = 
            new HashMap<String, PendingMessagingTransformation>();
    
    protected TransformationOptionsMessageConverter optionsConverter;
    protected MessageProducer messageProducer;
    protected String version;
    
    public void setOptionsConverter(TransformationOptionsMessageConverter optionsConverter)
    {
        this.optionsConverter = optionsConverter;
    }

    public void setMessageProducer(MessageProducer messageProducer)
    {
        this.messageProducer = messageProducer;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String transform(ContentReader reader, ContentWriter writer, 
            org.alfresco.service.cmr.repository.TransformationOptions options,
            boolean executeAsynchronously) throws ContentIOException
    {
        ContentReference remoteSourceContentReference;
        ContentReference remoteTargetContentReference;
        try
        {
            // Get a reference to write to on the remote system
            remoteSourceContentReference = 
                    contentTransport.createContentReference(reader.getMimetype());
            
            // Write source to remote content system
            contentTransport.write(reader, remoteSourceContentReference);
            
            // Get a reference for the target on the remote system
            remoteTargetContentReference = 
                    contentTransport.createContentReference(writer.getMimetype());
        }
        catch (Exception e)
        {
            throw new ContentIOException(e.getMessage());
        }
            
        // Convert the options to a simple object for data formatters
        org.alfresco.content.transform.options.TransformationOptions requestOptions = 
                optionsConverter.convert(options);
        
        TransformationRequest request = new TransformationRequest(
                remoteSourceContentReference, remoteTargetContentReference, requestOptions);
        
        logger.debug("sending transformation request " + request.getRequestId());
        
        try
        {
            messageProducer.send(request);
        }
        catch (MessagingException e)
        {
            throw new ContentIOException("Messaging exception during transform: " + e.getMessage(), e);
        }
        
        pendingTransformations.put(request.getRequestId(), 
                new PendingMessagingTransformation(request, reader, writer, executeAsynchronously));
        
        if (executeAsynchronously)
        {
            return request.getRequestId();
        }
        else
        {
            long startTime = (new Date()).getTime();
            long timeout = options.getTimeoutMs();
            try {
                while (((new Date()).getTime() - startTime <= timeout) || timeout == -1)
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Polling for pending transformation " + request.getRequestId() +
                                " completion in " + PENDING_TRANSFORM_POLLING_INTERVAL_MS + "ms");
                    }
                    Thread.sleep(PENDING_TRANSFORM_POLLING_INTERVAL_MS);
                    PendingMessagingTransformation pendingTransformation = pendingTransformations.get(
                            request.getRequestId());
                    if (pendingTransformation.getStatus() == null)
                    {
                        // in-progress confirmation hasn't come back yet
                        continue;
                    }
                    if (pendingTransformation.getStatus().equals(TransformationReply.STATUS_COMPLETE))
                    {
                        return request.getRequestId();
                    }
                    if (pendingTransformation.getStatus().equals(TransformationReply.STATUS_ERROR))
                    {
                        throw new ContentIOException("Could not complete transformation: " + 
                                pendingTransformation.getDetail());
                    }
                }
                // We must have timed out
                throw new ContentIOException("Time out while waiting for transformation reply");
            }
            catch (InterruptedException e)
            {
                // We were asked to stop
            }
        }
        return null;
    }

    public Float getProgress(String runningTransformationId)
    {
        if (runningTransformationId == null)
        {
            return null;
        }
        PendingMessagingTransformation transformation = pendingTransformations.get(runningTransformationId);
        if (transformation == null)
        {
            // TODO throw exception instead
            return null;
        }
        return transformation.getProgress();
    }
    
    public void onReceive(Object message)
    {
        if (!(message instanceof TransformationReply))
        {
            logger.error("Incorrect message type!");
            // TODO Better handling here, dead letter?
            return;
        }
        
        TransformationReply transformationReply = (TransformationReply) message;
        
        if (logger.isDebugEnabled())
        {
            logger.debug("Received reply for transformation " + transformationReply.getRequestId() +
                    " with status=" + transformationReply.getStatus());
        }
        
        PendingMessagingTransformation pendingTransformation = pendingTransformations.get(
                transformationReply.getRequestId());
        if (pendingTransformation == null)
        {
            // TODO Need to better handle errors here, send an error message?
            logger.error("Unknown pending transformation: " + 
                    transformationReply.getRequestId());
            return;
        }
        pendingTransformation.setLastReply(transformationReply);
        if (transformationReply.getStatus().equals(TransformationReply.STATUS_COMPLETE))
        {
            try
            {
                contentTransport.read(transformationReply.getTargetContentReference(), 
                        pendingTransformation.getWriter());
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
            
            if (pendingTransformation.getExecuteAsynchronously())
            {
                pendingTransformations.remove(transformationReply.getRequestId());
            }
            
            if (logger.isDebugEnabled())
            {
                logger.debug("Transformation " + transformationReply.getRequestId() + " complete");
            }
        }
        else if (transformationReply.getStatus().equals(TransformationReply.STATUS_ERROR))
        {
            logger.error(transformationReply.getDetail());
            if (pendingTransformation.getExecuteAsynchronously())
            {
                pendingTransformations.remove(transformationReply.getRequestId());
            }
        }
    }
    
    public Class<?> getConsumingMessageBodyClass()
    {
        return TransformationReply.class;
    }
    
    public void transform(ContentReader reader, ContentWriter writer) throws ContentIOException
    {
        transform(reader, writer, null, false);
    }
    
    public void transform(ContentReader reader, ContentWriter writer, 
            org.alfresco.service.cmr.repository.TransformationOptions options) throws ContentIOException
    {
        transform(reader, writer, options, false);
    }

    public String getVersionString()
    {
        return version;
    }

    public long getTransformationTime()
    {
        return 0;
    }

    public long getTransformationTime(String sourceMimetype, String targetMimetype)
    {
        return 0;
    }

    @Deprecated
    public void transform(ContentReader reader, ContentWriter writer, Map<String, Object> options)
            throws ContentIOException
    {
        throw new UnsupportedOperationException("options map is not supported");
    }

    public boolean isTransformable(String sourceMimetype, String targetMimetype, 
            org.alfresco.service.cmr.repository.TransformationOptions options)
    {
        // The content transformer which wraps this worker can be configured for the mime types supported
        return true;
    }

}
