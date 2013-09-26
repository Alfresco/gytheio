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
package org.alfresco.content;

import org.alfresco.content.ContentReference;
import org.alfresco.messaging.AbstractRequest;

/**
 * Represents a request for content transformation from source to target with transformation options
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentRequest extends AbstractRequest
{
    private ContentReference sourceContentReference;
    
    public AbstractContentRequest()
    {
        super();
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

}
