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

import java.util.concurrent.Future;

/**
 * Defines an asynchronous request-reply message producer
 * 
 * @author Ray Gauss II
 *
 * @param <RQ>
 * @param <RP>
 */
public interface RequestReplyMessageProducer<RQ extends Request<RP>, RP extends Reply> extends MessageProducer
{
    
    /**
     * Sends the given request message to the configured queue and waits for its reply,
     * returning a {@link Future} object that will contain that reply once available.
     * 
     * @param request
     * @return an executing future object which will contain the reply once available
     * @throws MessagingException
     */
    public Future<RP> asyncRequest(RQ request) throws MessagingException;

}
