/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Gytheio
 *
 * Gytheio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gytheio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Gytheio. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorker;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.handler.webdav.WebDavContentReferenceHandlerImpl;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.amqp.AmqpNodeBootstrapUtils;

/**
 * Base bootstrap which creates a component, configures it with a worker, and creates
 * an endpoint for sending and receiving messages
 * 
 * @author Ray Gauss II
 *
 * @param <W>
 */
public abstract class AbstractComponentBootstrapFromProperties<W extends ContentWorker>
{
    private static final Log logger = LogFactory.getLog(AbstractComponentBootstrapFromProperties.class);

    public static final String PROP_WORKER_CONTENT_REF_HANDLER_SOURCE_PREFIX = 
            "gytheio.worker.contentrefhandler.source";
    public static final String PROP_WORKER_CONTENT_REF_HANDLER_CLASS_SUFFIX = ".class";
    public static final String PROP_WORKER_CONTENT_REF_HANDLER_FILE_DIR_SUFFIX = ".file.dir";
    public static final String PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_URL_SUFFIX = ".webdav.url";
    public static final String PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_USERNAME_SUFFIX = ".webdav.username";
    public static final String PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_PASSWORD_SUFFIX = ".webdav.password";
    
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
     * @param handler the content reference handler
     * @param dirPropertiesKey the properties key for getting the directory value
     * @return the ContentReferenceHandler
     */
    protected void setFileContentReferenceHandlerProvider(
            FileContentReferenceHandlerImpl handler, String dirPropertiesKey)
    {
        String directoryPath = properties.getProperty(dirPropertiesKey);
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(directoryPath);
                new FileContentReferenceHandlerImpl();
        handler.setFileProvider(fileProvider);
    }
    
    protected void setWebDavContentReferenceHandlerCredentials(
            WebDavContentReferenceHandlerImpl handler,
            String urlPropertiesKey,
            String usernamePropertiesKey,
            String passwordPropertiesKey)
    {
        String url = properties.getProperty(urlPropertiesKey);
        String username = properties.getProperty(usernamePropertiesKey);
        String password = properties.getProperty(passwordPropertiesKey);
        handler.setRemoteBaseUrl(url);
        handler.setUsername(username);
        handler.setPassword(password);
    }
    
    protected ContentReferenceHandler createContentReferenceHandler(String propertiesKeyPrefix)
    {
        ContentReferenceHandler handler = 
                (ContentReferenceHandler) SimpleAmqpNodeBootstrap.createObjectFromClassInProperties(
                        properties, propertiesKeyPrefix + PROP_WORKER_CONTENT_REF_HANDLER_CLASS_SUFFIX);
        if (handler instanceof FileContentReferenceHandlerImpl)
        {
            setFileContentReferenceHandlerProvider(
                    (FileContentReferenceHandlerImpl) handler, 
                    propertiesKeyPrefix + PROP_WORKER_CONTENT_REF_HANDLER_FILE_DIR_SUFFIX);
        }
        if (handler instanceof WebDavContentReferenceHandlerImpl)
        {
            setWebDavContentReferenceHandlerCredentials(
                    (WebDavContentReferenceHandlerImpl) handler, 
                    propertiesKeyPrefix + PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_URL_SUFFIX,
                    propertiesKeyPrefix + PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_USERNAME_SUFFIX,
                    propertiesKeyPrefix + PROP_WORKER_CONTENT_REF_HANDLER_WEBDAV_PASSWORD_SUFFIX);
            ((WebDavContentReferenceHandlerImpl) handler).init();
        }
        
        return handler;
    }
    
    /**
     * Constructs a {@link FileContentReferenceHandlerImpl} using the path
     * specified in the properties file under the given propertiesKey.
     * 
     * @param propertiesKey
     * @return the ContentReferenceHandler
     */
    protected ContentReferenceHandler createWebDavContentReferenceHandler(String propertiesKey)
    {
        String directoryPath = properties.getProperty(propertiesKey);
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(directoryPath);
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
            throw new GytheioRuntimeException("Could not create AMQP endpoint");
        }
        
        component.setMessageProducer(endpoint);
        
        logger.debug("Initialized component " + component.toString());
        
        endpoint.startListener();
    }


}
