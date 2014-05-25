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
package org.gytheio.health.camel;

import org.gytheio.health.Heart;
import org.gytheio.health.Heartbeat;
import org.gytheio.messaging.MessageProducer;

/**
 * Heart implementation which uses a Gytheio {@link MessageProducer} to
 * send a {@link Heartbeat} message.
 * 
 * @author rgauss
 */
public class HeartImpl implements Heart
{
    private String componentId;
    private String instanceId;
    private MessageProducer messageProducer;
    
    /**
     * Sets the component ID to be used for {@link Heartbeat} messages.
     * 
     * @param componentId
     */
    public void setComponentId(String componentId)
    {
        this.componentId = componentId;
    }

    /**
     * Sets the component instance ID to be used for {@link Heartbeat} messages.
     * 
     * @param instanceId
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    /**
     * Sets the message producer used to send {@link Heartbeat} messages.
     * 
     * @param messageProducer
     */
    public void setMessageProducer(MessageProducer messageProducer)
    {
        this.messageProducer = messageProducer;
    }

    @Override
    public void beat()
    {
        Heartbeat heartbeat = new Heartbeat(componentId, instanceId);
        messageProducer.send(heartbeat);
    }
    
}
