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
package org.gytheio.content.transform;

import java.io.File;

import org.gytheio.content.ContentReference;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.transform.options.TransformationOptions;

/**
 * Abstract transform node worker which uses a content reference handler to convert the 
 * content reference into a usable File object for the actual implementation.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentTransformerWorker implements ContentTransformerNodeWorker
{
    
    protected ContentReferenceHandler contentReferenceHandler;
    
    public void setContentReferenceHandler(ContentReferenceHandler contentReferenceFileHandler)
    {
        this.contentReferenceHandler = contentReferenceFileHandler;
    }

    public void transform(
            ContentReference source, 
            ContentReference target,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception
    {
        transformInternal(
                contentReferenceHandler.getFile(source), source.getMediaType(), 
                contentReferenceHandler.getFile(target), target.getMediaType(), 
                options,
                progressReporter);
    }
    
    /**
     * Transforms the given source file to the given target file and media type using
     * the given transformation options and reports progress via the given progress reporter.
     * 
     * @param sourceFile
     * @param sourceMimetype
     * @param targetFile
     * @param targetMimetype
     * @param options
     * @param progressReporter
     * @throws Exception
     */
    protected abstract void transformInternal(
            File sourceFile, String sourceMimetype, 
            File targetFile, String targetMimetype,
            TransformationOptions options,
            ContentTransformerNodeWorkerProgressReporter progressReporter) throws Exception;

}
