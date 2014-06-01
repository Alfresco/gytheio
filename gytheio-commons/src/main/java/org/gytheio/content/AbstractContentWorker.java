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
package org.gytheio.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.gytheio.content.handler.ContentReferenceHandler;

/**
 * Base implementation of a content worker with a <code>sourceContentReferenceHandler</code>
 * field.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentWorker implements ContentWorker
{
    private static final Log logger = LogFactory.getLog(AbstractContentWorker.class);
    
    protected static final String FRAMEWORK_PROPERTY_NAME = "name";
    protected static final String FRAMEWORK_PROPERTY_VERSION = "version";

    protected ContentReferenceHandler sourceContentReferenceHandler;
    private boolean isAvailable;
    private Properties properties;
    protected String versionString;
    protected String versionDetailsString;
    
    /**
     * Sets the content reference handler to be used for retrieving
     * the source content to be worked on.
     * 
     * @param sourceContentReferenceHandler
     */
    public void setSourceContentReferenceHandler(ContentReferenceHandler sourceContentReferenceHandler)
    {
        this.sourceContentReferenceHandler = sourceContentReferenceHandler;
    }
    
    /**
     * Performs any initialization needed after content reference handlers are set
     */
    public void initialize()
    {
        loadProperties();
        initializeVersionString();
        initializeVersionDetailsString();
    }
    
    @Override
    public boolean isAvailable()
    {
        return isAvailable;
    }
    
    protected void setIsAvailable(boolean isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    protected Properties getProperties()
    {
        return properties;
    }
    
    protected void loadProperties()
    {
        String propertiesFilePath = "/" + 
                this.getClass().getCanonicalName().replaceAll("\\.", "/") + ".properties";
        InputStream inputStream = this.getClass().getResourceAsStream(propertiesFilePath);
        if (inputStream == null)
        {
            logger.debug(propertiesFilePath + " not found");
            return;
        }
        try
        {
            properties = new Properties();
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
            properties = null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    protected void initializeVersionString()
    {
        if (getProperties() == null)
        {
            versionString = this.getClass().getSimpleName();
        }
        else
        {
            versionString = getProperties().getProperty(FRAMEWORK_PROPERTY_NAME) + " " +
                getProperties().getProperty(FRAMEWORK_PROPERTY_VERSION);
        }
    }
    
    @Override
    public String getVersionString()
    {
        return versionString;
    }
    
    protected void initializeVersionDetailsString()
    {
        versionDetailsString = "JVM: " + System.getProperty("java.version");
    }
    
    @Override
    public String getVersionDetailsString()
    {
        return versionDetailsString;
    }
    
}
