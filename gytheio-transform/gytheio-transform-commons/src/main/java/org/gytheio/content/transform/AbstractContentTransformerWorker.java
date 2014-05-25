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

import org.gytheio.content.AbstractContentWorker;
import org.gytheio.content.ContentReference;
import org.gytheio.content.handler.ContentReferenceHandler;
import org.gytheio.content.mediatype.FileMediaType;

/**
 * Abstract transform node worker which uses a content reference handler to convert the 
 * content reference into a usable File object for the actual implementation.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractContentTransformerWorker 
        extends AbstractContentWorker implements ContentTransformerWorker
{
    protected ContentReferenceHandler targetContentReferenceHandler;
    
    public void setTargetContentReferenceHandler(ContentReferenceHandler targetContentReferenceHandler)
    {
        this.targetContentReferenceHandler = targetContentReferenceHandler;
    }

    public void initialize()
    {
    }
    
    protected String getExtension(ContentReference contentReference)
    {
        return FileMediaType.SERVICE.getExtension(contentReference.getMediaType());
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("sourceContentReferenceHandler: " + sourceContentReferenceHandler.toString());
        builder.append(", ");
        builder.append("targetContentReferenceHandler: " + targetContentReferenceHandler.toString());
        builder.append("]");
        return builder.toString();
    }

}
