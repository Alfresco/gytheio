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

import java.util.List;

import org.gytheio.content.AbstractContentRequest;
import org.gytheio.content.ContentReference;
import org.gytheio.messaging.Request;

/**
 * Represents a request for content hash
 * 
 * @author Ray Gauss II
 */
public class HashRequest extends AbstractContentRequest implements Request<HashReply>
{
    private String hashAlgorithm;
    
    public HashRequest()
    {
        super();
    }
    
    public HashRequest(List<ContentReference> sourceContentReferences, String hashAlgorithm)
    {
        super();
        setSourceContentReferences(sourceContentReferences);
        this.hashAlgorithm = hashAlgorithm;
    }

    /**
     * Gets the hash algorithm to be used
     * 
     * @return the hash algorithm
     */
    public String getHashAlgorithm()
    {
        return hashAlgorithm;
    }

    /**
     * Sets hash algorithm to be used
     * 
     * @param hashAlgorithm
     */
    public void setHashAlgorithm(String hashAlgorithm)
    {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public Class<HashReply> getReplyClass()
    {
        return HashReply.class;
    }
    
}
