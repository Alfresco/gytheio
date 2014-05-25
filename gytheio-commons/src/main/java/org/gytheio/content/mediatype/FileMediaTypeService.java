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
package org.gytheio.content.mediatype;

import java.io.File;

public interface FileMediaTypeService
{
    
    /**
     * Get the extension for the specified internet media type  
     * 
     * @param mediaType a valid media type
     * @return Returns the default extension for the media type
     */
    public String getExtension(String mediaType);

    /**
     * Get the internet media type for the specified extension
     * 
     * @param extension a valid file extension
     * @return Returns a valid media type if found, or null if does not exist
     */
    public String getMediaType(String extension);

    /**
     * Get the internet media type for the specified file using only its
     * file name, no inspection
     * 
     * @param file
     * @return Returns a valid media type if found, or null if does not exist
     */
    public String getMediaTypeByName(File file);

}
