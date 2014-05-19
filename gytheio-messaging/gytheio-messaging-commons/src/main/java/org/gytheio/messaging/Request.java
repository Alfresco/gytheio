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
package org.gytheio.messaging;

/**
 * Defines a request message where a {@link Reply} is expected
 * 
 * @author Ray Gauss II
 *
 * @param <RP> the Reply type
 */
public interface Request<RP extends Reply>
{
    /**
     * Gets the correlating request ID.
     * <p>
     * Note that this is unlikely to be the same value as the
     * 'native' correlation ID used by a particular messaging transport.
     * 
     * @return the request correlation ID
     */
    public String getRequestId();
    
    /**
     * Gets the class of expected {@link Reply} messages
     * 
     * @return the reply message class
     */
    public Class<RP> getReplyClass();
    
    /**
     * Gets the optional overriding reply to queue name
     * 
     * @return the reply to queue name
     */
    public String getReplyTo();
    
    /**
     * Sets the optional overriding reply to queue name
     * 
     * @param replyTo
     */
    public void setReplyTo(String replyTo);
    
}
