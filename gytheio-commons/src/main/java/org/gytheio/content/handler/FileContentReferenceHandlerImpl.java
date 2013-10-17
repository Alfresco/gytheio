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
package org.gytheio.content.handler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.alfresco.service.cmr.repository.ContentIOException;
import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentReferenceUriImpl;
import org.gytheio.util.TempFileProvider;

/**
 * Java {@link File} content reference handler implementation.
 * <p>
 * Content reference creation is delegated to {@link TempFileProvider}.
 * 
 * @author Ray Gauss II
 */
public class FileContentReferenceHandlerImpl implements ContentReferenceHandler
{
    private static final Log logger = LogFactory.getLog(FileContentReferenceHandlerImpl.class);
    
    public static final String URI_SCHEME_FILE = "file:/";

    @Override
    public boolean isContentReferenceSupported(ContentReference contentReference)
    {
        if (contentReference == null)
        {
            return false;
        }
        if (!(contentReference instanceof ContentReferenceUriImpl))
        {
            return false;
        }
        return ((ContentReferenceUriImpl) contentReference).getUri().startsWith(URI_SCHEME_FILE);
    }
    
    public ContentReference createContentReference(String fileName, String mediaType) throws ContentIOException
    {
        String suffix = fileName.substring(StringUtils.lastIndexOf(fileName, "."), fileName.length());
        String prefix = fileName.substring(0, StringUtils.lastIndexOf(fileName, "."));
        
        File tempFile = TempFileProvider.createTempFile(prefix, suffix);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("Created file content reference for " +
            		"mediaType=" + mediaType + ": " + tempFile.getAbsolutePath());
        }
        
        return new ContentReferenceUriImpl(tempFile.toURI().toString(), mediaType);
    }

    @Override
    public File getFile(ContentReference contentReference) throws ContentIOException
    {
        if (!isContentReferenceSupported(contentReference))
        {
            throw new ContentIOException("ContentReference not supported");
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Getting file for content reference: " + 
                    ((ContentReferenceUriImpl) contentReference).getUri());
        }
        try
        {
            return new File(new URI(((ContentReferenceUriImpl) contentReference).getUri()));
        }
        catch (URISyntaxException e)
        {
            throw new ContentIOException("Syntax error getting file reference", e);
        }
    }

    @Override
    public InputStream getInputStream(ContentReference contentReference) throws ContentIOException
    {
        File file = getFile(contentReference);
        if (file == null)
        {
            throw new ContentIOException("File not found");
        }
        try
        {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            throw new ContentIOException("File not found", e);
        }
    }

    @Override
    public void putFile(File sourceFile, ContentReference targetContentReference) throws ContentIOException
    {
        File targetFile = getFile(targetContentReference);
        try
        {
            FileUtils.copyFile(sourceFile, targetFile);
        }
        catch (IOException e)
        {
            throw new ContentIOException("Error copying file", e);
        }
        
    }

    @Override
    public void putInputStream(InputStream sourceInputStream, ContentReference targetContentReference)
            throws ContentIOException
    {
        File targetFile = getFile(targetContentReference);
        try
        {
            IOUtils.copyLarge(sourceInputStream, new FileOutputStream(targetFile));
        }
        catch (IOException e)
        {
            throw new ContentIOException("Error copying input stream", e);
        }
    }

    @Override
    public void delete(ContentReference contentReference) throws ContentIOException
    {
        File file = getFile(contentReference);
        boolean deleted = file.delete();
        if (!deleted)
        {
            throw new ContentIOException("File could not be deleted");
        }
    }

    @Override
    public boolean isAvailable()
    {
        return true;
    }

}
