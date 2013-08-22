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
package org.alfresco.content.transform;

import java.util.UUID;

import org.alfresco.content.ContentReference;
import org.alfresco.content.transform.options.TransformationOptions;

/**
 * Represents a request for content transformation from source to target with transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationRequest
{
    private final String transformationRequestId;
    private ContentReference sourceContentReference;
    private ContentReference targetContentReference;
    private TransformationOptions options;
    
    public TransformationRequest()
    {
        super();
        this.transformationRequestId = UUID.randomUUID().toString();
    }
    
    public TransformationRequest(ContentReference sourceContentReference, ContentReference targetContentReference, 
            TransformationOptions options)
    {
        super();
        this.transformationRequestId = UUID.randomUUID().toString();
        this.sourceContentReference = sourceContentReference;
        this.targetContentReference = targetContentReference;
        this.options = options;
    }

    /**
     * Gets the generated UUID for the transformation request
     * 
     * @return the request ID
     */
    public String getTransformationRequestId()
    {
        return transformationRequestId;
    }

    /**
     * Gets the source content reference object
     * 
     * @return source content reference
     */
    public ContentReference getSourceContentReference()
    {
        return sourceContentReference;
    }

    /**
     * Sets the source content reference object
     * 
     * @param sourceContentReference
     */
    public void setSourceContentReference(ContentReference sourceContentReference)
    {
        this.sourceContentReference = sourceContentReference;
    }

    /**
     * Gets the target content reference object
     * 
     * @return target content reference
     */
    public ContentReference getTargetContentReference()
    {
        return targetContentReference;
    }

    /**
     * Sets the target content reference object
     * 
     * @param targetContentReference
     */
    public void setTargetContentReference(ContentReference targetContentReference)
    {
        this.targetContentReference = targetContentReference;
    }

    /**
     * Gets the options for the requested transformation
     * 
     * @return the transformation options
     */
    public TransformationOptions getOptions()
    {
        return options;
    }

    /**
     * Sets the options for the requested transformation
     * 
     * @param options
     */
    public void setOptions(TransformationOptions options)
    {
        this.options = options;
    }
    
}
