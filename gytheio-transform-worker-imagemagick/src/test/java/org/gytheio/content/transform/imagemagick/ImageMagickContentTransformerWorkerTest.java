/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform.imagemagick;

import static junit.framework.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.service.cmr.repository.PagedSourceOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.AbstractContentTransformerWorkerTest;
import org.gytheio.content.transform.ContentTransformerWorker;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;
import org.gytheio.content.transform.options.ImageTransformationOptions;
import org.gytheio.content.transform.options.TransformationOptions;
import org.junit.Before;
import org.junit.Test;

/**
 * @see org.alfresco.repo.content.transform.ffmpeg.FfmpegContentTransformerWorker
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public class ImageMagickContentTransformerWorkerTest extends AbstractContentTransformerWorkerTest
{
    private static final Log logger = LogFactory.getLog(ImageMagickContentTransformerWorkerTest.class);

    private ContentTransformerWorker transformerWorker;
    private ContentReferenceHandler contentReferenceHandler;
    private ContentTransformerWorkerProgressReporter progressReporter;
    
    @Before
    public void setUp() throws Exception {
        FileProvider fileProvider = new TempFileProvider();
        contentReferenceHandler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) contentReferenceHandler).setFileProvider(fileProvider);
        progressReporter = new LoggingProgressReporterImpl();
        
        transformerWorker = new ImageMagickContentTransformerWorker();
        ((ImageMagickContentTransformerWorker) transformerWorker).setSourceContentReferenceHandler(
                contentReferenceHandler);
        ((ImageMagickContentTransformerWorker) transformerWorker).setTargetContentReferenceHandler(
                contentReferenceHandler);
        ((ImageMagickContentTransformerWorker) transformerWorker).initialize();
    }
    
    protected void transform(String sourceMimetype, String targetMimetype, TransformationOptions options) throws Exception
    {
        String[] quickFiles = getQuickFilenames(sourceMimetype);
        for (String quickFile : quickFiles)
        {
            String sourceExtension = quickFile.substring(quickFile.lastIndexOf('.')+1);
            String targetExtension = mediaTypeService.getExtension(targetMimetype);
            
            // is there a test file for this conversion?
            ContentReference sourceReference = 
                    AbstractContentTransformerWorkerTest.getNamedQuickTestFileReference(quickFile, sourceMimetype);
            if (sourceReference == null)
            {
                continue;  // no test file available for that extension
            }
            
            String callingMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            
            // make a writer for the target file
            String targetFileName = getClass().getSimpleName() + "_" + callingMethodName + "_" + 
            sourceExtension + "." + targetExtension;
            ContentReference targetReference = 
                    contentReferenceHandler.createContentReference(targetFileName, targetMimetype);
            
            // do the transformation
            transformerWorker.transform(sourceReference, targetReference, options, progressReporter);
            
            File targetFile = contentReferenceHandler.getFile(targetReference);
            
            assertTrue(targetFile.length() > 10);
        }
    }
    
    @Test
    public void testPageSourceOptions() throws Exception
    {
        // Test empty source options
        ImageTransformationOptions options = new ImageTransformationOptions();
        this.transform(
                FileMediaType.MEDIATYPE_PDF.getMediaType(), 
                FileMediaType.MEDIATYPE_IMAGE_PNG.getMediaType(), 
                options);
        
        // Test first page
        options = new ImageTransformationOptions();
        List<TransformationSourceOptions> sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        sourceOptionsList.add(PagedSourceOptions.getPage1Instance());
        options.setSourceOptionsList(sourceOptionsList);
        this.transform(
                FileMediaType.MEDIATYPE_PDF.getMediaType(),
                FileMediaType.MEDIATYPE_IMAGE_PNG.getMediaType(), 
                options);
        
        // Test second page
        options = new ImageTransformationOptions();
        sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(2);
        sourceOptions.setEndPageNumber(2);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        this.transform(
                FileMediaType.MEDIATYPE_PDF.getMediaType(), 
                FileMediaType.MEDIATYPE_IMAGE_PNG.getMediaType(), 
                options);
        
        // Test page range invalid for target type
        options = new ImageTransformationOptions();
        sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(1);
        sourceOptions.setEndPageNumber(2);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        try {
            this.transform(
                    FileMediaType.MEDIATYPE_PDF.getMediaType(), 
                    FileMediaType.MEDIATYPE_IMAGE_PNG.getMediaType(), 
                    options);
            fail("An exception regarding an invalid page range should have been thrown");
        }
        catch (Exception e)
        {
            // failure expected
        }
        
        // Test page out of range
        options = new ImageTransformationOptions();
        sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(3);
        sourceOptions.setEndPageNumber(3);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        try {
            this.transform(
                    FileMediaType.MEDIATYPE_PDF.getMediaType(), 
                    FileMediaType.MEDIATYPE_IMAGE_PNG.getMediaType(), 
                    options);
            fail("An exception regarding an invalid page range should have been thrown");
        }
        catch (Exception e)
        {
            // failure expected
        }
    }
}
