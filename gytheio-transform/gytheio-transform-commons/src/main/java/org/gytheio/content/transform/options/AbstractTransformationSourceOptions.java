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
import java.util.List;
import java.util.Map;

import org.gytheio.util.CloneField;
import org.gytheio.util.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base implementation of TransformationSourceOptions which holds applicable mimetypes
 * and handles merge of options.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractTransformationSourceOptions implements TransformationSourceOptions
{

    private static final long serialVersionUID = 2017077314384548215L;
    
    /** The list of applicable media types */
    private List<String> applicableMediaTypes;
    
    public AbstractTransformationSourceOptions()
    {
        super();
    }
    
    /**
     * Constructs a field copy object from the given 
     * 
     * @param origObject
     * @return a field copy object
     */
    public AbstractTransformationSourceOptions(AbstractTransformationSourceOptions origObject)
    {
        this();
        BeanUtils.copyFields(origObject, this, false);
    }
    
    @Override
    public void merge(TransformationSourceOptions override)
    {
        BeanUtils.copyFields(override, this, true);
    }
    
    /**
     * Gets the list of applicable media types
     * 
     * @return the applicable media types
     */
    @JsonIgnore
    @CloneField
    public List<String> getApplicableMediaTypes()
    {
        return applicableMediaTypes;
    }

    /**
     * Sets the list of applicable media types
     * 
     * @param applicableMimetypes the applicable media types
     */
    public void setApplicableMediaTypes(List<String> applicableMediaTypes)
    {
        this.applicableMediaTypes = applicableMediaTypes;
    }

    /**
     * Gets whether or not these transformation source options apply for the
     * given media type
     * 
     * @param mediaType the media type of the source
     * @return if these transformation source options apply
     */
    public boolean isApplicableForMediaType(String mediaType)
    {
        if (mediaType != null && applicableMediaTypes != null) { return applicableMediaTypes.contains(mediaType); }
        return false;
    }

    /**
     * Adds the given paramValue to the given params if it's not null.
     * 
     * @param paramName
     * @param paramValue
     * @param params
     */
    protected void putParameterIfNotNull(String paramName, Serializable paramValue, Map<String, Serializable> params)
    {
        if (paramValue != null)
        {
            params.put(paramName, paramValue);
        }
    }
    
    @Override
    public String toString()
    {
        return BeanUtils.toString(this);
    }
    
}
