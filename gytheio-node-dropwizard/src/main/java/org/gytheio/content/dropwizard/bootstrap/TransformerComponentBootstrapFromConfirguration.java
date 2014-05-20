package org.gytheio.content.dropwizard.bootstrap;

import io.dropwizard.setup.Environment;

import org.gytheio.content.dropwizard.configuration.NodeConfiguration;
import org.gytheio.content.dropwizard.health.TransformerHealthCheck;
import org.gytheio.content.transform.AbstractContentTransformerWorker;
import org.gytheio.content.transform.BaseContentTransformerComponent;
import org.gytheio.content.transform.ContentTransformerWorker;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;

import com.codahale.metrics.health.HealthCheck;

/**
 * Bootstraps a transformer component
 *
 */
public class TransformerComponentBootstrapFromConfirguration
        extends AbstractComponentBootstrapFromConfiguration<BaseContentTransformerComponent, ContentTransformerWorker>
{
    public TransformerComponentBootstrapFromConfirguration(
            NodeConfiguration nodeConfig, Environment environment, ContentTransformerWorker worker)
    {
        super(nodeConfig, environment, worker);
    }

    protected static final String PROP_WORKER_DIR_TARGET = "gytheio.worker.dir.target";
    
    @Override
    protected BaseContentTransformerComponent createComponent()
    {
        return new BaseContentTransformerComponent();
    }
    
    protected void initWorker()
    {
        if (!(worker instanceof AbstractContentTransformerWorker))
        {
            throw new GytheioRuntimeException(
                    "Only " + AbstractContentTransformerWorker.class.getSimpleName() + " supported");
        }
        ((AbstractContentTransformerWorker) worker).setSourceContentReferenceHandler(
                createFileContentReferenceHandler(nodeConfig.getSourceDirectory()));
        ((AbstractContentTransformerWorker) worker).setTargetContentReferenceHandler(
                createFileContentReferenceHandler(nodeConfig.getTargetDirectory()));
        ((AbstractContentTransformerWorker) worker).initialize();
    }

    @Override
    public HealthCheck createHealthCheck(BaseContentTransformerComponent component, AmqpDirectEndpoint endpoint)
    {
        return new TransformerHealthCheck(component, endpoint);
    }

}
