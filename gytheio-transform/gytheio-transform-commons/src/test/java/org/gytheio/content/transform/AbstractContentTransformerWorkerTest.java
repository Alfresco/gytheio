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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.ContentReference;
import org.gytheio.content.mediatype.FileMediaTypeService;
import org.gytheio.content.mediatype.FileMediaTypeServiceImpl;
import org.gytheio.content.transform.ContentTransformerWorkerProgressReporter;

/**
 * @see org.alfresco.repo.content.transform.ffmpeg.FfmpegContentTransformerWorker
 * 
 * @author Derek Hulley
 * @author Ray Gauss II
 */
public abstract class AbstractContentTransformerWorkerTest
{
    private static final Log logger = LogFactory.getLog(AbstractContentTransformerWorkerTest.class);

    protected FileMediaTypeService mediaTypeService = new FileMediaTypeServiceImpl(null);
    
    /**
     * For the given mime type, returns one or more quick*
     *  files to be tested.
     * By default this is just quick + the default extension.
     * However, you can override this if you need special
     *  rules, eg quickOld.foo, quickMid.foo and quickNew.foo
     *  for differing versions of the file format.
     */
    protected String[] getQuickFilenames(String sourceMimetype) {
       String sourceExtension = mediaTypeService.getExtension(sourceMimetype);
       return new String[] {
             "quick." + sourceExtension
       };
    }
    
    /**
     * Helper method to load one of the "The quick brown fox" files from the
     * classpath.
     * 
     * @param the file required, eg <b>quick.txt</b>
     * @return Returns a test resource loaded from the classpath or <tt>null</tt> if
     *      no resource could be found.
     * @throws IOException
     */
    public static ContentReference getNamedQuickTestFileReference(String quickname, String mediaType) throws IOException
    {
        URL url = AbstractContentTransformerWorkerTest.class.getClassLoader().getResource("quick/" + quickname);
        if (url == null)
        {
            throw new FileNotFoundException(quickname + " not found");
        }
        // TODO Consider introduction of Spring core dependency for better resolution in jars, etc.
        return new ContentReference(url.toString(), mediaType);
    }
    
    /**
     * Helper method to load one of the "The quick brown fox" files from the
     * classpath.
     * 
     * @param the file required, eg <b>quick.txt</b>
     * @return Returns a test resource loaded from the classpath or <tt>null</tt> if
     *      no resource could be found.
     * @throws IOException
     */
    public static File loadNamedQuickTestFile(String quickname) throws IOException
    {
        URL url = AbstractContentTransformerWorkerTest.class.getClassLoader().getResource("quick/" + quickname);
        if (url == null)
        {
            return null;
        }
        try
        {
            return new File(new URI(url.toString()));
        }
        catch (URISyntaxException e)
        {
            throw new IOException(e);
        }
        // TODO Consider introduction of Spring core dependency
        // return ResourceUtils.getFile(url);
    }
    
    /**
     * Helper method to load one of the "The quick brown fox" files from the
     * classpath.
     * 
     * @param the file extension required, eg <b>txt</b> for the file quick.txt
     * @return Returns a test resource loaded from the classpath or <tt>null</tt> if
     *      no resource could be found.
     * @throws IOException
     */
    public static File loadQuickTestFile(String extension) throws IOException
    {
       return loadNamedQuickTestFile("quick."+extension);
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
