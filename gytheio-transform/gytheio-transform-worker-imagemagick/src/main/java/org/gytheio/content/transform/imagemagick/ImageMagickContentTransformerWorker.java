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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.content.transform.magick.ImageResizeOptions;
import org.alfresco.service.cmr.repository.CropSourceOptions;
import org.alfresco.service.cmr.repository.PagedSourceOptions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.ContentIOException;
import org.gytheio.content.ContentReference;
import org.gytheio.content.file.TempFileProvider;
import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.AbstractFileContentTransformerWorker;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;
import org.gytheio.content.transform.options.ImageTransformationOptions;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.content.transform.options.TransformationOptionsImpl;
import org.gytheio.error.GytheioRuntimeException;
import org.gytheio.util.exec.RuntimeExec;
import org.gytheio.util.exec.RuntimeExec.ExecutionResult;

/**
 * Executes a statement to implement 
 * 
 * @author Derek Hulley
 */
public class ImageMagickContentTransformerWorker extends AbstractFileContentTransformerWorker
{
    /** options variable name */
    private static final String KEY_OPTIONS = "options";
    /** source variable name */
    private static final String VAR_SOURCE = "source";
    /** target variable name */
    private static final String VAR_TARGET = "target";
    
    /** the prefix for mimetypes supported by the transformer */
    public static final String MIMETYPE_IMAGE_PREFIX = "image/";
    
    private static final Log logger = LogFactory.getLog(ImageMagickContentTransformerWorker.class);

    /** the system command executer */
    private RuntimeExec executer;
    
    private String imgExe = "convert";

    /** the check command executer */
    private RuntimeExec checkCommand;
    
    /** the output from the check command */
    private String versionString;
    
    private boolean available;
    
    /**
     * Default constructor
     */
    public ImageMagickContentTransformerWorker()
    {
        // Intentionally empty
    }
    
    /**
     * Set the runtime command executer that must be executed in order to run
     * <b>ImageMagick</b>.  Whether or not this is the full path to the convertCommand
     * or just the convertCommand itself depends the environment setup.
     * <p>
     * The command must contain the variables <code>${source}</code> and
     * <code>${target}</code>, which will be replaced by the names of the file to
     * be transformed and the name of the output file respectively.
     * <pre>
     *    convert ${source} ${target}
     * </pre>
     *  
     * @param executer the system command executer
     */
    public void setExecuter(RuntimeExec executer)
    {
        executer.setProcessProperty(
                "MAGICK_TMPDIR", TempFileProvider.getTempDir().getAbsolutePath());
        this.executer = executer;
    }
    

    /**
     * Sets the command that must be executed in order to retrieve version information from the converting executable
     * and thus test that the executable itself is present.
     * 
     * @param checkCommand
     *            command executer to retrieve version information
     */
    public void setCheckCommand(RuntimeExec checkCommand)
    {
        this.checkCommand = checkCommand;
    }
    
    /**
     * Gets the version string captured from the check command.
     * 
     * @return the version string
     */
    public String getVersionString()
    {
        return this.versionString;
    }
    
    /**
     * @return Returns true if the transformer is functioning otherwise false
     */
    public boolean isAvailable()
    {
        return available;
    }

    /**
     * Make the transformer available
     * @param available
     */
    protected void setAvailable(boolean available)
    {
        this.available = available;
    }

    
    public void initConversion()
    {
        try
        {
            // load, into memory the sample gif
            String resourcePath = "org/alfresco/repo/content/transform/magick/alfresco.gif";
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (imageStream == null)
            {
                throw new GytheioRuntimeException("Sample image not found: " + resourcePath);
            }
            // dump to a temp target reference (we may only be able to write to the target handler)
            ContentReference sourceReference = targetContentReferenceHandler.createContentReference(
                    getClass().getSimpleName() + "_init_source_.gif", 
                    FileMediaType.IMAGE_GIF.getMediaType());
            targetContentReferenceHandler.putInputStream(imageStream, sourceReference);
            
            // create the output file
            ContentReference targetReference = targetContentReferenceHandler.createContentReference(
                    getClass().getSimpleName() + "_init_target_.png",
                    FileMediaType.IMAGE_PNG.getMediaType());
            
            // execute it
            transform(
                    Arrays.asList(sourceReference), 
                    Arrays.asList(targetReference), 
                    new TransformationOptionsImpl(),
                    null);
            
            // check that the file exists
            
            if (targetReference.getSize() == null || targetReference.getSize() == 0)
            {
                throw new Exception("Image conversion failed: \n" +
                        "   from: " + sourceReference + "\n" +
                        "   to: " + targetReference);
            }
            // we can be sure that it works
            setAvailable(true);            
        }
        catch (Throwable e)
        {
            logger.error(
                    getClass().getSimpleName() + " not available: " +
                    (e.getMessage() != null ? e.getMessage() : ""));
            // debug so that we can trace the issue if required
            logger.debug(e);
        }
    }
    
