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
package org.gytheio.content.transform.imagemagick;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.alfresco.service.cmr.repository.PagedSourceOptions;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.FileProviderImpl;
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
        FileProvider fileProvider = new FileProviderImpl(TempFileProvider.getTempDir().getPath());
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
    
    protected List<ContentReference> transform(
            String sourceMimetype, 
            String targetMimetype, 
            TransformationOptions options,
            String filename,
            boolean testCreatesTargetReference) throws Exception
    {
        String sourceExtension = filename.substring(filename.lastIndexOf('.')+1);
        String targetExtension = mediaTypeService.getExtension(targetMimetype);
        
        // is there a test file for this conversion?
        ContentReference sourceReference = 
                AbstractContentTransformerWorkerTest.getNamedQuickTestFileReference(
                        filename, sourceMimetype);
        if (sourceReference == null)
        {
            return null;  // no test file available for that extension
        }
        
        String callingMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        
        List<ContentReference> targetReferences = null;
        
        if (testCreatesTargetReference)
        {
            // make a writer for the target file
            String targetFileName = getClass().getSimpleName() + "_" + callingMethodName + "_" + 
            sourceExtension + "." + targetExtension;
            ContentReference createdTargetReference = 
                    contentReferenceHandler.createContentReference(targetFileName, targetMimetype);
            List<ContentReference> createdTargetReferences = Arrays.asList(createdTargetReference);
            
            // do the transformation
            targetReferences = transformerWorker.transform(
                    Arrays.asList(sourceReference), 
                    createdTargetReferences,
                    options, progressReporter);
        }
        else
        {
            // do the transformation
            targetReferences = transformerWorker.transform(
                    Arrays.asList(sourceReference), 
                    targetMimetype,
                    options, progressReporter);
        }
        
        if (targetReferences != null)
        {
            for (ContentReference targetReference : targetReferences)
            {
                assertTrue(targetReference.getUri() + 
                        " size too small ", targetReference.getSize() > 10);
            }
        }
        return targetReferences;
    }
    
    protected List<ContentReference> transform(
            String sourceMimetype, 
            String targetMimetype, 
            TransformationOptions options,
            boolean testsCreatesTargetReferences) throws Exception
    {
        List<ContentReference> targetReferences = new ArrayList<ContentReference>();
        String[] quickFiles = getQuickFilenames(sourceMimetype);
        for (String quickFile : quickFiles)
        {
            List<ContentReference> quickTargetReferences = transform(
                    sourceMimetype, targetMimetype, options, quickFile, testsCreatesTargetReferences);
            if (quickTargetReferences != null)
            {
                targetReferences.addAll(quickTargetReferences);
            }
        }
        return targetReferences;
    }
    
    @Test
    public void testEmptyPageSourceOptions() throws Exception
    {
        // Test empty source options
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<ContentReference> targetReferences = this.transform(
                FileMediaType.PDF.getMediaType(), 
                FileMediaType.IMAGE_PNG.getMediaType(), 
                options,
                true);
        assertTrue(targetReferences.get(0).getSize() > 10);
    }
    
    @Test
    public void testWorkerMultiTargetCreation() throws Exception
    {
        // Test empty source options
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<ContentReference> targetReferences = this.transform(
                FileMediaType.PDF.getMediaType(), 
                FileMediaType.IMAGE_PNG.getMediaType(), 
                options,
                "quick.pdf",
                false);
        assertEquals(2, targetReferences.size());
        assertTrue(targetReferences.get(0).getSize() > 10);
    }
    
    @Test
    public void testFirstPageSourceOptions() throws Exception
    {
        // Test first page
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<TransformationSourceOptions> sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        sourceOptionsList.add(PagedSourceOptions.getPage1Instance());
        options.setSourceOptionsList(sourceOptionsList);
        List<ContentReference> targetReferences = this.transform(
                FileMediaType.PDF.getMediaType(),
                FileMediaType.IMAGE_PNG.getMediaType(), 
                options,
                true);
        assertTrue(targetReferences.get(0).getSize() > 10);
    }
    
    @Test
    public void testSecondPageSourceOptions() throws Exception
    {
        // Test second page
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<TransformationSourceOptions> sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(2);
        sourceOptions.setEndPageNumber(2);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        List<ContentReference> targetReferences = this.transform(
                FileMediaType.PDF.getMediaType(), 
                FileMediaType.IMAGE_PNG.getMediaType(), 
                options,
                true);
        assertTrue(targetReferences.get(0).getSize() > 10);
    }
    
    @Test
    public void testRangePageSourceOptions() throws Exception
    {
        // Test page range invalid for target type
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<TransformationSourceOptions> sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(1);
        sourceOptions.setEndPageNumber(2);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        try {
            this.transform(
                    FileMediaType.PDF.getMediaType(), 
                    FileMediaType.APPLICATION_PHOTOSHOP.getMediaType(), 
                    options,
                    true);
            fail("An exception regarding an invalid page range should have been thrown");
        }
        catch (Exception e)
        {
            // failure expected
        }
    }
    
    @Test
    public void testOutOfRangePageSourceOptions() throws Exception
    {
        // Test page out of range
        ImageTransformationOptions options = new ImageTransformationOptions();
        List<TransformationSourceOptions> sourceOptionsList = new ArrayList<TransformationSourceOptions>();
        PagedSourceOptions sourceOptions = new PagedSourceOptions();
        sourceOptions.setStartPageNumber(3);
        sourceOptions.setEndPageNumber(3);
        sourceOptionsList.add(sourceOptions);
        options.setSourceOptionsList(sourceOptionsList);
        try {
            this.transform(
                    FileMediaType.PDF.getMediaType(), 
                    FileMediaType.IMAGE_PNG.getMediaType(), 
                    options,
                    true);
            fail("An exception regarding an invalid page range should have been thrown");
        }
        catch (Exception e)
        {
            // failure expected
        }
    }
}
