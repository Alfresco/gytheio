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
import java.util.Map;

import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorker;

/**
 * Defines the methods responsible for doing the work of hash computation of content references
 * 
 * @author Ray Gauss II
 */
public interface ContentHashWorker extends ContentWorker
{
    public static final String HASH_ALGORITHM_MD5 = "MD5";
    public static final String HASH_ALGORITHM_SHA_256 = "SHA-256";
    public static final String HASH_ALGORITHM_SHA_512 = "SHA-512";
    
    /**
     * Generates a hash value for the given content reference using the given algorithm
     * 
     * @param sources
     * @param hashAlgorithm
     * @return the map of hex encoded hash values
     * @throws Exception
     */
    public Map<ContentReference, String> generateHashes(
            List<ContentReference> sources, 
            String hashAlgorithm) throws ContentIOException, InterruptedException, ContentHashException;
    
    /**
     * Determines whether or not the given hash algorithm is supported
     * by the implementation.
     * 
     * @param hashAlgorithm
     * @return true if the algorithm is supported
     */
    public boolean isAlgorithmSupported(String hashAlgorithm);
    
}