    /**
     * Checks for the JMagick and ImageMagick dependencies, using the common
     * {@link #transformInternal(File, File) transformation method} to check
     * that the sample image can be converted. 
     */
    @Override
    public void initialize()
    {
        super.initialize();
        if (executer == null)
        {
            if (System.getProperty("img.exe") != null)
            {
                imgExe = System.getProperty("img.exe");
            }
            executer = new RuntimeExec();
            Map<String, String[]> commandsAndArguments = new HashMap<>();
            commandsAndArguments.put(".*", new String[] { 
                imgExe,
                "${source}",
                "SPLIT:${options}",
                "${target}"
            });
            executer.setCommandsAndArguments(commandsAndArguments);
        }
        if (isAvailable())
        {
            try
            {
                // On some platforms / versions, the -version command seems to return an error code whilst still
                // returning output, so let's not worry about the exit code!
                ExecutionResult result = this.checkCommand.execute();
                this.versionString = result.getStdOut().trim();
            }
            catch (Throwable e)
            {
                setAvailable(false);
                logger.error(getClass().getSimpleName() + " not available: "
                        + (e.getMessage() != null ? e.getMessage() : ""));
                // debug so that we can trace the issue if required
                logger.debug(e);
            }
            
        }
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
        //   - Layer multiple sources into one target
        //   - PDF/TIFF pages: one source into multiple images
    }
    
    /**
     * Transform the image content from the source file to the target file
     */
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
        if (options instanceof ImageTransformationOptions)
        {
            ImageTransformationOptions imageOptions = (ImageTransformationOptions)options;
            CropSourceOptions cropOptions = imageOptions.getSourceOptions(CropSourceOptions.class);
            ImageResizeOptions resizeOptions = imageOptions.getResizeOptions();
            String commandOptions = imageOptions.getCommandOptions();
            if (commandOptions == null)
            {
                commandOptions = "";
            }
            if (imageOptions.isAutoOrient())
            {
                commandOptions = commandOptions + " -auto-orient"; 
            }
            if (cropOptions != null)
            {
                commandOptions = commandOptions + " " + getImageCropCommandOptions(cropOptions);
            }
            if (resizeOptions != null)
            {
                commandOptions = commandOptions + " " + getImageResizeCommandOptions(resizeOptions);
            }
            properties.put(KEY_OPTIONS, commandOptions);
        }
        properties.put(VAR_SOURCE, sourceFile.getAbsolutePath() + 
                getSourcePageRange(options, sourceMimetype, targetMimetype));
        properties.put(VAR_TARGET, targetFile.getAbsolutePath());
        
