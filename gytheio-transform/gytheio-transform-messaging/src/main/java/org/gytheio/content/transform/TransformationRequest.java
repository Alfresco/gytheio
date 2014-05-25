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

import java.util.List;

import org.gytheio.content.AbstractContentRequest;
import org.gytheio.content.ContentReference;
import org.gytheio.content.transform.options.TransformationOptions;
import org.gytheio.messaging.Request;

/**
 * Represents a request for content transformation from source to target with transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationRequest extends AbstractContentRequest implements Request<TransformationReply>
{
    private List<ContentReference> targetContentReferences;
    private TransformationOptions options;
    
    public TransformationRequest()
    {
        super();
    }
    
    public TransformationRequest(
            List<ContentReference> sourceContentReferences,
            List<ContentReference> targetContentReferences, 
            TransformationOptions options)
    {
        super();
        setSourceContentReferences(sourceContentReferences);
        this.targetContentReferences = targetContentReferences;
        this.options = options;
    }

    /**
     * Gets the target content reference objects
     * 
     * @return target content references
     */
    public List<ContentReference> getTargetContentReferences()
    {
        return targetContentReferences;
    }

    /**
     * Sets the target content reference objects
     * 
     * @param targetContentReferences
     */
    public void setTargetContentReferences(List<ContentReference> targetContentReferences)
    {
        this.targetContentReferences = targetContentReferences;
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

    @Override
    public Class<TransformationReply> getReplyClass()
    {
        return TransformationReply.class;
    }
    
}
