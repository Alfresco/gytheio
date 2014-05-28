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

import java.util.UUID;

/**
 * Represents a generic messaging request
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractRequest
{
    private final String requestId;
    private String replyQueueName;
    
    public AbstractRequest()
    {
        super();
        this.requestId = UUID.randomUUID().toString();
    }
    
    /**
     * Gets the generated UUID for the transformation request
     * 
     * @return the request ID
     */
    public String getRequestId()
    {
        return requestId;
    }

    /**
     * Gets the optional overriding reply to queue replies should be sent to
     * 
     * @return the optional override reply to queue
     */
    public String getReplyTo()
    {
        return replyQueueName;
    }

    /**
     * Sets the optional overriding reply to queue replies should be sent to
     * 
     * @param replyQueueName
     */
    public void setReplyTo(String replyQueueName)
    {
        this.replyQueueName = replyQueueName;
    }

}
