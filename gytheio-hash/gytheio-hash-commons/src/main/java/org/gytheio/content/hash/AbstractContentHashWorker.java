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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gytheio.content.AbstractContentWorker;
import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorkResult;

/**
 * Abstract hash node worker which uses a content reference handler to convert the 
 * content reference into a usable input stream for the actual implementation.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentHashWorker extends AbstractContentWorker implements ContentHashWorker
{

    @Override
    public void initialize()
    {
        super.initialize();
        if (sourceContentReferenceHandler != null && sourceContentReferenceHandler.isAvailable())
        {
            setIsAvailable(true);
        }
    }
    
    @Override
    public List<ContentWorkResult> generateHashes(
            List<ContentReference> sources, 
            String hashAlgorithm) throws ContentIOException, InterruptedException, ContentHashException
    {
        List<ContentWorkResult> results = new ArrayList<ContentWorkResult>();
        if (sources == null || sources.size() == 0)
        {
            return results;
        }
        for (ContentReference source : sources)
        {
            String value = generateHashInternal(
                        sourceContentReferenceHandler.getInputStream(source, true),
                        hashAlgorithm);
            Map<String, Object> resultDetails = new HashMap<String, Object>();
            resultDetails.put(ContentHashWorker.RESULT_DETAIL_HEX_ENCODED_VALUE, value);
            ContentWorkResult result = new ContentWorkResult(source, resultDetails);
            results.add(result);
        }
        return results;
    }
    
    /**
     * Computes the hash value for the given input stream using the given algorithm
     * 
     * @param sourceFile
     * @param hashAlgorithm
     * @return the hex encoded hash value
     * @throws ContentIOException
     * @throws InterruptedException
     * @throws ContentHashException 
     */
    public abstract String generateHashInternal(
            InputStream sourceFile,
            String hashAlgorithm) throws ContentIOException, InterruptedException, ContentHashException;
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("contentReferenceHandler: " + sourceContentReferenceHandler.toString());
        builder.append("]");
        return builder.toString();
    }

}
