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

import org.gytheio.content.handler.ContentReferenceHandler;

/**
 * A reference to content by its URI and media type (mimetype).
 * 
 * @see {@link ContentReferenceHandler}
 * 
 * @author Ray Gauss II
 */
public class ContentReference
{

    private String uri;
    private String mediaType;
    private Long size;
    
    public ContentReference()
    {
    }
    
    public ContentReference(String uri, String mediaType)
    {
        super();
        this.uri = uri;
        this.mediaType = mediaType;
    }
    
    public ContentReference(String uri, String mediaType, Long size)
    {
        super();
        this.uri = uri;
        this.mediaType = mediaType;
        this.size = size;
    }

    /**
     * Gets the URI for the content reference
     * 
     * @return the content URI
     */
    public String getUri()
    {
        return uri;
    }
    
    /**
     * Sets the URI for the content reference
     * 
     * @param uri
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }
    
    /**
     * Gets the media type (mimetype) of the content reference
     * 
     * @return media type
     */
    public String getMediaType()
    {
        return mediaType;
    }
    
    /**
     * Sets the media type (mimetype) of the content reference
     * 
     * @param mediaType
     */
    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    /**
     * Gets the size of the content binary if available
     * 
     * @return
     */
    public Long getSize()
    {
        return size;
    }

    /**
     * Sets the size of the content binary
     * 
     * @param size
     */
    public void setSize(Long size)
    {
        this.size = size;
    }

}
