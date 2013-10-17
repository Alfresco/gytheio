package org.alfresco.repo.content.hash;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.alfresco.messaging.content.transport.ContentTransport;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.ContentServicePolicies.OnContentUpdatePolicy;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.hash.HashService;
import org.alfresco.util.PropertyCheck;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.ContentReference;
import org.gytheio.content.hash.HashReply;
import org.gytheio.content.hash.HashRequest;
import org.gytheio.messaging.RequestReplyMessageProducer;

public class HashServiceMessagingImpl implements HashService, OnContentUpdatePolicy
{
    private static final Log logger = LogFactory.getLog(HashServiceMessagingImpl.class);

    private static final String DEFAULT_HASH_ALGORITHM = HashRequest.HASH_ALGORITHM_SHA_512;
    private static final long SYNCHRONOUS_HASH_TIMEOUT_MS = 1000 * 60 * 2; // 2 minutes
    
    protected PolicyComponent policyComponent;
    protected ContentService contentService;
    
    protected ContentTransport contentTransport;
    protected RequestReplyMessageProducer<HashRequest, HashReply> requestReplyMessageProducer;
    protected ExecutorService executorService;
    protected boolean available;
    
    public HashServiceMessagingImpl()
    {
        this.available = false;
    }

    public void setPolicyComponent(PolicyComponent policyComponent)
    {
        this.policyComponent = policyComponent;
    }

    public void setContentService(ContentService contentService)
    {
        this.contentService = contentService;
    }

    public void setContentTransport(ContentTransport contentTransport)
    {
        this.contentTransport = contentTransport;
    }
    
    public void setRequestReplyMessageProducer(
            RequestReplyMessageProducer<HashRequest, HashReply> requestMessageProducer)
    {
        this.requestReplyMessageProducer = requestMessageProducer;
    }

    public void setExecutorService(ExecutorService executorService)
    {
        this.executorService = executorService;
    }

    /**
     * @return Returns true if the transformer is functioning otherwise false
     */
    public boolean isAvailable()
    {
        if (contentTransport != null)
        {
            return available && contentTransport.isAvailable();
        }
        return available;
    }
    
    /**
     * Make the transformer available
     * 
     * @param available
     */
    protected void setAvailable(boolean available)
    {
        this.available = available;
    }
    
    public void init()
    {
        PropertyCheck.mandatory(this, "policyComponent",  policyComponent);
        PropertyCheck.mandatory(this, "contentService",  contentService);
        PropertyCheck.mandatory(this, "contentTransport",  contentTransport);
        PropertyCheck.mandatory(this, "requestMessageProducer",  requestReplyMessageProducer);
        
        if (executorService == null)
        {
            executorService = Executors.newCachedThreadPool();
        }
        
        this.policyComponent.bindClassBehaviour(
                OnContentUpdatePolicy.QNAME,
                ContentModel.TYPE_CONTENT,
                new JavaBehaviour(this, "onContentUpdate"));
    }
    
    @Override
    public Future<String> generateHashAsync(ContentReader reader, String hashAlgorithm)
            throws ContentIOException
    {
        try
        {
            // Get a reference to write to on the remote system
            ContentReference remoteSourceContentReference = 
                    contentTransport.createContentReference(reader.getMimetype());
            
            // Write source to remote content system
            contentTransport.write(reader, remoteSourceContentReference);
            
            HashRequest request = new HashRequest(
                    remoteSourceContentReference, DEFAULT_HASH_ALGORITHM);
            
            if (logger.isDebugEnabled())
            {
                logger.debug("sending hash request " + request.getRequestId());
            }
            
            FutureTask<String> hashValueFuture = new FutureTask<String>(
                    new ExtractHashValueCallable(requestReplyMessageProducer.asyncRequest(request)));
            
            executorService.execute(hashValueFuture);
            
            return hashValueFuture;
        }
        catch (Exception e)
        {
            throw new ContentIOException(e.getMessage(), e);
        }
    }
    
    @Override
    public String generateHash(ContentReader reader, String hashAlgorithm) throws ContentIOException
    {
        Future<String> hashValueFuture = generateHashAsync(reader, hashAlgorithm);
        try
        {
            return hashValueFuture.get(SYNCHRONOUS_HASH_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e)
        {
            // We were asked to stop
            hashValueFuture.cancel(true);
            return null;
        }
        catch (ExecutionException e)
        {
            throw new ContentIOException("Hash failed: " + e.getMessage(), e);
        }
        catch (TimeoutException e)
        {
            throw new ContentIOException("Hash timed out: " + e.getMessage(), e);
        }
    }
    
    public class ExtractHashValueCallable implements Callable<String>
    {
        private Future<HashReply> replyFuture;
        
        public ExtractHashValueCallable(Future<HashReply> replyFuture)
        {
            this.replyFuture = replyFuture;
        }

        @Override
        public String call() throws Exception
        {
            HashReply reply = replyFuture.get();
            return reply.getHexValue();
        }
    }

    @Override
    public void onContentUpdate(NodeRef nodeRef, boolean newContent)
    {
        if (nodeRef == null)
        {
            return;
        }
        
        ContentReader reader = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
        String hashValue = generateHash(reader, DEFAULT_HASH_ALGORITHM);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("nodeRef:" + nodeRef.toString() + " " + DEFAULT_HASH_ALGORITHM + "=" + hashValue);
        }
        
        // TODO - check that content version has not changed then save value as property in the Versionable aspect
    }

}
