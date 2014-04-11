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
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.FileProviderImpl;

/**
 * Java {@link File} content reference handler implementation.
 * <p>
 * Content reference creation is delegated to {@link TempFileProvider}.
 * 
 * @author Ray Gauss II
 */
public class FileContentReferenceHandlerImpl implements FileContentReferenceHandler
{
    private static final Log logger = LogFactory.getLog(FileContentReferenceHandlerImpl.class);
    
    public static final String URI_SCHEME_FILE = "file:/";
    
    private static final long DEFAULT_TRANSFER_CHECK_PERIOD_MS = 2000;
    
    private FileProvider fileProvider;
    private long transferCheckPeriodMs = DEFAULT_TRANSFER_CHECK_PERIOD_MS;

    public void setFileProvider(FileProvider fileProvider)
    {
        this.fileProvider = fileProvider;
    }

    public void setTransferCheckPeriodMs(long transferCheckPeriodMs)
    {
        this.transferCheckPeriodMs = transferCheckPeriodMs;
    }
    
    public void setFileProviderDirectoryPath(String directoryPath)
    {
        if (fileProvider != null)
        {
            throw new IllegalStateException("FileProvider has already been set");
        }
        fileProvider = new FileProviderImpl();
        ((FileProviderImpl) fileProvider).setDirectoryPath(directoryPath);
    }

    @Override
    public boolean isContentReferenceSupported(ContentReference contentReference)
    {
        if (contentReference == null)
        {
            return false;
        }
        return contentReference.getUri().startsWith(URI_SCHEME_FILE);
    }
    
    @Override
    public ContentReference createContentReference(String fileName, String mediaType) throws ContentIOException
    {
        String suffix = fileName.substring(StringUtils.lastIndexOf(fileName, "."), fileName.length());
        String prefix = fileName.substring(0, StringUtils.lastIndexOf(fileName, "."));
        
        File tempFile = fileProvider.createFile(prefix, suffix);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("Created file content reference for " +
            		"mediaType=" + mediaType + ": " + tempFile.getAbsolutePath());
        }
        
        return new ContentReference(tempFile.toURI().toString(), mediaType);
    }
    
    @Override
    public File getFile(ContentReference contentReference, boolean waitForTransfer) throws ContentIOException, InterruptedException
    {
        if (!isContentReferenceSupported(contentReference))
        {
            throw new ContentIOException("ContentReference not supported");
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Getting file for content reference: " + 
                    contentReference.getUri());
        }
        File file = null;
        try
        {
            file = new File(new URI(contentReference.getUri()));
        }
        catch (URISyntaxException e)
        {
            throw new ContentIOException("Syntax error getting file reference", e);
        }
        if (!waitForTransfer)
        {
            return file;
        }
        if (contentReference.getSize() == null)
        {
            logger.debug("Expected file size unknown, skipping size check");
            return file;
        }
        long expectedSize = contentReference.getSize();
        long actualSize = file.length();
        while (actualSize < expectedSize)
        {
            logger.trace("Checked file, expectedSize=" + expectedSize + " actualSize=" + actualSize + 
                    ", waiting " + transferCheckPeriodMs + "ms");
            Thread.sleep(transferCheckPeriodMs);
            actualSize = file.length();
        }
        logger.debug("File expectedSize=" + expectedSize + " actualSize=" + actualSize + ", ending check");
        return file;
    }
    
    @Override
    public InputStream getInputStream(ContentReference contentReference, boolean waitForAvailability) throws ContentIOException
    {
        File file = null;
        try
        {
            file = getFile(contentReference, waitForAvailability);
        }
        catch (InterruptedException e1)
        {
            // We were asked to stop
        }
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
    public long putInputStream(InputStream sourceInputStream, ContentReference targetContentReference)
            throws ContentIOException
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            File targetFile = getFile(targetContentReference, false);
            fileOutputStream = new FileOutputStream(targetFile);
            long sizeCopied = IOUtils.copyLarge(sourceInputStream, fileOutputStream);
            fileOutputStream.close();
            return sizeCopied;
        }
        catch (IOException e)
        {
            throw new ContentIOException("Error copying input stream", e);
        }
        catch (InterruptedException e)
        {
            return 0;
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                    sourceInputStream.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    @Override
    public void delete(ContentReference contentReference) throws ContentIOException
    {
        try
        {
            File file = getFile(contentReference, false);
            boolean deleted = file.delete();
            if (!deleted)
            {
                throw new ContentIOException("File could not be deleted");
            }
        }
        catch (InterruptedException e)
        {
            // Should not encounter this with waitForAvailability = false
        }
    }

    @Override
    public boolean isAvailable()
    {
        return fileProvider != null && fileProvider.isAvailable();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("fileProvider: " + fileProvider.toString());
        builder.append(", ");
        builder.append("isAvailable: " + isAvailable());
        builder.append("]");
        return builder.toString();
    }

}
