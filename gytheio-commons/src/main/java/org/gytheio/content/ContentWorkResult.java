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

import java.util.Map;

/**
 * The result of some operation on content which includes a content reference
 * and details about the operation.
 * <p>
 * The content reference could indicate the source content reference on which the 
 * work was requested or could be a new target content reference.
 *  
 * @author Ray Gauss II
 */
public class ContentWorkResult
{
    private ContentReference contentReference;
    private Map<String, Object> details;
    
    public ContentWorkResult()
    {
    }
    
    public ContentWorkResult(ContentReference contentReference, Map<String, Object> details)
    {
        this.contentReference = contentReference;
        this.details = details;
    }
    
    /**
     * Gets the content reference associated with the result.
     * 
     * @return the content reference
     */
    public ContentReference getContentReference()
    {
        return contentReference;
    }
    
    /**
     * Sets the content reference associated with the result.
     * 
     * @param contentReference
     */
    public void setContentReference(ContentReference contentReference)
    {
        this.contentReference = contentReference;
    }
    
    /**
     * Gets the additional details of the result of the content work.
     * 
     * @return additional details
     */
    public Map<String, Object> getDetails()
    {
        return details;
    }
    
    /**
     * Sets the additional details of the result of the content work.
     * 
     * @param details
     */
    public void setDetails(Map<String, Object> details)
    {
        this.details = details;
    }

}
