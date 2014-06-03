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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.AbstractRuntimeExecContentTransformerWorker;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;
import org.gytheio.content.transform.options.ImageResizeOptions;
import org.gytheio.content.transform.options.ImageTransformationOptions;
import org.gytheio.content.transform.options.TemporalSourceOptions;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.VideoTransformationOptions;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.util.exec.RuntimeExec;

/**
 * An FFmpeg command line implementation of a content hash node worker
 * 
 * @author Ray Gauss II
 */
public class FfmpegContentTransformerWorker extends AbstractRuntimeExecContentTransformerWorker
{
    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorker.class);
    
    protected static final String CMD_OPT_ASSIGNMENT = " ";
    protected static final String CMD_OPT_DELIMITER = " ";
    protected static final String CMD_OPT_NUM_VIDEO_FRAMES = "-vframes";
    protected static final String CMD_OPT_DISABLE_AUDIO = "-an";
    protected static final String CMD_OPT_DISABLE_VIDEO = "-vn";
    protected static final String CMD_OPT_DISABLE_SUBTITLES = "-sn";
    protected static final String CMD_OPT_VIDEO_CODEC = "-vcodec";
    protected static final String CMD_OPT_FORMAT = "-f";
    protected static final String CMD_OPT_DURATION = "-t";
    protected static final String CMD_OPT_OFFSET = "-ss";
    protected static final String CMD_OPT_SIZE = "-s";
    protected static final String CMD_OPT_PAIR_1_FRAME = CMD_OPT_NUM_VIDEO_FRAMES + CMD_OPT_ASSIGNMENT + "1";
    
    public static final String VAR_OPTIONS = "options";

    /** offset variable name */
    public static final String VAR_OFFSET = "offset";
    
    /** duration variable name */
    public static final String VAR_DURATION = "duration";

    /** source variable name */
    public static final String VAR_SOURCE = "source";

    /** target variable name */
    public static final String VAR_TARGET = "target";

    protected static final String DEFAULT_OFFSET = "00:00:00";
    
    private static final String PREFIX_IMAGE = "image/";
    private static final String PREFIX_AUDIO = "audio/";
    
    private String ffmpegExe = "ffmpeg";
    
    @Override
    protected void initializeExecuter()
    {
        if (executer == null)
        {
            if (System.getProperty("ffmpeg.exe") != null)
            {
                ffmpegExe = System.getProperty("ffmpeg.exe");
            }
            executer = new RuntimeExec();
            Map<String, String[]> commandsAndArguments = new HashMap<>();
            commandsAndArguments.put(".*", new String[] { 
                ffmpegExe,
                "-y",
                "-i",
                "${source}",
                "SPLIT:${options}",
                "${target}"
            });
            executer.setCommandsAndArguments(commandsAndArguments);
        }
    }

    @Override
    protected void initializeVersionDetailsExecuter()
    {
        if (versionDetailsExecuter == null)
        {
            versionDetailsExecuter = new RuntimeExec();
            Map<String, String[]> checkCommandsAndArguments = new HashMap<>();
            checkCommandsAndArguments.put(".*", new String[] { 
                ffmpegExe,
                "-version",
            });
            versionDetailsExecuter.setCommandsAndArguments(checkCommandsAndArguments);
        }
    }

    @Override
    protected void initializationTest()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("FFmpeg initialization test...");
        }
        try
        {
            initializationTest(
                    "org/gytheio/content/transform/ffmpeg/test.mp4",
                    FileMediaType.VIDEO_AVI.getMediaType(),
                    new VideoTransformationOptions());
        }
        catch (Exception e)
        {
            throw new GytheioRuntimeException("Could not initialize worker: " + e.getMessage(), e);
        }
    }
    
    /**
     * Determines if the source mimetype is supported by ffmpeg
     * 
     * @param mediaType the mimetype to check
     * @return Returns true if ffmpeg can handle the given mimetype format
     */
    public static boolean isSupportedSource(String mediaType)
    {
        return ((mediaType.startsWith(FileMediaType.PREFIX_VIDEO) && !(
                mediaType.equals("video/x-rad-screenplay") ||
                mediaType.equals("video/x-sgi-movie") ||
                mediaType.equals("video/mpeg2"))) ||
                (mediaType.startsWith(FileMediaType.PREFIX_AUDIO) && !(
                mediaType.equals("audio/vnd.adobe.soundbooth"))));
    }
    
    /**
     * Determines if FFmpeg can be made to support the given target mimetype.
     * 
     * @param mimetype the mimetype to check
     * @return Returns true if ffmpeg can handle the given mimetype format
     * @see #setUnsupportedMimetypes(String)
     */
    public static boolean isSupportedTarget(String mimetype)
    {
        return ((mimetype.startsWith(FileMediaType.PREFIX_VIDEO) && !(
                mimetype.equals("video/x-rad-screenplay") ||
                mimetype.equals("video/x-sgi-movie") ||
                mimetype.equals("video/mpeg2") ||
                mimetype.equals(FileMediaType.VIDEO_QUICKTIME.getMediaType()) ||
                mimetype.equals(FileMediaType.VIDEO_MP4.getMediaType()))) || // TODO: Move mp4, mov exclusion to properties when MM-108 is complete
                (mimetype.startsWith(FileMediaType.PREFIX_IMAGE) && !(
                mimetype.equals(FileMediaType.IMAGE_SVG.getMediaType()) ||
                mimetype.equals(FileMediaType.APPLICATION_PHOTOSHOP.getMediaType()) ||
                mimetype.equals(FileMediaType.IMG_DWG.getMediaType()) ||
                mimetype.equals("image/vnd.adobe.premiere") ||
                mimetype.equals("image/x-portable-anymap") ||
                mimetype.equals("image/x-xpixmap") ||
                mimetype.equals("image/x-dwt") ||
                mimetype.equals("image/cgm") || 
                mimetype.equals("image/ief"))) ||
                (mimetype.startsWith(FileMediaType.PREFIX_AUDIO) && !(
                mimetype.equals("audio/vnd.adobe.soundbooth") ||
                mimetype.equals(FileMediaType.AUDIO_MP4.getMediaType())))); // TODO: Move m4a exclusion to properties when MM-108 is complete
    }

    @Override
    public boolean isTransformable(List<String> sourceMediaTypes, String targetMediaType, TransformationOptions options)
    {
        if (!isAvailable())
        {
            return false;
        }
        
        // TODO: Other transform types, i.e.:
        //   - Layer multiple sources into one target
        if (sourceMediaTypes.size() > 1)
        {
            return false;
        }
        
        String sourceMediaType = sourceMediaTypes.get(0);
        
        if (logger.isDebugEnabled() && options != null)
        {
            logger.debug("checking support of " +
                    "sourceMediaType=" + sourceMediaType + " " + 
                    "targetMediaType=" + targetMediaType + " " +
                    options.getClass().getCanonicalName() + "=" + options.toString());
        }
        
        if (sourceMediaType.startsWith(FileMediaType.PREFIX_AUDIO) && 
                targetMediaType.startsWith(FileMediaType.PREFIX_IMAGE))
        {
            // Might be able to support audio to waveform image in the future, but for now...
            return false;
        }
        return (isSupportedSource(sourceMediaType) && isSupportedTarget(targetMediaType));
    }
    
    protected List<File> transformInternal(
            List<FileContentReferencePair> sourcePairs, 
            List<FileContentReferencePair> targetPairs,
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception
    {
        if (sourcePairs.size() > 1 || targetPairs.size() > 1)
        {
            throw new IllegalArgumentException("Only single source and target "
                    + "transformations are currently supported");
        }
        FileContentReferencePair targetPair = targetPairs.iterator().next();
        File targetFile = targetPair.getFile();
        singleTransformInternal(
                sourcePairs.iterator().next(), 
                targetPair, 
                options, progressReporter);
        return Arrays.asList(targetFile);
        // TODO: Other transform types, i.e.:
        //   - Stitch multiple sources into one target
        //   - Storyboard thumbnails: one source into multiple images
        //   - Merge multiple images into a movie?
        //   - Extract multiple audio tracks from a movie
    }
    
    protected void singleTransformInternal(
            FileContentReferencePair sourcePair, 
            FileContentReferencePair targetPair, 
            TransformationOptions options,
            ContentTransformerWorkerProgressReporter progressReporter) throws Exception
    {
        File sourceFile = sourcePair.getFile();
        File targetFile = targetPair.getFile();
        
        String sourceMimetype = sourcePair.getContentReference().getMediaType();
        String targetMimetype = targetPair.getContentReference().getMediaType();
        
        Map<String, String> properties = new HashMap<String, String>(5);
        // set properties
        String commandOptions = "";

        String sourceTemporalOptions = getSourceTemporalCommandOptions(sourceMimetype, targetMimetype, options);
        if (sourceTemporalOptions != null && !sourceTemporalOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + sourceTemporalOptions;
        }
        
        String formatOptions = getFormatCommandOptions(sourceMimetype, targetMimetype);
        if (formatOptions != null && !formatOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + formatOptions;
        }
        
        String exclusionOptions = getComponentExclusionCommandOptions(sourceMimetype, targetMimetype);
        if (exclusionOptions != null && !exclusionOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + exclusionOptions;
        }
        
        String resizeOptions = getTargetResizeCommandOptions(options);
        if (resizeOptions != null && !resizeOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + resizeOptions;
        }
        
        properties.put(VAR_OPTIONS, commandOptions.trim());
        properties.put(VAR_SOURCE, sourceFile.getAbsolutePath());
        properties.put(VAR_TARGET, targetFile.getAbsolutePath());

        // execute the statement
        RuntimeExec.ExecutionResult result = executer.execute(properties);
        if (result.getExitValue() != 0 && result.getStdErr() != null && result.getStdErr().length() > 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("full error: \n" + result.getStdErr());
            }
            throw new Exception("Failed to perform ffmpeg transformation: \n" + result);
        }
        // success
        if (logger.isDebugEnabled())
        {
            logger.debug("ffmpeg executed successfully: \n" + result);
        }
    }
    
    /**
     * Gets the ffmpeg command string for the transform options
     * provided
     * <p>
     * Note: The current implementation assumes a 4:3 aspect ratio in the source
     * and that the <code>imageResizeOptions</code> given signify max width and
     * heights.
     * <p>
     * TODO: Future implementations should examine the source for the aspect ratio to
     * correctly create the thumbnail.
     * 
     * @param imageResizeOptions image resize options
     * @return String the ffmpeg command options
     */
    protected String getTargetResizeCommandOptions(TransformationOptions options)
    {
        if (options == null || !(options instanceof ImageTransformationOptions))
        {
            return null;
        }
        ImageResizeOptions imageResizeOptions = ((ImageTransformationOptions) options).getResizeOptions();
        if (imageResizeOptions == null)
        {
            return null;
        }
        
        float aspectRatio = 1.3333f;

        StringBuilder builder = new StringBuilder(32);
        int width = 0;
        int height = 0;

        if (imageResizeOptions.getWidth() > 0 && imageResizeOptions.getHeight() > 0)
        {
            if (imageResizeOptions.getWidth() <= imageResizeOptions.getHeight())
            {
                width = imageResizeOptions.getWidth();
                height = Math.round(width * (1 / aspectRatio));
            }
            else if (imageResizeOptions.getWidth() > imageResizeOptions.getHeight())
            {
                height = imageResizeOptions.getHeight();
                width = Math.round(height * aspectRatio);
            }
        }

        if (width > 0 && height > 0)
        {
            if ((height % 2) != 0)
            {
                height = height - 1;
            }
            if ((width % 2) != 0)
            {
                width = width + 1;
            }
            builder.append(CMD_OPT_SIZE);
            builder.append(CMD_OPT_ASSIGNMENT);
            builder.append(width);
            builder.append("x");
            builder.append(height);
        }

        return builder.toString();
    }
    
    protected String getComponentExclusionCommandOptions(String sourceMimetype, String targetMimetype)
    {
        String commandOptions = "";
        if (disableVideo(sourceMimetype, targetMimetype))
        {
            commandOptions = commandOptions + CMD_OPT_DISABLE_VIDEO + CMD_OPT_DELIMITER;
        }
        if (disableAudio(sourceMimetype, targetMimetype))
        {
            commandOptions = commandOptions + CMD_OPT_DISABLE_AUDIO + CMD_OPT_DELIMITER;
        }
        if (disableSubtitles(sourceMimetype, targetMimetype))
        {
            commandOptions = commandOptions + CMD_OPT_DISABLE_SUBTITLES + CMD_OPT_DELIMITER;
        }
        return commandOptions.trim();
    }
    
    protected String getFormatCommandOptions(String sourceMimetype, String targetMimetype)
    {
        if (targetMimetype.startsWith(PREFIX_IMAGE))
        {
            return CMD_OPT_FORMAT + CMD_OPT_ASSIGNMENT + "image2";
        }
        if (targetMimetype.equals("audio/ogg"))
        {
            return CMD_OPT_FORMAT + CMD_OPT_ASSIGNMENT + "ogg";
        }
        return null;
    }

    /**
     * Gets the ffmpeg command string for the time-based video conversion transform options
     * provided
     * 
     * @param options time-based options
     * @return String the ffmpeg command options
     */
    protected String getSourceTemporalCommandOptions(String sourceMimetype, String targetMimetype, TransformationOptions options)
    {
        TemporalSourceOptions temporalSourceOptions = null;
        if (options != null)
        {
            temporalSourceOptions = options.getSourceOptions(TemporalSourceOptions.class);
        }
        String commandOptions = "";
        if (isSingleSourceFrameRangeRequired(sourceMimetype, targetMimetype))
        {
            commandOptions = commandOptions + CMD_OPT_PAIR_1_FRAME + CMD_OPT_DELIMITER;
        }
        else
        {
            if (temporalSourceOptions != null && temporalSourceOptions.getDuration() != null)
            {
                commandOptions = commandOptions + 
                        CMD_OPT_DURATION + CMD_OPT_ASSIGNMENT + temporalSourceOptions.getDuration() +
                        CMD_OPT_DELIMITER;
            }
        }
        if (temporalSourceOptions != null && temporalSourceOptions.getOffset() != null)
        {
            commandOptions = commandOptions + 
                    CMD_OPT_OFFSET + CMD_OPT_ASSIGNMENT + temporalSourceOptions.getOffset() +
                    CMD_OPT_DELIMITER;
        }
        return commandOptions.trim();
    }
    
    /**
     * Determines whether or not a single frame is required for the given source and target mimetypes.
     * 
     * @param sourceMimetype
     * @param targetMimetype
     * @return whether or not a page range must be specified for the transformer to read the target files
     */
    protected boolean isSingleSourceFrameRangeRequired(String sourceMimetype, String targetMimetype)
    {
        // Need a single frame if we're transforming from video to an image
        return (targetMimetype.startsWith(PREFIX_IMAGE));
    }
    
    /**
     * Determines whether or not video should be disabled for the given source and target mimetypes.
     * 
     * @param sourceMimetype
     * @param targetMimetype
     * @return whether or not to disable video in the output
     */
    protected boolean disableVideo(String sourceMimetype, String targetMimetype)
    {
        return (targetMimetype.startsWith(PREFIX_AUDIO));
    }
    
    /**
     * Determines whether or not audio should be disabled for the given source and target mimetypes.
     * 
     * @param sourceMimetype
     * @param targetMimetype
     * @return whether or not to disable audio in the output
     */
    protected boolean disableAudio(String sourceMimetype, String targetMimetype)
    {
        return (targetMimetype.startsWith(PREFIX_IMAGE));
    }
    
    /**
     * Determines whether or not subtitles should be disabled for the given source and target mimetypes.
     * 
     * @param sourceMimetype
     * @param targetMimetype
     * @return whether or not to disable subtitles in the output
     */
    protected boolean disableSubtitles(String sourceMimetype, String targetMimetype)
    {
        return (targetMimetype.startsWith(PREFIX_AUDIO));
    }
    
}
