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
package org.gytheio.content.util;

import java.util.List;

import org.gytheio.content.ContentReference;
import org.gytheio.content.ContentWorkResult;

/**
 * Util method for working with {@link ContentReference}s and {@link ContentWorkResult}s.
 * 
 * @author Ray Gauss II
 */
public class ContentUtil
{
    
    /**
     * Convenience method for getting a specific result detail value for a
     * given content reference.
     * 
     * @param detailKey
     * @param contentReference
     * @param results
     * @return the result detail value
     */
    public static Object getSingleResultDetailValue(
            String detailKey, 
            ContentReference contentReference,
            List<ContentWorkResult> results)
    {
        if (detailKey == null || contentReference == null || results == null || results.size() == 0)
        {
            return null;
        }
        ContentWorkResult foundResult = null;
        for (ContentWorkResult result : results)
        {
            if (contentReference.equals(result.getContentReference()))
            {
                if (foundResult != null)
                {
                    throw new IllegalStateException(
                            "More than one result corresponding to content reference " + 
                    contentReference.toString());
                }
                foundResult = result;
            }
        }
        if (foundResult == null || foundResult.getDetails() == null)
        {
            return null;
        }
        return foundResult.getDetails().get(detailKey);
    }

}
