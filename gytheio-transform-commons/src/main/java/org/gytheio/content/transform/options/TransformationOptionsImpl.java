/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.content.transform.options;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.service.cmr.repository.TransformationOptionLimits;
import org.alfresco.service.cmr.repository.TransformationSourceOptions;

/**
 * Concrete implementation of transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationOptionsImpl implements TransformationOptions
{
    /** Source options based on its mimetype */
    private Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap;
    
    private Map<String, Serializable> additionalOptions;
    
    /** Time, KBytes and page limits */
    private TransformationOptionLimits limits = new TransformationOptionLimits();

    public Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> getSourceOptionsMap()
    {
        return sourceOptionsMap;
    }

    public void setSourceOptionsMap(
            Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap)
    {
        this.sourceOptionsMap = sourceOptionsMap;
    }
    
    /**
     * Sets the list of source options further describing how the source should
     * be transformed based on its mimetype.
     * 
     * @param sourceOptionsList the source options list
     */
    public void setSourceOptionsList(Collection<TransformationSourceOptions> sourceOptionsList)
    {
        if (sourceOptionsList != null)
        {
            for (TransformationSourceOptions sourceOptions : sourceOptionsList)
            {
                addSourceOptions(sourceOptions);
            }
        }
    }

    /**
     * Gets the appropriate source options for the given mimetype if available.
     * 
     * @param sourceMimetype
     * @return the source options for the mimetype
     */
    @SuppressWarnings("unchecked")
    public <T extends TransformationSourceOptions> T getSourceOptions(Class<T> clazz)
    {
        if (sourceOptionsMap == null)
            return null;
        return (T) sourceOptionsMap.get(clazz);
    }
    
    /**
     * Adds the given sourceOptions to the sourceOptionsMap.
     * <p>
     * Note that if source options of the same class already exists a new
     * merged source options object is added.
     * 
     * @param sourceOptions
     */
    public void addSourceOptions(TransformationSourceOptions sourceOptions)
    {
        if (sourceOptionsMap == null)
        {
            sourceOptionsMap = new HashMap<Class<? extends TransformationSourceOptions>, TransformationSourceOptions>(1);
        }
        TransformationSourceOptions newOptions = sourceOptions;
//        TransformationSourceOptions existingOptions = sourceOptionsMap.get(sourceOptions.getClass());
//        if (existingOptions != null)
//        {
//            newOptions = existingOptions.mergedOptions(sourceOptions);
//        }
        sourceOptionsMap.put(sourceOptions.getClass(), newOptions);
    }
    
    /**
     * Gets the timeout (ms) on the InputStream after which an IOExecption is thrown
     * to terminate very slow transformations or a subprocess is terminated (killed).
     * @return timeoutMs in milliseconds. If less than or equal to zero (the default)
     *         there is no timeout.
     */
    public long getTimeoutMs()
    {
        return limits.getTimeoutMs();
    }

    /**
     * Sets a timeout (ms) on the InputStream after which an IOExecption is thrown
     * to terminate very slow transformations or to terminate (kill) a subprocess.
     * @param timeoutMs in milliseconds. If less than or equal to zero (the default)
     *                  there is no timeout.
     *                  If greater than zero the {@code readLimitTimeMs} must not be set.
     */
    public void setTimeoutMs(long timeoutMs)
    {
        limits.setTimeoutMs(timeoutMs);
    }
    
    /**
     * Get the page limit before returning EOF.
     * @return If less than or equal to zero (the default) no limit should be applied.
     */
    public int getPageLimit()
    {
        return limits.getPageLimit();
    }
    
    /**
     * Set the number of pages read from the source before returning EOF.
     * 
     * @param pageLimit the number of pages to be read from the source. If less 
     *        than or equal to zero (the default) no limit is applied.
     */
    public void setPageLimit(int pageLimit)
    {
        limits.setPageLimit(pageLimit);
    }

    public Map<String, Serializable> getAdditionalOptions()
    {
        return additionalOptions;
    }

    public void setAdditionalOptions(Map<String, Serializable> additionalOptions)
    {
        this.additionalOptions = additionalOptions;
    }
}
