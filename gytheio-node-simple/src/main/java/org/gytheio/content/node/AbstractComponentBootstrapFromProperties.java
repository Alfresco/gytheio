package org.gytheio.content.node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorker;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.error.AlfrescoRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.amqp.AmqpNodeBootstrapUtils;

public abstract class AbstractComponentBootstrapFromProperties<W extends ContentWorker>
{
    private static final Log logger = LogFactory.getLog(AbstractComponentBootstrapFromProperties.class);

    public static final String PROP_WORKER_DIR_SOURCE = "gytheio.worker.dir.source";
    
    protected Properties properties;
    protected W worker;
    
    public AbstractComponentBootstrapFromProperties(Properties properties, W worker)
    {
        this.properties = properties;
        this.worker = worker;
    }
    
    /**
     * Constructs a {@link FileContentReferenceHandlerImpl} using the path
     * specified in the properties file under the given propertiesKey.
     * 
     * @param propertiesKey
     * @return the ContentReferenceHandler
     */
    protected ContentReferenceHandler createFileContentReferenceHandler(String propertiesKey)
    {
        String directoryPath = properties.getProperty(propertiesKey);
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(directoryPath);
        FileContentReferenceHandlerImpl fileContentReferenceHandler = 
                new FileContentReferenceHandlerImpl();
        fileContentReferenceHandler.setFileProvider(fileProvider);
        return fileContentReferenceHandler;
    }
    
    /**
     * Constructs a {@link FileContentReferenceHandlerImpl} using a {@link TempFileProvider}.
     * 
     * @return the ContentReferenceHandler
     */
    protected ContentReferenceHandler createTempFileContentReferenceHandler()
    {
        TempFileProvider fileProvider = new TempFileProvider();
        FileContentReferenceHandlerImpl fileContentReferenceHandler = 
                new FileContentReferenceHandlerImpl();
        fileContentReferenceHandler.setFileProvider(fileProvider);
        return fileContentReferenceHandler;
    }

    protected abstract AbstractComponent<W> createComponent();
    
    protected abstract void initWorker();
    
    protected void run()
    {
        initWorker();
        
        AbstractComponent<W> component = createComponent();
        component.setWorker(worker);
        
        AmqpDirectEndpoint endpoint = 
                AmqpNodeBootstrapUtils.createEndpoint(component, properties);
        if (endpoint == null)
        {
            throw new AlfrescoRuntimeException("Could not create AMQP endpoint");
        }
        
        component.setMessageProducer(endpoint);
        
        logger.debug("Initialized component " + component.toString());
        
        endpoint.startListener();
    }


}
