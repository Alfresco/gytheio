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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.AbstractRuntimeExecContentTransformerWorker;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;
import org.gytheio.content.transform.options.AudioTransformationOptions;
import org.gytheio.content.transform.options.ImageResizeOptions;
import org.gytheio.content.transform.options.ImageTransformationOptions;
import org.gytheio.content.transform.options.TemporalSourceOptions;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.VideoTransformationOptions;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.util.exec.RuntimeExec;
import org.gytheio.util.exec.RuntimeExec.ExecutionResult;

/**
 * An FFmpeg command line implementation of a content hash node worker
 * 
 * @author Ray Gauss II
 */
public class FfmpegContentTransformerWorker extends AbstractRuntimeExecContentTransformerWorker
{
    private static final Log logger = LogFactory.getLog(FfmpegContentTransformerWorker.class);
    
    protected static final String CMD_OPT_ASSIGNMENT = " ";
    protected static final String CMD_OPT_PARAM_ASSIGNMENT = "=";
    protected static final String CMD_OPT_DELIMITER = " ";
    protected static final String CMD_OPT_NUM_VIDEO_FRAMES = "-vframes";
    protected static final String CMD_OPT_DISABLE_AUDIO = "-an";
    protected static final String CMD_OPT_DISABLE_VIDEO = "-vn";
    protected static final String CMD_OPT_DISABLE_SUBTITLES = "-sn";
    protected static final String CMD_OPT_VIDEO_CODEC_v0 = "-vcodec";
    protected static final String CMD_OPT_VIDEO_CODEC_v1 = "-c:v";
    protected static final String CMD_OPT_VIDEO_BITRATE_v0 = "-vb";
    protected static final String CMD_OPT_VIDEO_BITRATE_v1 = "-b:v";
    protected static final String CMD_OPT_VIDEO_PRESET = "-vpre";
    protected static final String CMD_OPT_AUDIO_CODEC_v0 = "-acodec";
    protected static final String CMD_OPT_AUDIO_CODEC_v1 = "-c:a";
    protected static final String CMD_OPT_AUDIO_BITRATE_v0 = "-ab";
    protected static final String CMD_OPT_AUDIO_BITRATE_v1 = "-b:a";
    protected static final String CMD_OPT_AUDIO_SAMPLING_RATE = "-ar";
    protected static final String CMD_OPT_AUDIO_CHANNELS = "-ac";
    protected static final String CMD_OPT_FORMAT = "-f";
    protected static final String CMD_OPT_DURATION = "-t";
    protected static final String CMD_OPT_OFFSET = "-ss";
    protected static final String CMD_OPT_SIZE = "-s";
    protected static final String CMD_OPT_SCALE = "-vf scale";
    protected static final String CMD_OPT_FRAME_RATE = "-r";
    protected static final String CMD_OPT_MOV_FLAGS = "-movflags";
    protected static final String CMD_OPT_MOV_FLAGS_FASTSTART = "+faststart";
    protected static final String CMD_OPT_ENABLE_EXPERIMENTAL = "-strict experimental";
    protected static final String CMD_OPT_PAIR_1_FRAME = CMD_OPT_NUM_VIDEO_FRAMES + CMD_OPT_DELIMITER + "1";
    
    protected static final String DEFAULT_VIDEO_PRESET = "default";
    
    public static final String VAR_OPTIONS = "options";

    /** offset variable name */
    public static final String VAR_OFFSET = "offset";
    
    /** duration variable name */
    public static final String VAR_DURATION = "duration";

