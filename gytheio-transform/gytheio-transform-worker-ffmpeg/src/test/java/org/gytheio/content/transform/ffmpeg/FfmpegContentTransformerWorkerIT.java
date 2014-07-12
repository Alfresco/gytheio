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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorkResult;
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
import org.gytheio.content.transform.options.AudioTransformationOptions;
import org.gytheio.content.transform.options.ImageResizeOptions;
import org.gytheio.content.transform.options.TemporalSourceOptions;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.TransformationOptionsImpl;
import org.gytheio.content.transform.options.VideoTransformationOptions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @see {@link FfmpegContentTransformerWorker}
 * 
 * @author Derek Hulley
 * @author Ray Gauss II
 */
public class FfmpegContentTransformerWorkerIT extends AbstractContentTransformerWorkerTest
{
    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorkerIT.class);

    private ContentTransformerWorker transformerWorker;
    private StringListProgressReporter testProgressReporter;
    private File sourceFile;
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
        
        testProgressReporter = new StringListProgressReporter();
        
        sourceFile = new File(this.getClass().getResource("/quick/quick.mpg").toURI());
        source = new ContentReference(
                sourceFile.toURI().toString(), "video/mpeg", sourceFile.length());
    }
    
    @Test
    public void testVersion() throws Exception
    {
        assertTrue(transformerWorker.getVersionString().contains("Gytheio FFmpeg Content Transformer Worker"));
        assertTrue(transformerWorker.getVersionDetailsString().contains("ffmpeg version") ||
                transformerWorker.getVersionDetailsString().contains("FFmpeg version 0"));
    }
    
    @Test
    public void testIsTransformable() throws Exception
    {
        boolean isTransformable = transformerWorker.isTransformable(
                Arrays.asList(FileMediaType.VIDEO_MP4.getMediaType()),
                FileMediaType.IMAGE_PNG.getMediaType(),
                new TransformationOptionsImpl());
        assertTrue("Should be supported with standard options", isTransformable);
        isTransformable = transformerWorker.isTransformable(
                Arrays.asList(FileMediaType.VIDEO_MPG.getMediaType()),
                FileMediaType.VIDEO_MPG.getMediaType(),
                new TransformationOptionsImpl());
        assertTrue("Should be supported with standard options", isTransformable);

        isTransformable = transformerWorker.isTransformable(
                Arrays.asList("audio/vnd.adobe.soundbooth"),
                FileMediaType.MP3.getMediaType(),
                new TransformationOptionsImpl());
        assertFalse("Should *not* be supported with standard options", isTransformable);
        isTransformable = transformerWorker.isTransformable(
                Arrays.asList(FileMediaType.MP3.getMediaType()),
                FileMediaType.IMAGE_PNG.getMediaType(),
                new TransformationOptionsImpl());
        assertFalse("Should *not* be supported with standard options", isTransformable);
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
                testProgressReporter);
        
        long targetSize = targetFile.length();
        
        assertTrue("Target file size is zero", targetSize > 0);
        assertTrue("Trimmed target file size should be less than 1/2 original size of " + source.getSize() +
                " but was " + targetSize, targetSize < source.getSize()/2);
    }
    
    protected List<String> getStreamDetails(String details)
    {
        List<String> streams = new ArrayList<String>();
        String[] lines = details.split("\\n");
        for (String line : lines)
        {
            if (line.matches(".*Stream .*"))
            {
                streams.add(line.trim());
            }
        }
        return streams;
    }
    
    protected void assertSomeStreamMatches(String message, List<String> streams, String regex)
    {
        boolean matched = false;
        for (String stream : streams)
        {
            if (stream.matches(regex))
            {
                matched = true;
                break;
            }
        }
        assertTrue(message, matched);
    }
    
    @Test
    public void testGetDetails() throws Exception
    {
        String details = ((FfmpegContentTransformerWorker) transformerWorker).getDetails(sourceFile);
        assertTrue(details.contains("mpeg1video"));
    }
    
    protected List<String> testTransformation(
            FileMediaType targetMediaType,
            VideoTransformationOptions options) throws Exception
    {
        return testTransformation(source, targetMediaType, options, testProgressReporter);
    }
    
    protected List<String> testTransformation(
            ContentReference sourceReference,
            FileMediaType targetMediaType,
            VideoTransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception
    {
        String sourceDetails = ((FfmpegContentTransformerWorker) transformerWorker).getDetails(sourceFile);
        List<String> sourceStreams = getStreamDetails(sourceDetails);
        for (String stream : sourceStreams)
        {
            logger.debug("Source " + stream);
        }
        
        String sourceExtension = FileMediaType.VIDEO_MPG.getExtension();
        String targetExtension = targetMediaType.getExtension();
        
        // make a writer for the target file
        File targetFile = TempFileProvider.createTempFile(
                getClass().getSimpleName() + "_quick_" + sourceExtension + "_",
                "." + targetExtension);
        
        ContentReference target = new ContentReference(
                targetFile.toURI().toString(), targetMediaType.getMediaType());
        
        transformerWorker.transform(
                Arrays.asList(sourceReference), 
                Arrays.asList(target), 
                options, 
                progressReporter);
        
        long targetSize = targetFile.length();
        assertTrue("Target file size is zero", targetSize > 0);
        
        String targetDetails = ((FfmpegContentTransformerWorker) transformerWorker).getDetails(targetFile);
        List<String> targetStreams = getStreamDetails(targetDetails);
        if (targetStreams != null)
        {
            for (String stream : targetStreams)
            {
                logger.debug("Target " + stream);
            }
        }
        return targetStreams;
    }
    
    @Test
    public void testResizeMaintainAspectRatioTransformation() throws Exception
    {
        ImageResizeOptions resizeOptions = new ImageResizeOptions();
        resizeOptions.setHeight(180);
        resizeOptions.setWidth(396);
        resizeOptions.setMaintainAspectRatio(true);
        
        VideoTransformationOptions options = new VideoTransformationOptions();
        options.setResizeOptions(resizeOptions);
        
        List<String> targetStreams = testTransformation(FileMediaType.VIDEO_MP4, options);
        
        assertSomeStreamMatches("Target resolution incorrect", targetStreams, ".*Video: .*320x180.*");
    }
    
    @Test
    public void testResizeMaintainAspectRatioNoWidthTransformation() throws Exception
    {
        ImageResizeOptions resizeOptions = new ImageResizeOptions();
        resizeOptions.setHeight(180);
        resizeOptions.setMaintainAspectRatio(true);
        
        VideoTransformationOptions options = new VideoTransformationOptions();
        options.setResizeOptions(resizeOptions);
        
        List<String> targetStreams = testTransformation(FileMediaType.VIDEO_MP4, options);
        
        assertSomeStreamMatches("Target resolution incorrect", targetStreams, ".*Video: .*320x180.*");
    }
    
    @Test
    public void testResizeStretchTransformation() throws Exception
    {
        ImageResizeOptions resizeOptions = new ImageResizeOptions();
        resizeOptions.setHeight(180);
        resizeOptions.setWidth(396);
        resizeOptions.setMaintainAspectRatio(false);
        
        VideoTransformationOptions options = new VideoTransformationOptions();
        options.setResizeOptions(resizeOptions);
        
        List<String> targetStreams = testTransformation(FileMediaType.VIDEO_MP4, options);
        
        assertSomeStreamMatches("Target resolution incorrect", targetStreams, ".*Video: .*396x180.*");
    }
    
    @Test
    public void testProxyTransformation() throws Exception
    {
        ImageResizeOptions resizeOptions = new ImageResizeOptions();
        resizeOptions.setHeight(180);
        resizeOptions.setWidth(396);
        
        VideoTransformationOptions options = new VideoTransformationOptions();
        options.setResizeOptions(resizeOptions);
        options.setTargetVideoCodec(VideoTransformationOptions.VIDEO_CODEC_H264);
        options.setTargetVideoBitrate(80000L);
        options.setTargetVideoFrameRate(10.0f);
        options.setTargetAudioCodec(AudioTransformationOptions.AUDIO_CODEC_AAC);
        options.setTargetAudioSamplingRate(11025);
        options.setTargetAudioBitrate(20000L);
        options.setTargetAudioChannels(2);
        
        List<String> targetStreams = testTransformation(FileMediaType.VIDEO_MP4, options);
        
        assertSomeStreamMatches("Target video codec incorrect", targetStreams, ".*Video: .*h264.*");
        assertSomeStreamMatches("Target resolution incorrect", targetStreams, ".*Video: .*320x180.*");
        assertSomeStreamMatches("Target frame rate incorrect", targetStreams, ".*Video: .*10 fps.*");
        assertSomeStreamMatches("Target video bitrate incorrect", targetStreams, ".*Video: .*20 kb\\/s.*");
        assertSomeStreamMatches("Target audio codec incorrect", targetStreams, ".*Audio: .*aac.*");
        assertSomeStreamMatches("Target audio sampling rate incorrect", targetStreams, ".*Audio: .*11025 Hz.*");
        assertSomeStreamMatches("Target audio bitrate incorrect", targetStreams, ".*Audio: .*22 kb\\/s.*");
        assertSomeStreamMatches("Target audio codec incorrect", targetStreams, ".*Audio: .*stereo.*");
        
        List<String> progressEvents = testProgressReporter.getProgressEvents();
        assertTrue(progressEvents.size() > 0);
    }
    
    @Test
    public void testInavlidTransformation() throws Exception
    {
        VideoTransformationOptions options = new VideoTransformationOptions();
        options.setTargetVideoCodec(VideoTransformationOptions.VIDEO_CODEC_H264);
        options.setTargetAudioCodec(AudioTransformationOptions.AUDIO_CODEC_AAC);
        
        try
        {
            testTransformation(FileMediaType.VIDEO_WEBM, options);
            fail("Only VP8 video and Vorbis audio are supported for WebM");
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage().contains("Only VP8 video and Vorbis audio are supported for WebM"));
        }
    }
    
    @Test
    @Ignore
    public void testStoryboardThumbnails() throws Exception
    {
        TransformationOptions options = new TransformationOptionsImpl();
        List<ContentWorkResult> results = transformerWorker.transform(
                Arrays.asList(source), 
                FileMediaType.IMAGE_JPEG.getMediaType(), 
                options, 
                testProgressReporter);
        assertEquals(4, results.size());
        for (ContentWorkResult result : results)
        {
            assertTrue("Target file size is zero", result.getContentReference().getSize() > 0);
        }
    }
    
    protected class StringListProgressReporter implements ContentTransformerWorkerProgressReporter
    {
        protected static final String PREFIX_STARTED = "STARTED";
        protected static final String PREFIX_PROGRESS = "PROGRESS: ";
        protected static final String PREFIX_COMPLETE = "COMPLETE: ";
        protected static final String PREFIX_ERROR = "ERROR: ";
        
        private List<String> progressEvents = new ArrayList<String>();
        
        @Override
        public void onTransformationStarted()
        {
            progressEvents.add(PREFIX_STARTED);
        }

        @Override
        public void onTransformationProgress(float progress)
        {
            String progressEvent = PREFIX_PROGRESS + progress;
            progressEvents.add(PREFIX_PROGRESS + progress);
            logger.debug("**** progressEvent=" + progressEvent);
        }

        @Override
        public void onTransformationComplete(List<ContentWorkResult> results)
        {
            progressEvents.add(PREFIX_COMPLETE);
        }

        @Override
        public void onTransformationError(String errorMessage)
        {
            progressEvents.add(PREFIX_ERROR + errorMessage);
        }

        public List<String> getProgressEvents()
        {
            return progressEvents;
        }
    }
}
