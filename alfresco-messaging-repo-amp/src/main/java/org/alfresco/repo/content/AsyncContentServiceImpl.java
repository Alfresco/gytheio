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
package org.alfresco.repo.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.content.transform.AsyncContentTransformer;
import org.alfresco.repo.content.transform.ContentTransformer;
import org.alfresco.service.cmr.repository.ContentIOException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NoTransformerException;
import org.alfresco.service.cmr.repository.TransformationOptions;

/**
 * Extends ContentService for asynchronous <code>transform</code> calls.
 */
public class AsyncContentServiceImpl extends ContentServiceImpl
{

    // TODO should we share and/or persist running transformations in clusters
    private Map<String, AsyncContentTransformer> runningTransformations = 
            new HashMap<String, AsyncContentTransformer>();
    
    /**
     * Async capable transform method.
     * 
     * @param reader
     * @param writer
     * @param options
     * @param executeAsyncronously
     * @return the ID of the transformation request
     * @throws Exception
     */
    public String transform(ContentReader reader, ContentWriter writer, 
            TransformationOptions options, boolean executeAsyncronously) throws NoTransformerException, ContentIOException
    {
        if (!executeAsyncronously)
        {
            transform(reader, writer, options);
            return null;
        }
        
        String sourceMimetype = reader.getMimetype();
        String targetMimetype = writer.getMimetype();
        List<ContentTransformer> activeTransformers = getActiveTransformers(sourceMimetype, -1, targetMimetype, options, true);
        if (activeTransformers == null || activeTransformers.size() == 0)
        {
            throw new RuntimeException("No asycnhronous transformer found for " +
                    "sourceMimetype=" + sourceMimetype + " targetMimetype=" + targetMimetype);
        }
        AsyncContentTransformer contentTransformer = 
                (AsyncContentTransformer) activeTransformers.iterator().next();
        
        String runningTransformationId = contentTransformer.transform(reader, writer, options, true);
        runningTransformations.put(runningTransformationId, contentTransformer);
        return runningTransformationId;
    }
    
    
    /**
     * Gets the async capable content transformers
     * 
     * @param sourceMimetype
     * @param sourceSize
     * @param targetMimetype
     * @param options
     * @param executeAsyncronously
     * @return the async transformers
     */
    public List<ContentTransformer> getActiveTransformers(
            String sourceMimetype, long sourceSize, String targetMimetype, TransformationOptions options, boolean executeAsyncronously)
    {
        List<ContentTransformer> activeTransformers = getActiveTransformers(sourceMimetype, sourceSize, targetMimetype, options);
        if (!executeAsyncronously)
        {
            return activeTransformers;
        }
        
        List<ContentTransformer> activeAysncTransformers = new ArrayList<ContentTransformer>();
        for (ContentTransformer contentTransformer : activeTransformers)
        {
            if (!(contentTransformer instanceof AsyncContentTransformer))
            {
                continue;
            }
            if (((AsyncContentTransformer) contentTransformer).isTransformable(
                    sourceMimetype, sourceSize, targetMimetype, options))
            {
                activeAysncTransformers.add(contentTransformer);
            }
        }
        return activeAysncTransformers;
    }
    
    public Float getProgress(String runningTransformationId)
    {
        Float progress = null;
        for (AsyncContentTransformer transformer : runningTransformations.values())
        {
            progress = transformer.getProgress(runningTransformationId);
            if (progress != null)
            {
                break;
            }
        }
        return progress;
    }
    
}
