package org.alfresco.repo.content.transform;

import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.gytheio.content.transform.TransformationReply;
import org.gytheio.content.transform.TransformationRequest;

public class PendingMessagingTransformation
{
    private TransformationRequest request;
    private ContentReader reader;
    private ContentWriter writer;
    private TransformationReply lastReply;
    private boolean executeAsynchronously;
    
    public PendingMessagingTransformation(
            TransformationRequest request, 
            ContentReader reader, 
            ContentWriter writer, 
            boolean executeAsynchronously)
    {
        super();
        this.request = request;
        this.reader = reader;
        this.writer = writer;
        this.executeAsynchronously = executeAsynchronously;
    }

    public TransformationRequest getRequest()
    {
        return request;
    }
    
    public void setRequest(TransformationRequest request)
    {
        this.request = request;
    }
    
    public ContentReader getReader()
    {
        return reader;
    }

    public void setReader(ContentReader reader)
    {
        this.reader = reader;
    }

    public ContentWriter getWriter()
    {
        return writer;
    }
    
    public void setWriter(ContentWriter writer)
    {
        this.writer = writer;
    }
    
    public boolean getExecuteAsynchronously()
    {
        return executeAsynchronously;
    }

    public void setExecuteAsynchronously(boolean executeAsynchronously)
    {
        this.executeAsynchronously = executeAsynchronously;
    }

    public String getStatus()
    {
        if (lastReply == null)
        {
            return null;
        }
        return lastReply.getStatus();
    }
    
    public String getDetail()
    {
        if (lastReply == null)
        {
            return null;
        }
        return lastReply.getDetail();
    }

    public Float getProgress()
    {
        if (lastReply == null)
        {
            return null;
        }
        return lastReply.getProgress();
    }

    public void setLastReply(TransformationReply reply)
    {
        this.lastReply = reply;
    }

}
