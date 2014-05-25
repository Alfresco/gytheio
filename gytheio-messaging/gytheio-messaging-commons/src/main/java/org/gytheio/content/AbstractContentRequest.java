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

import java.util.List;

import org.gytheio.content.ContentReference;
import org.gytheio.messaging.AbstractRequest;

/**
 * Represents a request for some operation on content sources.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentRequest extends AbstractRequest
{
    private List<ContentReference> sourceContentReferences;
    
    public AbstractContentRequest()
    {
        super();
    }

    /**
     * Gets the source content reference objects
     * 
     * @return source content references
     */
    public List<ContentReference> getSourceContentReferences()
    {
        return sourceContentReferences;
    }

    /**
     * Sets the source content reference objects
     * 
     * @param sourceContentReference
     */
    public void setSourceContentReferences(List<ContentReference> sourceContentReferences)
    {
        this.sourceContentReferences = sourceContentReferences;
    }

}
