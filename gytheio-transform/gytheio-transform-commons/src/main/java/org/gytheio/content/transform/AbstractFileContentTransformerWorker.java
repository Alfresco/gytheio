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
package org.gytheio.content.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.FileContentReferenceHandler;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.options.TransformationOptions;

/**
 * Extension of AbstractContentTransformerWorker for dealing with file
 * content references
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractFileContentTransformerWorker extends AbstractContentTransformerWorker
{

    @Override
    public List<ContentReference> transform(
            List<ContentReference> sources, 
            List<ContentReference> targets,
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception
    {
        boolean isTempTargetUsed = true;
        List<FileContentReferencePair> sourcePairs = null;
        List<FileContentReferencePair> targetPairs = null;
        
        if (sources != null)
        {
            sourcePairs = new ArrayList<>(sources.size());
            if (sourceContentReferenceHandler instanceof FileContentReferenceHandler)
            {
                for (ContentReference source : sources)
                {
                    sourcePairs.add(new FileContentReferencePair(
                            ((FileContentReferenceHandler) sourceContentReferenceHandler).getFile(
                                    source, true), 
                            source));
                }
            }
            else
            {
                for (ContentReference source : sources)
                {
                    InputStream sourceInputStream = sourceContentReferenceHandler.getInputStream(source, true);
                    File sourceFile = createTempFile(source);
                    FileUtils.copyInputStreamToFile(sourceInputStream, sourceFile);
                    
                    sourcePairs.add(new FileContentReferencePair(sourceFile, source));
                }
            }
        }
        if (targets != null)
        {
            targetPairs = new ArrayList<FileContentReferencePair>(targets.size());
            if (targetContentReferenceHandler instanceof FileContentReferenceHandler)
            {
                isTempTargetUsed = false;
                for (ContentReference target : targets)
                {
                    File targetFile = ((FileContentReferenceHandler) targetContentReferenceHandler).getFile(target, false);
                    targetPairs.add(new FileContentReferencePair(targetFile, target));
                }
            }
            else
            {
                for (ContentReference target : targets)
                {
                    File targetFile = createTempFile(target);
                    targetPairs.add(new FileContentReferencePair(targetFile, target));
                }
            }
        }
        
        List<File> resultFiles = transformInternal(
                sourcePairs, 
                targetPairs, 
                options,
                progressReporter);
        
        if (resultFiles == null)
        {
            return null;
        }
        // We're assuming the final results are the same size and in the same order as the targets if present
        if (targets != null && targets.size() != resultFiles.size())
        {
            throw new IllegalStateException(
                    "The number of actual target files (" + resultFiles.size() + ") " +
                    "did not match the number of expected targets (" + targets.size() + ")");
        }
        
        List<ContentReference> results = new ArrayList<ContentReference>(resultFiles.size());
        
        for (int i = 0; i < resultFiles.size(); i++)
        {
            File resultFile = resultFiles.get(i);
            String resultMediaType = FileMediaType.SERVICE.getMediaTypeByName(resultFile);
            ContentReference target = null;
            if (isTempTargetUsed)
            {
                if (targets != null)
                {
                    target = targets.get(i);
                }
                else
                {
                    target = targetContentReferenceHandler.createContentReference(
                            resultFile.getName(), resultMediaType);
                }
                FileInputStream targetInputStream = new FileInputStream(resultFile);
                targetContentReferenceHandler.putInputStream(targetInputStream, target);
            }
            else
            {
                target = targets.get(i);
            }
            target.setSize(resultFile.length());
            results.add(target);
        }
        return results;
    }
    
    /**
     * Creates a temp file from the given content reference
     * 
     * @param contentReference
     * @return the temp file
     */
    protected File createTempFile(ContentReference contentReference)
    {
        return TempFileProvider.createTempFile(
                this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString(), 
                "." + getExtension(contentReference));
    }
    
    /**
     * Transforms the given source file to the given target file and media type using
     * the given transformation options and reports progress via the given progress reporter.
     * 
     * @param sourceFiles
     * @param sourceRefs
     * @param targetFiles
     * @param targetRefs
     * @param options
     * @param progressReporter
     * @throws Exception
     */
    protected abstract List<File> transformInternal(
            List<FileContentReferencePair> sources, 
            List<FileContentReferencePair> targets, 
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception;

    /**
     * Wrapper for a content reference and a {@link File}, useful
     * for passing around an already instantiated File object with
     * a related content reference.
     */
    protected class FileContentReferencePair
    {
        private File file;
        private ContentReference contentReference;
        
        public FileContentReferencePair(File file, ContentReference contentReference)
        {
            super();
            this.file = file;
            this.contentReference = contentReference;
        }
        
        public File getFile()
        {
            return file;
        }
        public void setFile(File file)
        {
            this.file = file;
        }
        public ContentReference getContentReference()
        {
            return contentReference;
        }
        public void setContentReference(ContentReference contentReference)
        {
            this.contentReference = contentReference;
        }
    }
}
