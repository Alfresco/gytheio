package org.alfresco.repo.content.hash;

import java.util.HashSet;
import java.util.Set;

import org.alfresco.content.ContentReference;
import org.alfresco.content.hash.HashRequest;
import org.alfresco.messaging.MessageProducer;
import org.alfresco.messaging.content.transport.ContentTransport;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HashServiceMessagingImpl implements HashService
{
    private static final Log logger = LogFactory.getLog(HashServiceMessagingImpl.class);

    private static final String DEFAULT_HASH_ALGORITHM = HashRequest.HASH_ALGORITHM_SHA_512;
    
    protected ContentTransport contentTransport;
    protected MessageProducer messageProducer;
    protected boolean available;
    
    // TODO: In a clustered env this would have to be distributed (Hazelcast) or persisted
    protected Set<String> pendingHashes = new HashSet<String>();
    
    public HashServiceMessagingImpl()
    {
        this.available = false;
    }

    public void setContentTransport(ContentTransport contentTransport)
    {
        this.contentTransport = contentTransport;
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
    
    @Override
    public String hash(ContentReader reader) throws ContentIOException
    {
        ContentReference remoteSourceContentReference;
        try
        {
            // Get a reference to write to on the remote system
            remoteSourceContentReference = 
                    contentTransport.createContentReference(reader.getMimetype());
            
            // Write source to remote content system
            contentTransport.write(reader, remoteSourceContentReference);
        }
        catch (Exception e)
        {
            throw new ContentIOException(e.getMessage());
        }
        
        HashRequest request = new HashRequest(
                remoteSourceContentReference, DEFAULT_HASH_ALGORITHM);
        
        logger.debug("sending transformation request " + request.getRequestId());
        
        messageProducer.send(request);
        
        pendingHashes.add(request.getRequestId());
        
        // TODO return hash
        return null;
    }

}
