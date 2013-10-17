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
package org.gytheio.content;

/**
 * URI implementation of the content reference interface.
 * 
 * @author Ray Gauss II
 */
public class ContentReferenceUriImpl implements ContentReference
{

    private String uri;
    private String mediaType;
    
    public ContentReferenceUriImpl()
    {
    }
    
    public ContentReferenceUriImpl(String uri, String mediaType)
    {
        super();
        this.uri = uri;
        this.mediaType = mediaType;
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
    
    public String getMediaType()
    {
        return mediaType;
    }
    
    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

}
