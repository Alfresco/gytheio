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

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class MediaTypeServiceTest
{
    
    private FileMediaTypeService mediaTypeService;
    
    @Before
    public void setUp()
    {
        mediaTypeService = new FileMediaTypeServiceImpl(null);
        ((FileMediaTypeServiceImpl) mediaTypeService).init();
    }

    @Test
    public void testGetMediaTypeFromExtension()
    {
        assertEquals("text/plain", mediaTypeService.getMediaType("txt"));
        assertEquals("application/pdf", mediaTypeService.getMediaType("pdf"));
    }
    
    @Test
    public void testGetExtensionFromMediaType()
    {
        assertEquals("txt", mediaTypeService.getExtension("text/plain"));
        assertEquals("pdf", mediaTypeService.getExtension("application/pdf"));
    }
    
    @Test
    public void testFileMediaType()
    {
        assertEquals("text/plain", FileMediaType.TEXT_PLAIN.getMediaType());
        assertEquals("application/pdf", FileMediaType.PDF.getMediaType());
    }
    
}
