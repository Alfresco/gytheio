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

import org.alfresco.content.AbstractContentRequest;
import org.alfresco.content.ContentReference;
import org.alfresco.messaging.Request;

/**
 * Represents a request for content hash
 * 
 * @author Ray Gauss II
 */
public class HashRequest extends AbstractContentRequest implements Request<HashReply>
{
    public static final String HASH_ALGORITHM_MD5 = "MD5";
    public static final String HASH_ALGORITHM_SHA_256 = "SHA-256";
    public static final String HASH_ALGORITHM_SHA_512 = "SHA-512";
    
    private String hashAlgorithm;
    
    public HashRequest()
    {
        super();
    }
    
    public HashRequest(ContentReference sourceContentReference, String hashAlgorithm)
    {
        super();
        setSourceContentReference(sourceContentReference);
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
