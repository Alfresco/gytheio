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
package org.gytheio.content.hash;

import org.gytheio.content.ContentReference;

/**
 * Defines the methods responsible for doing the work of hash computation of a content reference
 * 
 * @author Ray Gauss II
 */
public interface ContentHashWorker
{
    public static final String HASH_ALGORITHM_MD5 = "MD5";
    public static final String HASH_ALGORITHM_SHA_256 = "SHA-256";
    public static final String HASH_ALGORITHM_SHA_512 = "SHA-512";
    
    /**
     * Generates a hash value for the given content reference using the given algorithm
     * 
     * @param source
     * @param hashAlgorithm
     * @return the hex encoded hash value
     * @throws Exception
     */
    public String generateHash(
            ContentReference source, 
            String hashAlgorithm) throws Exception;
    
}
