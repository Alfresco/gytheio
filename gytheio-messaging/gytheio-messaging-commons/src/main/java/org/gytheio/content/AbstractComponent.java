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
package org.gytheio.content;

import org.gytheio.messaging.MessageProducer;

/**
 * Base implementation of a component with content worker and messageProducer fields.
 *
 * @author Ray Gauss II
 *
 * @param <W>
 */
public abstract class AbstractComponent<W extends ContentWorker> implements Component
{
    protected String name;
    protected W worker;
    protected MessageProducer messageProducer;
    
    public String getName()
    {
        if (name != null)
        {
            return name;
        }
        return this.getClass().getSimpleName();
    }

    /**
     * Sets the component name
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the transformer worker which does the actual work of the transformation
     * 
     * @param transformerWorker
     */
    public void setWorker(W worker)
    {
        this.worker = worker;
    }

    /**
     * Sets the message producer used to send replies
     * 
     * @param messageProducer
     */
    public void setMessageProducer(MessageProducer messageProducer)
    {
        this.messageProducer = messageProducer;
    }
    

    public void init()
    {
    }
    
    public void onReceive(Object message)
    {
        onReceiveImpl(message);
    }
    
    protected abstract void onReceiveImpl(Object message);
    
    @Override
    public boolean isWorkerAvailable()
    {
        if (worker == null)
        {
            return false;
        }
        return worker.isAvailable();
    }

    @Override
    public String getWorkerVersionString()
    {
        if (worker == null)
        {
            return null;
        }
        return worker.getVersionString();
    }

    @Override
    public String getWorkerVersionDetailsString()
    {
        if (worker == null)
        {
            return null;
        }
        return worker.getVersionDetailsString();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("worker: " + 
                ((worker == null) ? worker : worker.toString()));
        builder.append(", ");
        builder.append("messageProducer: " + 
                ((messageProducer == null) ? messageProducer : messageProducer.toString()));
        builder.append("]");
        return builder.toString();
    }
    
}
