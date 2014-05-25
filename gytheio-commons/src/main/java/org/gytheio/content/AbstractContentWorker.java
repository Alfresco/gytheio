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

import org.gytheio.content.handler.ContentReferenceHandler;

/**
 * Base implementation of a content worker with a <code>sourceContentReferenceHandler</code>
 * field.
 */
public abstract class AbstractContentWorker implements ContentWorker
{
    protected ContentReferenceHandler sourceContentReferenceHandler;
    
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
    public abstract void initialize();
}
