package org.gytheio.content.node;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.transform.AbstractContentTransformerWorker;
import org.gytheio.content.transform.BaseContentTransformerComponent;

public class TransformerComponentBootstrapFromProperties<W extends AbstractContentTransformerWorker> extends 
        AbstractComponentBootstrapFromProperties<W>
{
    protected static final String PROP_WORKER_DIR_TARGET = "gytheio.worker.dir.target";
    
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
        worker.setSourceContentReferenceHandler(
                createFileContentReferenceHandler(PROP_WORKER_DIR_SOURCE));
        worker.setTargetContentReferenceHandler(
                createFileContentReferenceHandler(PROP_WORKER_DIR_TARGET));
        worker.initialize();
    }

}
