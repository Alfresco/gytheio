package org.gytheio.content.dropwizard.bootstrap;

import io.dropwizard.setup.Environment;

import org.gytheio.content.dropwizard.configuration.NodeConfiguration;
import org.gytheio.content.dropwizard.health.HashHealthCheck;
import org.gytheio.content.hash.AbstractContentHashWorker;
import org.gytheio.content.hash.BaseContentHashComponent;
import org.gytheio.content.hash.ContentHashWorker;
import org.gytheio.error.AlfrescoRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;

import com.codahale.metrics.health.HealthCheck;

/**
 * Bootraps a hash component
 *
 */
public class HashComponentBootstrapFromConfirguration
        extends AbstractComponentBootstrapFromConfiguration<BaseContentHashComponent, ContentHashWorker>
{
    public HashComponentBootstrapFromConfirguration(
            NodeConfiguration nodeConfig, Environment environment, ContentHashWorker worker)
    {
        super(nodeConfig, environment, worker);
    }

    protected static final String PROP_WORKER_DIR_TARGET = "gytheio.worker.dir.target";
    
    @Override
    protected BaseContentHashComponent createComponent()
    {
        return new BaseContentHashComponent();
    }
    
    protected void initWorker()
    {
        if (!(worker instanceof AbstractContentHashWorker))
        {
            throw new AlfrescoRuntimeException(
                    "Only " + AbstractContentHashWorker.class.getSimpleName() + " supported");
        }
        ((AbstractContentHashWorker) worker).setSourceContentReferenceHandler(
                createFileContentReferenceHandler(nodeConfig.getSourceDirectory()));
        ((AbstractContentHashWorker) worker).initialize();
    }

    @Override
    public HealthCheck createHealthCheck(BaseContentHashComponent component, AmqpDirectEndpoint endpoint)
    {
        return new HashHealthCheck(component, endpoint);
    }

}
