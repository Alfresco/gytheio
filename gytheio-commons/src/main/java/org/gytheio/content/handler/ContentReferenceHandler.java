/*
 * Copyright (C) 2005-2018 Alfresco Software Limited.
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
package org.gytheio.content.handler;

import java.io.File;
import java.io.InputStream;

import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;

/**
 * Defines a handler for reading and writing {@link ContentReference} objects.
 * 
 * @author Ray Gauss II
 */
public interface ContentReferenceHandler
{
    
    /**
     * Determines whether the given content reference is supported by the handler
     * 
     * @param contentReference
     * @return whether or not the content reference is supported
     */
    public boolean isContentReferenceSupported(ContentReference contentReference);
    
    /**
     * Determines whether the given content reference exists
     * 
     * @param contentReference
     * @return whether or not the content reference exists
     */
    public boolean isContentReferenceExists(ContentReference contentReference);
    
    /**
     * Creates a content reference of the given file name and media type.
     * 
     * @param fileName
     * @param mediaType
     * @return the created content reference
     * @throws ContentIOException
     */
    public ContentReference createContentReference(String fileName, String mediaType) throws ContentIOException;
    
    /**
     * Gets an input stream for the given content reference
     * 
     * @param contentReference
     * @param waitForAvailability whether or not to check and wait for availability
     * @return the content reference input stream
     * @throws ContentIOException
     * @throws InterruptedException
     */
    public InputStream getInputStream(ContentReference contentReference, boolean waitForAvailability) throws ContentIOException, InterruptedException;
    
    /**
     * Writes the given source input stream into the given target content reference.
     * <p>
     * Implementations should close both streams.
     * 
     * @param sourceInputStream
     * @param targetContentReference
     * @return the size copied
     * @throws ContentIOException
     */
    public long putInputStream(InputStream sourceInputStream, ContentReference targetContentReference) throws ContentIOException;

    /**
     * Writes the given source file into the given target content reference.
     * <p>
     * 
     * @param sourceFile
     * @param targetContentReference
     * @return the size copied
     * @throws ContentIOException
     */
    public long putFile(File sourceFile, ContentReference targetContentReference) throws ContentIOException;

    /**
     * Deletes the given content reference
     * @param contentReference
     * @throws ContentIOException
     */
    public void delete(ContentReference contentReference) throws ContentIOException;
    
    /**
     * Determines whether or not the handler is currently available, i.e. any required credentials
     * are defined and valid.
     * 
     * @return whether or not the handler is available
     */
    public boolean isAvailable();
    
}
