/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.messaging.content.transport;

import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.gytheio.content.ContentReference;

/**
 * Defines methods for reading and writing files to a shared or remote content system.
 * 
 * @author Ray Gauss II
 */
public interface ContentTransport
{
    
    /**
     * Writes the given <code>contentReader</code> into the given remote 
     * content reference, i.e. a URL
     * 
     * @param contentReader
     * @param contentReference
     * @throws Exception
     */
    public void write(ContentReader contentReader, ContentReference contentReference) throws Exception;

    /**
     * Reads the given remote content reference, i.e. a URL, into the 
     * given <code>contentWriter</code>
     * 
     * @param contentReference
     * @param contentWriter
     * @throws Exception
     */
    public void read(ContentReference contentReference, ContentWriter contentWriter) throws Exception;
    
    /**
     * Creates a content reference on the remote system for the given media type
     * 
     * @param mediaType
     * @return the remote content reference, i.e. a URL
     * @throws Exception
     */
    public ContentReference createContentReference(String mediaType) throws Exception;
    
    /**
     * Whether or not the transport is configured and available
     * 
     * @return true if the transport is available
     */
    public boolean isAvailable();
    
}
