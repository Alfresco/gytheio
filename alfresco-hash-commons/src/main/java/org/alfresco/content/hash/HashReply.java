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

import org.alfresco.messaging.Reply;

/**
 * Represents a reply from a content hasher on the status of a hash request.
 * 
 * @author Ray Gauss II
 */
public class HashReply implements Reply
{
    
    private String requestId;
    private String hexValue;
    
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
     * Gets the value of the content hash
     * 
     * @return the hash value
     */
    public String getHexValue()
    {
        return hexValue;
    }

    /**
     * Sets the value of the content hash
     * 
     * @param value
     */
    public void setHexValue(String hexValue)
    {
        this.hexValue = hexValue;
    }

}
