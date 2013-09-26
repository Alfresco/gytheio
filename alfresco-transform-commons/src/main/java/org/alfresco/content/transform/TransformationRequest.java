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

import org.alfresco.content.AbstractContentRequest;
import org.alfresco.content.ContentReference;
import org.alfresco.content.transform.options.TransformationOptions;
import org.alfresco.messaging.Request;

/**
 * Represents a request for content transformation from source to target with transformation options
 * 
 * @author Ray Gauss II
 */
public class TransformationRequest extends AbstractContentRequest implements Request<TransformationReply>
{
    private ContentReference targetContentReference;
    private TransformationOptions options;
    
    public TransformationRequest()
    {
        super();
    }
    
    public TransformationRequest(ContentReference sourceContentReference, ContentReference targetContentReference, 
            TransformationOptions options)
    {
        super();
        setSourceContentReference(sourceContentReference);
        this.targetContentReference = targetContentReference;
        this.options = options;
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

    @Override
    public Class<TransformationReply> getReplyClass()
    {
        return TransformationReply.class;
    }
    
}
