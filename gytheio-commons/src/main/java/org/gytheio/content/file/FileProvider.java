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
package org.gytheio.content.file;

import java.io.File;

/**
 * Defines methods to create files.  Implementations might include leverage Java's temporary
 * file components, explicit user-defined directories, etc.
 *
 * @author Ray Gauss II
 */
public interface FileProvider
{
    
    /**
     * Create a file with the given prefix and suffix.
     * 
     * @param prefix
     * @param suffix
     * @return the newly created File object
     */
    public File createFile(String prefix, String suffix);
    
    /**
     * Determines whether or not the file provider is available.
     * <p>
     * Some implementations might check permissions via this method.
     * 
     * @return whether or not the file provider is available as configured
     */
    public boolean isAvailable();

}
