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
package org.gytheio.content.hash;

import java.util.Map;

import org.gytheio.content.ContentReference;
import org.gytheio.messaging.Reply;

/**
 * Represents a reply from a content hasher on the status of a hash request.
 * 
 * @author Ray Gauss II
 */
public class HashReply implements Reply
{
    
    private String requestId;
    private Map<ContentReference, String> hexValues;
    
    public HashReply() {
        super();
    }
    
    public HashReply(HashRequest request)
    {
        super();
        this.requestId = request.getRequestId();
    }

    /**
     * Gets the UUID for the original hash request
     * 
     * @return the hash request ID
     */
    public String getRequestId()
    {
        return requestId;
    }

    /**
     * Sets the UUID for the original hash request
     * 
     * @param requestId
     */
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    /**
     * Gets the map of content hash values
     * 
     * @return the map of hash values
     */
    public Map<ContentReference, String> getHexEncodedValues()
    {
        return hexValues;
    }

    /**
     * Sets the map of content hash values
     * 
     * @param the map of hashValues
     */
    public void setHexEncodedValues(Map<ContentReference, String> hexValues)
    {
        this.hexValues = hexValues;
    }

}
