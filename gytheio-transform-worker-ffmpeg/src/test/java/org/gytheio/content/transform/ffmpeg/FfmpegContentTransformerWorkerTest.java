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
package org.gytheio.content.transform.ffmpeg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

import static junit.framework.Assert.*;

import org.alfresco.service.cmr.repository.TemporalSourceOptions;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.FileProvider;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.handler.FileContentReferenceHandlerImpl;
import org.gytheio.content.transform.ContentTransformerWorker;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;
import org.gytheio.content.transform.ffmpeg.FfmpegContentTransformerWorker;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.TransformationOptionsImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @see org.alfresco.repo.content.transform.ffmpeg.FfmpegContentTransformerWorker
 * 
 * @author Derek Hulley, Ray Gauss II
 */
public class FfmpegContentTransformerWorkerTest
{
    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorkerTest.class);

    private ContentTransformerWorker transformerWorker;
    
    @Before
    public void setUp() throws Exception {
        FileProvider fileProvider = new TempFileProvider();
        ContentReferenceHandler contentReferenceHandler = new FileContentReferenceHandlerImpl();
        ((FileContentReferenceHandlerImpl) contentReferenceHandler).setFileProvider(fileProvider);
        transformerWorker = new FfmpegContentTransformerWorker();
        ((FfmpegContentTransformerWorker) transformerWorker).setSourceContentReferenceHandler(
                contentReferenceHandler);
        ((FfmpegContentTransformerWorker) transformerWorker).setTargetContentReferenceHandler(
                contentReferenceHandler);
        ((FfmpegContentTransformerWorker) transformerWorker).initialize();
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
            
            File sourceFile = new File(this.getClass().getResource("/quick/quick.mpg").toURI());
            long origSize = sourceFile.length();
            
            ContentReference source = new ContentReference(
                    this.getClass().getResource("/quick/quick.mpg").toURI().toString(), "video/mpeg");
            
            // make a writer for the target file
            File targetFile = TempFileProvider.createTempFile(
                    getClass().getSimpleName() + "_quick_" + sourceExtension + "_",
                    "." + targetExtension);
            
            ContentReference target = new ContentReference(
                    targetFile.toURI().toString(), "video/mp4");
            
            
            transformerWorker.transform(source, target, options, new LoggingProgressReporterImpl());
            
            long targetSize = targetFile.length();
            
            assertTrue("Target file size is zero", targetSize > 0);
            assertTrue("Trimmed target file size should be less than 1/2 original size of " + origSize +
                    " but was " + targetSize, targetSize < origSize/2);
        
    }
    
    public class LoggingProgressReporterImpl implements ContentTransformerWorkerProgressReporter
    {
        
        public void onTransformationStarted()
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Starting transformation");
            }
        }
        
        public void onTransformationProgress(float progress)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug(progress*100 + "% progress on transformation");
            }
        }
        
        public void onTransformationComplete()
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Completed transformation");
            }
        }
    }
}
