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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.gytheio.content.mediatype.FileMediaType;
import org.gytheio.content.transform.options.CropSourceOptions;
import org.gytheio.content.transform.options.ImageResizeOptions;
import org.gytheio.content.transform.options.ImageTransformationOptions;
import org.gytheio.content.transform.options.PagedSourceOptions;
import org.gytheio.content.transform.options.TemporalSourceOptions;
import org.gytheio.messaging.jackson.ObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformationRequestMarshallingTest
{

    private TransformationRequest transformationRequest;
    private ObjectMapper mapper;
    
    @Before
    public void setUp() {
        ImageTransformationOptions options = new ImageTransformationOptions();
        options.setAutoOrient(true);
        options.setCommandOptions("command options");
        options.setIncludeEmbedded(true);
        options.setPageLimit(50);
        options.setTimeoutMs(1000);
        
        ImageResizeOptions imageResizeOptions = new ImageResizeOptions();
        imageResizeOptions.setAllowEnlargement(true);
        imageResizeOptions.setHeight(55);
        imageResizeOptions.setMaintainAspectRatio(true);
        imageResizeOptions.setPercentResize(true);
        imageResizeOptions.setResizeToThumbnail(true);
        imageResizeOptions.setWidth(56);
        options.setResizeOptions(imageResizeOptions);
        
        CropSourceOptions cropSourceOptions = new CropSourceOptions();
        cropSourceOptions.setApplicableMediaTypes(Arrays.asList("image media types"));
        cropSourceOptions.setGravity("gravity");
        cropSourceOptions.setHeight(40);
        cropSourceOptions.setPercentageCrop(true);
        cropSourceOptions.setWidth(41);
        cropSourceOptions.setXOffset(5);
        cropSourceOptions.setYOffset(6);
        options.addSourceOptions(cropSourceOptions);
        
        PagedSourceOptions pagedSourceOptions = new PagedSourceOptions();
        pagedSourceOptions.setApplicableMediaTypes(Arrays.asList("document media types"));
        pagedSourceOptions.setStartPageNumber(7);
        pagedSourceOptions.setEndPageNumber(8);
        options.addSourceOptions(pagedSourceOptions);
        
        TemporalSourceOptions temporalSourceOptions = new TemporalSourceOptions();
        temporalSourceOptions.setApplicableMediaTypes(Arrays.asList("a/v media types"));
        temporalSourceOptions.setDuration("00:00:00.5");
        temporalSourceOptions.setOffset("00:00:00.2");
        options.addSourceOptions(temporalSourceOptions);
        
        transformationRequest = new TransformationRequest();
        transformationRequest.setOptions(options);
        transformationRequest.setTargetMediaType(FileMediaType.VIDEO_MP4.getMediaType());
        
        mapper = ObjectMapperFactory.createInstance();
    }
    
    @Test
    public void testMarshalling() throws IOException
    {
        String json = mapper.writeValueAsString(transformationRequest);
        
        System.out.println("json=" + json);
        
        TransformationRequest unmarshalledRequest = mapper.readValue(json, TransformationRequest.class);
        
        assertEquals(
                "00:00:00.5", 
                unmarshalledRequest.getOptions().getSourceOptions(TemporalSourceOptions.class).getDuration());
    }

}
