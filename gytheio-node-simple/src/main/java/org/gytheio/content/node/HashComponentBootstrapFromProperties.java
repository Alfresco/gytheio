package org.gytheio.content.node;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.hash.AbstractContentHashWorker;
import org.gytheio.content.hash.BaseContentHashComponent;

public class HashComponentBootstrapFromProperties<W extends AbstractContentHashWorker> extends 
        AbstractComponentBootstrapFromProperties<W>
{
    public HashComponentBootstrapFromProperties(Properties properties, W worker)
    {
        super(properties, worker);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AbstractComponent createComponent()
    {
        return new BaseContentHashComponent();
    }
    
    protected void initWorker()
    {
        ContentReferenceHandler sourceHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_SOURCE_PREFIX);
        worker.setSourceContentReferenceHandler(sourceHandler);
        worker.initialize();
    }

}
