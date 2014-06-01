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
 * @author Ray Gauss II
 */
public class TransformerComponentBootstrapFromConfirguration
        extends AbstractComponentBootstrapFromConfiguration<BaseContentTransformerComponent, ContentTransformerWorker>
{
    public TransformerComponentBootstrapFromConfirguration(
            NodeConfiguration nodeConfig, Environment environment, ContentTransformerWorker worker)
    {
        super(nodeConfig, environment, worker);
    }

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
                createContentReferenceHandler(nodeConfig.getContentReferenceHandlersConfig().getSource()));
        ((AbstractContentTransformerWorker) worker).setTargetContentReferenceHandler(
                createContentReferenceHandler(nodeConfig.getContentReferenceHandlersConfig().getTarget()));
        ((AbstractContentTransformerWorker) worker).initialize();
    }

    @Override
    public HealthCheck createHealthCheck(BaseContentTransformerComponent component, AmqpDirectEndpoint endpoint)
    {
        return new TransformerHealthCheck(component, endpoint);
    }

}
