package org.alfresco.repo.content.transform;

import org.apache.camel.Handler;

public interface MessagingContentTransformerWorker extends AsyncContentTransformerWorker
{
    
    @Handler
    public void onReceive(Object message);

}
