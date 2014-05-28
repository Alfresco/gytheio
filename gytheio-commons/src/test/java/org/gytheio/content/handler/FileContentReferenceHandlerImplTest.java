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
package org.gytheio.content.handler;

import org.apache.commons.lang.StringUtils;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.FileProviderImpl;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Tests of the file content reference handler
 * 
 * @see {@link FileContentReferenceHandler}
 * @author Ray Gauss II
 */
public class FileContentReferenceHandlerImplTest
{
    private ContentReferenceHandler handler;
    
    @Before
    public void setUp()
    {
        FileProvider fileProvider = new FileProviderImpl(
                TempFileProvider.getTempDir().getPath());
        handler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) handler).setFileProvider(fileProvider);
    }
    
    protected void checkReference(String fileName, String mediaType)
    {
        ContentReference reference = handler.createContentReference(fileName, mediaType);
        assertEquals(mediaType, reference.getMediaType());
        
        String uri = reference.getUri();
        String createdFileName = uri.split("\\/")[uri.split("\\/").length-1];
        
        String origPrefix = fileName.substring(0, StringUtils.lastIndexOf(fileName, "."));
        String origSuffix = fileName.substring(StringUtils.lastIndexOf(fileName, "."), fileName.length());
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name prefix '" + origPrefix + "'", 
                createdFileName.contains(origPrefix));
        assertTrue("ContentReference file name '" + createdFileName + 
                "' did not contain original file name suffix '" + origPrefix + "'", 
                createdFileName.contains(origSuffix));
    }
    
    @Test
    public void testSimpleReference()
    {
        checkReference("myfile.txt", "text/plain");
    }
    
    @Test
    public void testPathReference()
    {
        checkReference("my.file.txt", "text/plain");
    }

}
