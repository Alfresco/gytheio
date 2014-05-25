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
package org.gytheio.content.mediatype;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.gytheio.error.GytheioRuntimeException;

public class FileMediaTypeServiceImpl implements FileMediaTypeService
{

    private static final Log logger = LogFactory.getLog(FileMediaTypeServiceImpl.class);

    protected TikaConfig tikaConfig;
    protected Tika tika;
    
    public FileMediaTypeServiceImpl(TikaConfig tikaConfig)
    {
        this.tikaConfig = tikaConfig;
        init();
    }

    public void init()
    {
        if (tikaConfig == null)
        {
            logger.debug("Initializing with default Tika config");
            tikaConfig = TikaConfig.getDefaultConfig();
        }
        if (tika == null)
        {
            tika = new Tika(tikaConfig);
        }
    }

    @Override
    public String getExtension(String mimetype)
    {
        try
        {
            MimeType tikaMimeType = tikaConfig.getMimeRepository().forName(mimetype);
            if (tikaMimeType != null)
            {
                String tikaExtension = tikaMimeType.getExtension();
                if (tikaExtension.startsWith("."))
                {
                    tikaExtension = tikaExtension.substring(1, tikaExtension.length());
                }
                return tikaExtension;
            }
        }
        catch (MimeTypeException e)
        {
            throw new GytheioRuntimeException("Could not get extension for mimetype", e);
        }
        
        return null;
    }

    @Override
    public String getMediaType(String extension)
    {
        return tika.detect("*." + extension);
    }
    
    @Override
    public String getMediaTypeByName(File file)
    {
        return tika.detect(file.getName());
    }
    
    public MimeType getTikaMimeType(String mimetype) throws MimeTypeException
    {
        return tikaConfig.getMimeRepository().forName(mimetype);
    }

}
