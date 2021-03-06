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

/**
 * Defines a low-level worker that performs some action on content, i.e. performing
 * a transformation, extracting metadata, computing a hash, etc.
 * 
 * @author Ray Gauss II
 */
public interface ContentWorker
{
    
    /**
     * Gets whether or not the dependencies of the worker have been
     * properly configured for its normal operation, i.e. content reference handlers,
     * command line applications, etc.
     * 
     * @return true if the worker is available
     */
    public boolean isAvailable();
    
    /**
     * Gets a string returning name and version information.
     * 
     * @return the version string
     */
    public String getVersionString();
    
    
    /**
     * Gets a string returning detailed version information such as JVM 
     * or command line application's version output
     * 
     * @return the version string
     */
    public String getVersionDetailsString();

}