    protected static final String DEFAULT_OFFSET = "00:00:00";
    
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
    protected void initializeFileDetailsExecuter()
    {
        if (fileDetailsExecuter == null)
        {
            fileDetailsExecuter = new RuntimeExec();
            Map<String, String[]> commandsAndArguments = new HashMap<>();
            commandsAndArguments.put(".*", new String[] { 
                ffmpegExe,
                "-i",
                "${source}"
            });
            fileDetailsExecuter.setCommandsAndArguments(commandsAndArguments);
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
    
    @Override
    protected void initializeVersionDetailsString()
    {
        super.initializeVersionDetailsString();
        if (versionDetailsExecuter == null)
        {
            versionDetailsExecuter = new RuntimeExec();
        }
        Map<String, String[]> checkCommandsAndArguments = new HashMap<String, String[]>();
        checkCommandsAndArguments.put(".*", new String[] { 
            ffmpegExe,
            "-h",
            "full"
        });
        versionDetailsExecuter.setCommandsAndArguments(checkCommandsAndArguments);
        String fullHelp = null;
        try
        {
            ExecutionResult result = this.versionDetailsExecuter.execute();
            String out = result.getStdOut().trim();
            if (!out.equals(""))
            {
                fullHelp = out;
            }
            fullHelp = result.getStdErr().trim();
        }
        catch (Throwable e)
        {
            logger.info(getClass().getSimpleName() + " could not get help: "
                    + (e.getMessage() != null ? e.getMessage() : ""));
        }
        if (fullHelp != null)
        {
            this.versionDetailsString = this.versionDetailsString + "\n\n" +
                    fullHelp;
        }
        checkCommandsAndArguments = new HashMap<String, String[]>();
        checkCommandsAndArguments.put(".*", new String[] { 
            ffmpegExe,
            "-formats"
        });
        versionDetailsExecuter.setCommandsAndArguments(checkCommandsAndArguments);
        String formats = null;
        try
        {
            ExecutionResult result = this.versionDetailsExecuter.execute();
            String out = result.getStdOut().trim();
            if (!out.equals(""))
            {
                formats = out;
            }
            formats = result.getStdErr().trim();
        }
        catch (Throwable e)
        {
            logger.info(getClass().getSimpleName() + " could not get formats: "
                    + (e.getMessage() != null ? e.getMessage() : ""));
        }
        if (formats != null)
        {
            this.versionDetailsString = this.versionDetailsString + "\n\n" +
                    formats;
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
                mimetype.equals("video/mpeg2"))) ||
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
                mimetype.equals("audio/vnd.adobe.soundbooth"))));
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
        
        if (logger.isTraceEnabled() && options != null)
        {
            logger.trace("checking support of " +
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
        
        String resizeOptions = getTargetResizeCommandOptions(options, sourceFile);
        if (resizeOptions != null && !resizeOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + resizeOptions;
        }
        
        String targetVideoOptions = getTargetVideoCommandOptions(options);
        if (targetVideoOptions != null && !targetVideoOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + targetVideoOptions;
        }
        
        String targetAudioOptions = getTargetAudioCommandOptions(options);
        if (targetAudioOptions != null && !targetAudioOptions.equals(""))
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + targetAudioOptions;
        }
        
        properties.put(VAR_OPTIONS, commandOptions.trim());
        properties.put(VAR_SOURCE, sourceFile.getAbsolutePath());
        properties.put(VAR_TARGET, targetFile.getAbsolutePath());

        // execute the statement
        RuntimeExec.ExecutionResult result = executer.execute(
                properties, 
                null, 
                new FfmpegInputStreamReaderThreadFactory(progressReporter, isVersion1()),
                -1);
        if (result.getExitValue() != 0 && result.getStdErr() != null && result.getStdErr().length() > 0)
        {
            throw new Exception("Failed to perform ffmpeg transformation: \n" + 
                    result.toString() + "\n\n-------- Full Error --------\n" +
                    result.getStdErr() + "\n----------------------------\n");
        }
        // success
        if (logger.isDebugEnabled())
        {
            logger.debug("ffmpeg executed successfully: \n" + result);
        }
    }
    
    protected String getFfmpegVersionNumber()
    {
        Pattern verisonNumPattern = Pattern.compile("(FFmpeg version |ffmpeg version )((\\d|\\.)+\\d)(.*)");
        try
        {
            Matcher versionNumMatcher = verisonNumPattern.matcher(this.versionDetailsString);
            if (versionNumMatcher.find())
            {
                return versionNumMatcher.group(2);
            }
        }
        catch (Throwable e)
        {
            logger.info("Could not determine version of FFmpeg: " + e.getMessage());
        }
        return null;
    }
    
    protected boolean isFilterSupported()
    {
        String ffmpegVersionNumber = getFfmpegVersionNumber();
        if (ffmpegVersionNumber == null)
        {
            return false;
        }
        DefaultArtifactVersion filtersSupportedVersion = new DefaultArtifactVersion("0.7");
        DefaultArtifactVersion thisVersion = new DefaultArtifactVersion(ffmpegVersionNumber);
        return thisVersion.compareTo(filtersSupportedVersion) >= 0;
    }
    
    public boolean isVersion1()
    {
        String ffmpegVersionNumber = getFfmpegVersionNumber();
        if (ffmpegVersionNumber == null)
        {
            return false;
        }
        DefaultArtifactVersion version1 = new DefaultArtifactVersion("1.0");
        DefaultArtifactVersion thisVersion = new DefaultArtifactVersion(ffmpegVersionNumber);
        return thisVersion.compareTo(version1) >= 0;
    }
    
    protected String getResolution(String details)
    {
        if (details == null)
        {
            return null;
        }
        String[] segments = details.split(", ");
        for (String segment : segments)
        {
            if (segment.matches("[0-9]+x[0-9]+( \\[.*\\])?"))
            {
                if (segment.contains(" "))
                {
                    return segment.split(" ")[0];
                }
                return segment;
            }
        }
        return null;
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
     * @param options transformation options
     * @param sourceFile
     * @return String the ffmpeg command options
     */
    protected String getTargetResizeCommandOptions(
            TransformationOptions options, File sourceFile)
    {
        if (options == null)
        {
            return null;
        }
        ImageResizeOptions imageResizeOptions = null;
        if (options instanceof ImageTransformationOptions)
        {
            imageResizeOptions = ((ImageTransformationOptions) options).getResizeOptions();
        }
        if (options instanceof VideoTransformationOptions)
        {
            imageResizeOptions = ((VideoTransformationOptions) options).getResizeOptions();
        }
        if (imageResizeOptions == null)
        {
            return null;
        }
        
        float aspectRatio = 1.3333f; // default
        try
        {
            String sourceDetails = getDetails(sourceFile);
            String sourceResolution = getResolution(sourceDetails);
            if (sourceResolution != null)
            {
                Integer sourceWidth = new Integer(sourceResolution.split("x")[0]);
                Integer sourceHeight = new Integer(sourceResolution.split("x")[1]);
                aspectRatio = sourceWidth.floatValue() / sourceHeight.floatValue();
            }
        }
        catch (Exception e)
        {
            logger.warn("Could not get file details: " + e.getMessage());
        }
        
        StringBuilder builder = new StringBuilder(32);
        int width = imageResizeOptions.getWidth();
        int height = imageResizeOptions.getHeight();

        if (imageResizeOptions.isMaintainAspectRatio())
        {
            // Could use ffmpeg's scale features here but this seems easier
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
            else if (!isFilterSupported())
            {
                if (imageResizeOptions.getHeight() < 0)
                {
                    width = imageResizeOptions.getWidth();
                    height = Math.round(width * (1 / aspectRatio));
                }
                else
                {
                    height = imageResizeOptions.getHeight();
                    width = Math.round(height * aspectRatio);
                }
            }
            if (height > 0 && (height % 2) != 0)
            {
                height = height - 1;
            }
            if (width > 0 && (width % 2) != 0)
            {
                width = width + 1;
            }
        }

        if (isFilterSupported())
        {
            builder.append(CMD_OPT_SCALE);
            builder.append(CMD_OPT_PARAM_ASSIGNMENT);
            builder.append(width);
            builder.append(":");
            builder.append(height);
        }
        else
        {
            builder.append(CMD_OPT_SIZE);
            builder.append(CMD_OPT_ASSIGNMENT);
            builder.append(width);
            builder.append("x");
            builder.append(height);
        }
        
        return builder.toString();
    }
    
    protected String getFfmpegVideoCodec(String gytheioVideoCodec)
    {
        if (versionDetailsString == null)
        {
            return null;
        }
        if (VideoTransformationOptions.VIDEO_CODEC_H264.equals(gytheioVideoCodec))
        {
            return "libx264";
        }
        if (VideoTransformationOptions.VIDEO_CODEC_MPEG4.equals(gytheioVideoCodec))
        {
            return "mpeg4";
        }
        if (VideoTransformationOptions.VIDEO_CODEC_THEORA.equals(gytheioVideoCodec))
        {
            return "libtheora";
        }
        if (VideoTransformationOptions.VIDEO_CODEC_VP8.equals(gytheioVideoCodec))
        {
            return "libvpx";
        }
        if (VideoTransformationOptions.VIDEO_CODEC_WMV.equals(gytheioVideoCodec))
        {
            return null;
        }
        return null;
    }
    
    protected String getCmdOptVideoBitrate()
    {
        return (isVersion1() ? CMD_OPT_VIDEO_BITRATE_v1 : CMD_OPT_VIDEO_BITRATE_v0);
    }
    
    protected String getCmdOptVideoCodec()
    {
        return (isVersion1() ? CMD_OPT_VIDEO_CODEC_v1 : CMD_OPT_VIDEO_CODEC_v0);
    }
    
    protected String getCmdOptAudioBitrate()
    {
        return (isVersion1() ? CMD_OPT_AUDIO_BITRATE_v1 : CMD_OPT_AUDIO_BITRATE_v0);
    }
    
    protected String getCmdOptAudioCodec()
    {
        return (isVersion1() ? CMD_OPT_AUDIO_CODEC_v1 : CMD_OPT_AUDIO_CODEC_v0);
    }
    
    protected String getTargetVideoCommandOptions(TransformationOptions options)
    {
        String commandOptions = "";
        if (options == null)
        {
            return null;
        }
        if (!(options instanceof VideoTransformationOptions))
        {
            return null;
        }
        Float frameRate = ((VideoTransformationOptions) options).getTargetVideoFrameRate();
        Long videoBitrate = ((VideoTransformationOptions) options).getTargetVideoBitrate();
        String videoCodec = ((VideoTransformationOptions) options).getTargetVideoCodec();
        
        if (frameRate != null)
        {
            commandOptions = commandOptions + 
                    CMD_OPT_FRAME_RATE + CMD_OPT_DELIMITER + frameRate;
        }
        if (videoBitrate != null)
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + 
                    getCmdOptVideoBitrate() + CMD_OPT_DELIMITER + (videoBitrate / 1000) + "k";
        }
        if (videoCodec != null)
        {
            commandOptions = commandOptions.trim() + CMD_OPT_DELIMITER + 
                    getCmdOptVideoCodec() + CMD_OPT_DELIMITER + getFfmpegVideoCodec(videoCodec);
            if (!isVersion1() && videoCodec.equals(VideoTransformationOptions.VIDEO_CODEC_H264))
            {
                commandOptions = commandOptions.trim() + CMD_OPT_DELIMITER + 
                        CMD_OPT_VIDEO_PRESET + CMD_OPT_DELIMITER + DEFAULT_VIDEO_PRESET;
            }
        }
        return commandOptions.trim();
    }
    
    protected String getFfmpegAudioCodec(String gytheioAudioCodec)
    {
        if (versionDetailsString == null)
        {
            return null;
        }
        if (AudioTransformationOptions.AUDIO_CODEC_AAC.equals(gytheioAudioCodec))
        {
            if (versionDetailsString.contains("libfdk-aac"))
            {
                return "libfdk_aac";
            }
            if (versionDetailsString.contains("libfaac"))
            {
                return "libfaac";
            }
            if (versionDetailsString.contains("libvo-aacenc"))
            {
                return "libvo_aacenc";
            }
            if (versionDetailsString.contains("aac"))
            {
                return "aac" + CMD_OPT_DELIMITER + CMD_OPT_ENABLE_EXPERIMENTAL;
            }
            
        }
        if (AudioTransformationOptions.AUDIO_CODEC_MP3.equals(gytheioAudioCodec))
        {
            return "libmp3lame";
        }
        if (AudioTransformationOptions.AUDIO_CODEC_VORBIS.equals(gytheioAudioCodec))
        {
            return "libvorbis";
        }
        if (AudioTransformationOptions.AUDIO_CODEC_WMA.equals(gytheioAudioCodec))
        {
            return "wmav2";
        }
        return null;
    }
    
    protected String getTargetAudioCommandOptions(TransformationOptions options)
    {
        String commandOptions = "";
        if (options == null)
        {
            return null;
        }
        if (!(options instanceof AudioTransformationOptions))
        {
            return null;
        }
        Long audioBitrate = ((AudioTransformationOptions) options).getTargetAudioBitrate();
        Integer audioSamplingRate = ((AudioTransformationOptions) options).getTargetAudioSamplingRate();
        Integer audioChannels = ((AudioTransformationOptions) options).getTargetAudioChannels();
        String audioCodec = ((AudioTransformationOptions) options).getTargetAudioCodec();
        boolean fastStartEnabled = ((AudioTransformationOptions) options).getTargetFastStartEnabled();
        
        if (audioBitrate != null)
        {
            commandOptions = commandOptions + 
                    getCmdOptAudioBitrate() + CMD_OPT_DELIMITER + (audioBitrate / 1000) + "k";
        }
        if (audioSamplingRate != null)
        {
            commandOptions = commandOptions + CMD_OPT_DELIMITER + 
                    CMD_OPT_AUDIO_SAMPLING_RATE + CMD_OPT_DELIMITER + audioSamplingRate;
        }
        if (audioChannels != null)
        {
            commandOptions = commandOptions.trim() + CMD_OPT_DELIMITER + 
                    CMD_OPT_AUDIO_CHANNELS + CMD_OPT_DELIMITER + audioChannels;
        }
        if (audioCodec != null)
        {
            commandOptions = commandOptions.trim() + CMD_OPT_DELIMITER + 
                    getCmdOptAudioCodec() + CMD_OPT_DELIMITER + getFfmpegAudioCodec(audioCodec);
        }
        if (fastStartEnabled)
        {
            if (versionDetailsString != null && versionDetailsString.contains("faststart"))
            {
                commandOptions = commandOptions.trim() + CMD_OPT_DELIMITER + 
                        CMD_OPT_MOV_FLAGS + CMD_OPT_DELIMITER + CMD_OPT_MOV_FLAGS_FASTSTART;
            }
        }
        return commandOptions.trim();
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
        if (targetMimetype.startsWith(FileMediaType.PREFIX_IMAGE))
        {
            return CMD_OPT_FORMAT + CMD_OPT_DELIMITER + "image2";
        }
        if (targetMimetype.equals("audio/ogg"))
        {
            return CMD_OPT_FORMAT + CMD_OPT_DELIMITER + "ogg";
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
                        CMD_OPT_DURATION + CMD_OPT_DELIMITER + temporalSourceOptions.getDuration() +
                        CMD_OPT_DELIMITER;
            }
        }
        if (temporalSourceOptions != null && temporalSourceOptions.getOffset() != null)
        {
            commandOptions = commandOptions + 
                    CMD_OPT_OFFSET + CMD_OPT_DELIMITER + temporalSourceOptions.getOffset() +
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
        return (targetMimetype.startsWith(FileMediaType.PREFIX_IMAGE));
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
        return (targetMimetype.startsWith(FileMediaType.PREFIX_AUDIO));
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
        return (targetMimetype.startsWith(FileMediaType.PREFIX_IMAGE));
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
        return (targetMimetype.startsWith(FileMediaType.PREFIX_AUDIO));
    }
    
}
