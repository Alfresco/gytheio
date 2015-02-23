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

import org.gytheio.messaging.MessageConsumer;

/**
 * Defines a component which is a consumer of request for content action messages,
 * delegates that work to a worker, and sends a reply with the results.
 * 
 * @author Ray Gauss II
 */
public interface Component extends MessageConsumer
{
    /**
     * Gets the name of the component, useful for health checks.
     * 
     * @return the component name
     */
    public String getName();
    
    /**
     * Determines whether or not the worker is available.
     * 
     * @return true if the worker is available
     * @see {@link ContentWorker#isAvailable()}
     */
    public boolean isWorkerAvailable();
    
    /**
     * Gets a string returning name and version information 
     * 
     * @return the version string
     * @see {@link ContentWorker#getVersionString()}
     */
    public String getWorkerVersionString();
    
    
    /**
     * Gets a string returning detailed version information such as JVM 
     * or command line application's version output
     * 
     * @return the version string
     * @see {@link ContentWorker#getVersionDetailsString()}
     */
    public String getWorkerVersionDetailsString();

}
