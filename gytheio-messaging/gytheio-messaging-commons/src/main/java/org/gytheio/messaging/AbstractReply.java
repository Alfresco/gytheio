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
package org.gytheio.messaging;

/**
 * Base implementation of a reply.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractReply implements Reply
{
    private String requestId;
    
    public AbstractReply() {
        super();
    }
    
    public AbstractReply(Request<?> request)
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

}
