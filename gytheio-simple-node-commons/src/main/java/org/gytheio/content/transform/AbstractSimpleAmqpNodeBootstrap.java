package org.gytheio.content.transform;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.error.AlfrescoRuntimeException;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.amqp.AmqpNodeBootstrapUtils;

/**
 * Base boostrap class which loads a properties file from the path
 * specified via command line argument, loads a worker class and creates an instance of it,
 * then creates an AMQP endpoint and starts a listener for it.
 *
 * @param <T> the type of worker
 */
public abstract class AbstractSimpleAmqpNodeBootstrap<T>
{
    protected static final String PROP_WORKER_CLASS = "gytheio.worker.class";
    protected static final String PROP_WORKER_DIR_SOURCE = "gytheio.worker.dir.source";

    private String propertiesFilePath;
    private Properties properties;
    private AmqpDirectEndpoint endpoint;
    
    public void run(String[] args)
    {
        if (args.length < 1)
        {
            throw new IllegalArgumentException("USAGE: propertiesFilePath");
        }
        
        this.propertiesFilePath = args[0];
        
        getProperties();
        
        getEndpoint().startListener();
    }
    
    /**
     * Loads the properties from the path specified via command line.
     * 
     * @return the properties object
     */
    protected Properties getProperties()
    {
        if (properties == null)
        {
            InputStream inputStream = null;
            try
            {
                properties = new Properties();
                inputStream = new FileInputStream(propertiesFilePath);
                properties.load(inputStream);
            }
            catch (IOException e)
            {
                throw new AlfrescoRuntimeException("Could not load required " + propertiesFilePath);
            }
            finally
            {
                try
                {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                }
                catch (IOException e)
                {
                    
                }
            }
        }
        return properties;
    }
    
    /**
     * Attempts to load the worker class specified in the properties file and
     * create a new instance of it.
     * 
     * @return the newly created worker object
     */
    @SuppressWarnings("unchecked")
    protected T createWorker()
    {
        try
        {
            String workerClassName = getProperties().getProperty(PROP_WORKER_CLASS);
            Class<T> workerClass = (Class<T>) AbstractSimpleAmqpNodeBootstrap.class.getClassLoader().loadClass(workerClassName);
            return workerClass.newInstance();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            throw new AlfrescoRuntimeException("Could not load worker class", e);
        }
    }
    
    /**
     * Creates and caches an AMQP endpoint using the properties file configuration.
     * 
     * @return the AMQP endpoint
     */
    protected AmqpDirectEndpoint getEndpoint()
    {
        if (endpoint == null)
        {
            endpoint = AmqpNodeBootstrapUtils.createEndpoint(
                    getMessageConsumer(), getProperties());
        }
        return endpoint;
    }
    
    /**
     * Gets the consumer that request message should be sent to.
     * 
     * @return the message consumer
     */
    protected abstract MessageConsumer getMessageConsumer();
    
    /**
     * Constructs a {@link FileContentReferenceHandlerImpl} using the path
     * specified in the properties file under the given propertiesKey.
     * 
     * @param propertiesKey
     * @return the ContentReferenceHandler
     */
    protected ContentReferenceHandler createFileContentReferenceHandler(String propertiesKey)
    {
        String directoryPath = getProperties().getProperty(propertiesKey);
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

}
