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
package org.gytheio.content.transform.ffmpeg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

import org.alfresco.service.cmr.repository.TemporalSourceOptions;
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
import org.gytheio.content.transform.ffmpeg.FfmpegContentTransformerWorker;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.TransformationOptionsImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @see org.alfresco.repo.content.transform.ffmpeg.FfmpegContentTransformerWorker
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public class FfmpegContentTransformerWorkerTest extends AbstractContentTransformerWorkerTest
{
    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorkerTest.class);

    private ContentTransformerWorker transformerWorker;
    private ContentTransformerWorkerProgressReporter progressReporter;
    private ContentReference source;
    
    @Before
    public void setUp() throws Exception {
        FileProvider fileProvider = new FileProviderImpl(TempFileProvider.getTempDir().getPath());
        ContentReferenceHandler contentReferenceHandler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) contentReferenceHandler).setFileProvider(fileProvider);
        transformerWorker = new FfmpegContentTransformerWorker();
        ((FfmpegContentTransformerWorker) transformerWorker).setSourceContentReferenceHandler(
                contentReferenceHandler);
        ((FfmpegContentTransformerWorker) transformerWorker).setTargetContentReferenceHandler(
                contentReferenceHandler);
        ((FfmpegContentTransformerWorker) transformerWorker).initialize();
        
        progressReporter = new LoggingProgressReporterImpl();
        
        File sourceFile = new File(this.getClass().getResource("/quick/quick.mpg").toURI());
        source = new ContentReference(
                sourceFile.toURI().toString(), "video/mpeg", sourceFile.length());
    }
    
    
    @Test
    public void testTrimTransformation() throws Exception
    {
        TemporalSourceOptions temporalSourceOptions = new TemporalSourceOptions();
        temporalSourceOptions.setOffset("00:00:00.5");
        temporalSourceOptions.setDuration("00:00:00.2");
        TransformationOptions options = new TransformationOptionsImpl();
        options.addSourceOptions(temporalSourceOptions);
        
        String sourceExtension = "mpg";
        String targetExtension = "mp4";
        
        // make a writer for the target file
        File targetFile = TempFileProvider.createTempFile(
                getClass().getSimpleName() + "_quick_" + sourceExtension + "_",
                "." + targetExtension);
        
        ContentReference target = new ContentReference(
                targetFile.toURI().toString(), "video/mp4");
        
        transformerWorker.transform(
                Arrays.asList(source), 
                Arrays.asList(target), 
                options, 
                progressReporter);
        
        long targetSize = targetFile.length();
        
        assertTrue("Target file size is zero", targetSize > 0);
        assertTrue("Trimmed target file size should be less than 1/2 original size of " + source.getSize() +
                " but was " + targetSize, targetSize < source.getSize()/2);
    }
    
    @Test
    @Ignore
    public void testStoryboardThumbnails() throws Exception
    {
        TransformationOptions options = new TransformationOptionsImpl();
        List<ContentReference> targets = transformerWorker.transform(
                Arrays.asList(source), 
                FileMediaType.IMAGE_JPEG.getMediaType(), 
                options, 
                progressReporter);
        assertEquals(4, targets.size());
        for (ContentReference target : targets)
        {
            assertTrue("Target file size is zero", target.getSize() > 0);
        }
    }
}
