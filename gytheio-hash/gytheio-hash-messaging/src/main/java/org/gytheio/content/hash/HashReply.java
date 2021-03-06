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

import org.gytheio.content.AbstractContentReply;
import org.gytheio.messaging.Request;

/**
 * Represents a reply from a content hasher on the status of a hash request.
 * 
 * @author Ray Gauss II
 */
public class HashReply extends AbstractContentReply
{

    public HashReply()
    {
        super();
    }

    public HashReply(Request<?> request)
    {
        super(request);
    }
    
}
