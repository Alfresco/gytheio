package org.alfresco.repo.content.transform;

import org.alfresco.messaging.content.transport.ContentTransport;

public abstract class AbstractRemoteContentTransformerWorker extends ContentTransformerHelper implements 
        AsyncContentTransformerWorker
{
    protected ContentTransport contentTransport;
    protected boolean available;
    
    public AbstractRemoteContentTransformerWorker()
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

}
