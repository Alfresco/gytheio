/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.content.hash;

import java.io.FileInputStream;
import java.io.InputStream;

import org.alfresco.content.ContentReference;
import org.alfresco.content.handler.ContentReferenceHandler;

/**
 * Abstract hash node worker which uses a content reference handler to convert the 
 * content reference into a usable input stream for the actual implementation.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentHashNodeWorker implements ContentHashNodeWorker
{
    
    protected ContentReferenceHandler contentReferenceHandler;
    
    public void setContentReferenceHandler(ContentReferenceHandler contentReferenceFileHandler)
    {
        this.contentReferenceHandler = contentReferenceFileHandler;
    }

    public String generateHash(
            ContentReference source, 
            String hashAlgorithm) throws Exception
    {
        return generateHashInternal(
                new FileInputStream(contentReferenceHandler.getFile(source)),
                hashAlgorithm);
    }
    
    /**
     * Computes the hash value for the given input stream using the given algorithm
     * 
     * @param sourceFile
     * @param hashAlgorithm
     * @return the hex encoded hash value
     * @throws Exception
     */
    public abstract String generateHashInternal(
            InputStream sourceFile,
            String hashAlgorithm) throws Exception;

}
