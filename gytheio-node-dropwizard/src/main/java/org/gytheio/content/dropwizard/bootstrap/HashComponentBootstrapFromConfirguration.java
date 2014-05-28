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

import io.dropwizard.setup.Environment;

import org.gytheio.content.dropwizard.configuration.NodeConfiguration;
import org.gytheio.content.dropwizard.health.HashHealthCheck;
import org.gytheio.content.hash.AbstractContentHashWorker;
import org.gytheio.content.hash.BaseContentHashComponent;
import org.gytheio.content.hash.ContentHashWorker;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;

import com.codahale.metrics.health.HealthCheck;

/**
 * Bootraps a hash component
 *
 * @author Ray Gauss II
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
            throw new GytheioRuntimeException(
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
