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

import org.gytheio.content.AbstractContentWorker;
import org.gytheio.content.ContentReference;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.transform.options.TransformationOptions;

/**
 * Abstract transform node worker which uses a content reference handler to convert the 
 * content reference into a usable File object for the actual implementation.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentTransformerWorker 
        extends AbstractContentWorker implements ContentTransformerWorker
{
    protected ContentReferenceHandler targetContentReferenceHandler;
    
    public void setTargetContentReferenceHandler(ContentReferenceHandler targetContentReferenceHandler)
    {
        this.targetContentReferenceHandler = targetContentReferenceHandler;
    }

    public void initialize()
    {
    }

    public void transform(
            ContentReference source, 
            ContentReference target,
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception
    {
        File sourceFile = sourceContentReferenceHandler.getFile(source, true);
        File targetFile = targetContentReferenceHandler.getFile(target);
        transformInternal(
                sourceFile, source.getMediaType(), 
                targetFile, target.getMediaType(), 
                options,
                progressReporter);
        target.setSize(targetFile.length());
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
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception;
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("sourceContentReferenceHandler: " + sourceContentReferenceHandler.toString());
        builder.append(", ");
        builder.append("targetContentReferenceHandler: " + targetContentReferenceHandler.toString());
        builder.append("]");
        return builder.toString();
    }

}
