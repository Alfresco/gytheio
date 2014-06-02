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
package org.gytheio.content.transform.options;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationOptionsImpl implements TransformationOptions
{
    /** Source options based on its mimetype */
    private Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap;
    
    /** The include embedded resources yes/no */
    private Boolean includeEmbedded;
    
    private Map<String, Serializable> additionalOptions;
    
    /** Time, KBytes and page limits */
    private TransformationOptionLimits limits = new TransformationOptionLimits();

    @Override
    public Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> getSourceOptionsMap()
    {
        return sourceOptionsMap;
    }

    @Override
    public void setSourceOptionsMap(
            Map<Class<? extends TransformationSourceOptions>, TransformationSourceOptions> sourceOptionsMap)
    {
        this.sourceOptionsMap = sourceOptionsMap;
    }
    
    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public <T extends TransformationSourceOptions> T getSourceOptions(Class<T> clazz)
    {
        if (sourceOptionsMap == null)
            return null;
        return (T) sourceOptionsMap.get(clazz);
    }
    
    @Override
    public void addSourceOptions(TransformationSourceOptions sourceOptions)
    {
        if (sourceOptionsMap == null)
        {
            sourceOptionsMap = new HashMap<Class<? extends TransformationSourceOptions>, TransformationSourceOptions>(1);
        }
        TransformationSourceOptions newOptions = sourceOptions;
        TransformationSourceOptions existingOptions = sourceOptionsMap.get(sourceOptions.getClass());
        if (existingOptions != null)
        {
            newOptions = existingOptions.mergedOptions(sourceOptions);
        }
        sourceOptionsMap.put(sourceOptions.getClass(), newOptions);
    }
    
    @Override
    public long getTimeoutMs()
    {
        return limits.getTimeoutMs();
    }

    @Override
    public void setTimeoutMs(long timeoutMs)
    {
        limits.setTimeoutMs(timeoutMs);
    }
    
    @Override
    public int getPageLimit()
    {
        return limits.getPageLimit();
    }
    
    @Override
    public void setIncludeEmbedded(Boolean includeEmbedded) 
    {
       this.includeEmbedded = includeEmbedded;
    }

    @Override
    public Boolean getIncludeEmbedded() 
    {
        return includeEmbedded;
    }
    
    @Override
    public void setPageLimit(int pageLimit)
    {
        limits.setPageLimit(pageLimit);
    }

    @Override
    public Map<String, Serializable> getAdditionalOptions()
    {
        return additionalOptions;
    }

    @Override
    public void setAdditionalOptions(Map<String, Serializable> additionalOptions)
    {
        this.additionalOptions = additionalOptions;
    }
}
