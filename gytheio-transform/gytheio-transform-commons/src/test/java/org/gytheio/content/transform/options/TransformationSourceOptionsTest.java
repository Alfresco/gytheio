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

import static junit.framework.Assert.*;

import java.util.Arrays;
import org.junit.Test;


public class TransformationSourceOptionsTest
{
    
    @Test
    public void testCropSourceOptions()
    {
        CropSourceOptions options = new CropSourceOptions();
        options.setApplicableMediaTypes(Arrays.asList("type1", "type2"));
        options.setWidth(640);
        options.setHeight(480);
        options.setXOffset(10);
        options.setYOffset(20);
        options.setGravity("up");
        
        CropSourceOptions copy = new CropSourceOptions(options);
        
        options.setWidth(22);
        options.setHeight(33);
        
        assertEquals("[type1, type2]", copy.getApplicableMediaTypes().toString());
        assertEquals(640, copy.getWidth());
        assertEquals(480, copy.getHeight());
        assertEquals(10, copy.getXOffset());
        assertEquals(20, copy.getYOffset());
        assertEquals("up", copy.getGravity());
    }

    
    @Test
    public void testPagedSourceOptions()
    {
        PagedSourceOptions options = new PagedSourceOptions();
        options.setApplicableMediaTypes(Arrays.asList("type1", "type2"));
        options.setStartPageNumber(5);
        options.setEndPageNumber(6);
        
        PagedSourceOptions copy = new PagedSourceOptions(options);
        assertEquals("[type1, type2]", copy.getApplicableMediaTypes().toString());
        assertEquals(new Integer(5), copy.getStartPageNumber());
        assertEquals(new Integer(6), copy.getEndPageNumber());
        
        PagedSourceOptions other = new PagedSourceOptions();
        other.setEndPageNumber(100);
        options.merge(other);
        assertEquals(new Integer(5), options.getStartPageNumber());
        assertEquals(new Integer(100), options.getEndPageNumber());
    }
    
    @Test
    public void testTemporalSourceOptions()
    {
        TemporalSourceOptions options = new TemporalSourceOptions();
        options.setApplicableMediaTypes(Arrays.asList("type1", "type2"));
        options.setDuration("59:59:59");
        options.setElementIntervalSeconds(5);
        options.setMaxElements(10);
        options.setOffset("00:00:01");
        
        TemporalSourceOptions copy = new TemporalSourceOptions(options);
        assertEquals("[type1, type2]", copy.getApplicableMediaTypes().toString());
        assertEquals("59:59:59", copy.getDuration());
        assertEquals(new Integer(5), copy.getElementIntervalSeconds());
        assertEquals(new Integer(10), copy.getMaxElements());
        assertEquals("00:00:01", copy.getOffset());
        
        TemporalSourceOptions other = new TemporalSourceOptions();
        other.setOffset("00:11:11");
        options.merge(other);
        assertEquals("59:59:59", options.getDuration());
        assertEquals("00:11:11", options.getOffset());
    }

}
