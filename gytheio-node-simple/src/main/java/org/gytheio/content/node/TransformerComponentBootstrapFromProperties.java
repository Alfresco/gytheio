package org.gytheio.content.node;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.transform.AbstractContentTransformerWorker;
import org.gytheio.content.transform.BaseContentTransformerComponent;

public class TransformerComponentBootstrapFromProperties<W extends AbstractContentTransformerWorker> extends 
        AbstractComponentBootstrapFromProperties<W>
{
    protected static final String PROP_WORKER_CONTENT_REF_HANDLER_TARGET_PREFIX = 
            "gytheio.worker.contentrefhandler.target";
    
    public TransformerComponentBootstrapFromProperties(Properties properties, W worker)
    {
        super(properties, worker);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected AbstractComponent createComponent()
    {
        return new BaseContentTransformerComponent();
    }
    
    protected void initWorker()
    {
        ContentReferenceHandler sourceHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_SOURCE_PREFIX);
        worker.setSourceContentReferenceHandler(sourceHandler);
        
        ContentReferenceHandler targetHandler = createContentReferenceHandler(
                PROP_WORKER_CONTENT_REF_HANDLER_TARGET_PREFIX);
        worker.setTargetContentReferenceHandler(targetHandler);
        
        worker.initialize();
    }

}
