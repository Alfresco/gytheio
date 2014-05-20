package org.gytheio.content.dropwizard.bootstrap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.dropwizard.setup.Environment;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorker;
import org.gytheio.content.dropwizard.configuration.BrokerConfiguration;
import org.gytheio.content.dropwizard.configuration.ComponentConfiguration;
import org.gytheio.content.dropwizard.configuration.NodeConfiguration;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.amqp.AmqpNodeBootstrapUtils;

import com.codahale.metrics.health.HealthCheck;

/**
 * Base bootstrap which creates a component, configures it with a worker, and creates
 * an endpoint for sending and receiving messages
 *
 * @param <C> the component type
 * @param <W> the worker type
 */
public abstract class 
        AbstractComponentBootstrapFromConfiguration<C extends AbstractComponent<W>, W extends ContentWorker>
{
    private static final Log logger = LogFactory.getLog(AbstractComponentBootstrapFromConfiguration.class);

    public static final String PROP_WORKER_DIR_SOURCE = "gytheio.worker.dir.source";
    
    protected NodeConfiguration nodeConfig;
    protected Environment environment;
    protected W worker;
    
    protected C component;
    protected AmqpDirectEndpoint endpoint;
    protected HealthCheck healthCheck;
    
    public AbstractComponentBootstrapFromConfiguration(
            NodeConfiguration nodeConfig, Environment environment, W worker)
    {
        this.nodeConfig = nodeConfig;
        this.environment = environment;
        this.worker = worker;
    }
    
    /**
     * Creates a new file content reference handler from the given directory path.
     * 
     * @param directoryPath
     * @return the new file content reference handler
     */
    protected ContentReferenceHandler createFileContentReferenceHandler(
            String directoryPath)
    {
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(directoryPath);
        FileContentReferenceHandlerImpl fileContentReferenceHandler = 
                new FileContentReferenceHandlerImpl();
        fileContentReferenceHandler.setFileProvider(fileProvider);
        return fileContentReferenceHandler;
    }
    
    protected abstract C createComponent();
    
    protected abstract void initWorker();
    
    
    /**
     * Initializes a new component with the given transformer worker with elements from the configuration,
     * creates an {@link AmqpDirectEndpoint}, and initializes it.
     * 
     * @param configuration
     * @param environment
     * @param worker
     * @param componentConfig
     * @return the initialized AMQP endpoint
     */
    public void init(
            NodeConfiguration nodeConfig, Environment environment,
            ComponentConfiguration componentConfig)
    {
        initWorker();
        
        component = createComponent();
        component.setWorker(worker);
        
        BrokerConfiguration brokerConfig = nodeConfig.getMessagingConfig().getBroker();
        
        endpoint = 
                AmqpNodeBootstrapUtils.createEndpoint(component, 
                        brokerConfig.getUrl(),
                        brokerConfig.getUsername(),
                        brokerConfig.getPassword(),
                        componentConfig.getRequestQueue(),
                        componentConfig.getReplyQueue());
        if (endpoint == null)
        {
            throw new GytheioRuntimeException("Could not create AMQP endpoint");
        }
        
        component.setMessageProducer(endpoint);
        component.init();
        
        healthCheck = createHealthCheck(component, endpoint);
        
        logger.debug("Initialized component " + component.toString());
    }
    
    protected abstract HealthCheck createHealthCheck(
            C component, AmqpDirectEndpoint endpoint);

    public C getComponent()
    {
        return component;
    }

    public AmqpDirectEndpoint getEndpoint()
    {
        return endpoint;
    }
    
    public HealthCheck getHealthCheck()
    {
        return healthCheck;
    }

}