        // execute the statement
        long timeoutMs = options.getTimeoutMs();
        RuntimeExec.ExecutionResult result = executer.execute(properties, timeoutMs);
        if (result.getExitValue() != 0 && result.getStdErr() != null && result.getStdErr().length() > 0)
        {
            throw new ContentIOException("Failed to perform ImageMagick transformation: \n" + result);
        }
        // success
        if (logger.isDebugEnabled())
        {
            logger.debug("ImageMagic executed successfully: \n" + executer);
        }
    }
    
    /**
     * Gets the imagemagick command string for the image crop options provided
     * 
     * @param imageResizeOptions    image resize options
     * @return String               the imagemagick command options
     */
    private String getImageCropCommandOptions(CropSourceOptions cropOptions)
    {
        StringBuilder builder = new StringBuilder(32);
        String gravity = cropOptions.getGravity();
        if(gravity!=null)
        {
            builder.append("-gravity ");
            builder.append(gravity);
            builder.append(" ");
        }
        builder.append("-crop ");
        int width = cropOptions.getWidth();
        if (width > -1)
        {
            builder.append(width);
        }
        
        int height = cropOptions.getHeight();
        if (height > -1)
        {
            builder.append("x");
            builder.append(height);
        }
        
        if (cropOptions.isPercentageCrop())
        {
            builder.append("%");
        }
        appendOffset(builder, cropOptions.getXOffset());
        appendOffset(builder, cropOptions.getYOffset());
        builder.append(" +repage");
        return builder.toString();
    }

    /**
     * @param builder
     * @param xOffset
     */
    private void appendOffset(StringBuilder builder, int xOffset)
    {
        if(xOffset>=0)
        {
            builder.append("+");
        }
        builder.append(xOffset);
    }

    /**
     * Gets the imagemagick command string for the image resize options provided
     * 
     * @param imageResizeOptions    image resize options
     * @return String               the imagemagick command options
     */
    private String getImageResizeCommandOptions(ImageResizeOptions imageResizeOptions)
    {
        StringBuilder builder = new StringBuilder(32);
        
        // These are ImageMagick options. See http://www.imagemagick.org/script/command-line-processing.php#geometry for details.
        if (imageResizeOptions.isResizeToThumbnail() == true)
        {
            builder.append("-thumbnail ");
        }
        else
        {
            builder.append("-resize ");
        }
        
        if (imageResizeOptions.getWidth() > -1)
        {
            builder.append(imageResizeOptions.getWidth());
        }
        
        if (imageResizeOptions.getHeight() > -1)
        {
            builder.append("x");
            builder.append(imageResizeOptions.getHeight());
        }
        
        if (imageResizeOptions.isPercentResize() == true)
        {
            builder.append("%");
        }
        // ALF-7308. Disallow the enlargement of small images e.g. within imgpreview thumbnail.
        if (!imageResizeOptions.getAllowEnlargement())
        {
            builder.append(">");
        }
        
        if (imageResizeOptions.isMaintainAspectRatio() == false)
        {
            builder.append("!");
        }
        
        return builder.toString();
    }
    
    /**
     * Determines whether or not a single page range is required for the given source and target mimetypes.
     * 
     * @param sourceMimetype
     * @param targetMimetype
     * @return whether or not a page range must be specified for the transformer to read the target files
     */
    private boolean isSingleSourcePageRangeRequired(String sourceMimetype, String targetMimetype)
    {
        // Need a page source if we're transforming from PDF or TIFF to an image other than TIFF
        // or from PSD
        return ((sourceMimetype.equals(FileMediaType.PDF.getMediaType()) || 
                sourceMimetype.equals(FileMediaType.IMAGE_TIFF.getMediaType())) && 
                ((!targetMimetype.equals(FileMediaType.IMAGE_TIFF.getMediaType()) 
                && targetMimetype.contains(FileMediaType.PREFIX_IMAGE)) ||
                targetMimetype.equals(FileMediaType.APPLICATION_PHOTOSHOP.getMediaType()) ||
                targetMimetype.equals(FileMediaType.APPLICATION_EPS.getMediaType())) ||
                sourceMimetype.equals(FileMediaType.APPLICATION_PHOTOSHOP.getMediaType()));
    }
    
    /**
     * Gets the page range from the source to use in the command line.
     * 
     * @param options the transformation options
     * @param sourceMimetype the source mimetype
     * @param targetMimetype the target mimetype
     * @return the source page range for the command line
     */
    private String getSourcePageRange(TransformationOptions options, String sourceMimetype, String targetMimetype)
    {
        // Check for PagedContentSourceOptions in the options
        if (options instanceof ImageTransformationOptions)
        {
            ImageTransformationOptions imageOptions = (ImageTransformationOptions) options;
            PagedSourceOptions pagedSourceOptions = imageOptions.getSourceOptions(PagedSourceOptions.class);
            if (pagedSourceOptions != null)
            {
                if (pagedSourceOptions.getStartPageNumber() != null && 
                        pagedSourceOptions.getEndPageNumber() != null)
                {
                    if (pagedSourceOptions.getStartPageNumber().equals(pagedSourceOptions.getEndPageNumber()))
                    {
                        return "[" + (pagedSourceOptions.getStartPageNumber() - 1) + "]";
                    }
                    else
                    {
                        if (isSingleSourcePageRangeRequired(sourceMimetype, targetMimetype))
                        {
                            throw new GytheioRuntimeException(
                                    "A single page is required for targets of type " + targetMimetype);
                        }
                        return "[" + (pagedSourceOptions.getStartPageNumber() - 1) + 
                                "-" + (pagedSourceOptions.getEndPageNumber() - 1) + "]";
                    }
                }
                else
                {
                    // TODO specified start to end of doc and start of doc to specified end not yet supported
                    // Just grab a single page specified by either start or end
                    if (pagedSourceOptions.getStartPageNumber() != null)
                        return "[" + (pagedSourceOptions.getStartPageNumber() - 1) + "]";
                    if (pagedSourceOptions.getEndPageNumber() != null)
                        return "[" + (pagedSourceOptions.getEndPageNumber() - 1) + "]";
                }
            }
        }
        if (options.getPageLimit() == 1 || isSingleSourcePageRangeRequired(sourceMimetype, targetMimetype))
        {
            return "[0]";
        }
        else
        {
            return "";
        }
    }
}
