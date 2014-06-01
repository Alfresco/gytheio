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
package org.gytheio.content.dropwizard.bootstrap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.dropwizard.setup.Environment;

import org.gytheio.content.AbstractComponent;
import org.gytheio.content.ContentWorker;
import org.gytheio.content.dropwizard.configuration.BrokerConfiguration;
import org.gytheio.content.dropwizard.configuration.ComponentConfiguration;
import org.gytheio.content.dropwizard.configuration.ContentReferenceHandlerConfiguration;
import org.gytheio.content.dropwizard.configuration.NodeConfiguration;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.handler.s3.S3ContentReferenceHandlerImpl;
import org.gytheio.content.handler.webdav.WebDavContentReferenceHandlerImpl;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;
import org.gytheio.messaging.amqp.AmqpNodeBootstrapUtils;

import com.codahale.metrics.health.HealthCheck;

/**
 * Base bootstrap which creates a component, configures it with a worker, and creates
 * an endpoint for sending and receiving messages
 *
 * @author Ray Gauss II
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
    
    protected ContentReferenceHandler createContentReferenceHandler(ContentReferenceHandlerConfiguration config)
    {
        String handlerClassName = config.getContentReferenceHandlerClass();
        if (handlerClassName.equals(FileContentReferenceHandlerImpl.class.getCanonicalName()))
        {
            return createFileContentReferenceHandler(config);
        }
        if (handlerClassName.equals(WebDavContentReferenceHandlerImpl.class.getCanonicalName()))
        {
            return createWebdavContentReferenceHandler(config);
        }
        if (handlerClassName.equals(S3ContentReferenceHandlerImpl.class.getCanonicalName()))
        {
            return createS3ContentReferenceHandler(config);
        }
        throw new GytheioRuntimeException("Unknown content reference handler or config missing");
    }
    
    /**
     * Creates a new file content reference handler from the given config.
     * 
     * @param config
     * @return the new file content reference handler
     */
    protected ContentReferenceHandler createFileContentReferenceHandler(
            ContentReferenceHandlerConfiguration config)
    {
        FileProviderImpl fileProvider = new FileProviderImpl();
        fileProvider.setDirectoryPath(config.getDirectoryPath());
        FileContentReferenceHandlerImpl contentReferenceHandler = 
                new FileContentReferenceHandlerImpl();
        contentReferenceHandler.setFileProvider(fileProvider);
        return contentReferenceHandler;
    }
    
    /**
     * Creates a new WebDAV content reference handler from the given config.
     * 
     * @param config
     * @return the new WebDAV content reference handler
     */
    protected ContentReferenceHandler createWebdavContentReferenceHandler(
            ContentReferenceHandlerConfiguration config)
    {
        WebDavContentReferenceHandlerImpl contentReferenceHandler = 
                new WebDavContentReferenceHandlerImpl();
        contentReferenceHandler.setRemoteBaseUrl(config.getRemoteBaseUrl());
        contentReferenceHandler.setUsername(config.getUsername());
        contentReferenceHandler.setPassword(config.getPassword());
        contentReferenceHandler.init();
        return contentReferenceHandler;
    }
    
    /**
     * Creates a new S3 content reference handler from the given config.
     * 
     * @param config
     * @return the new S3 content reference handler
     */
    protected ContentReferenceHandler createS3ContentReferenceHandler(
            ContentReferenceHandlerConfiguration config)
    {
        S3ContentReferenceHandlerImpl contentReferenceHandler = 
                new S3ContentReferenceHandlerImpl();
        contentReferenceHandler.setS3AccessKey(config.getS3AccessKey());
        contentReferenceHandler.setS3SecretKey(config.getS3SecretKey());
        contentReferenceHandler.setS3BucketName(config.getS3BucketName());
        contentReferenceHandler.setS3BucketRegion(config.getS3BucketRegion());
        contentReferenceHandler.init();
        return contentReferenceHandler;
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
