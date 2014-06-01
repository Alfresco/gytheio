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
package org.gytheio.content.dropwizard.configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration for source and target content reference handlers
 * 
 * @author Ray Gauss II
 */
public class ContentReferenceHandlersConfiguration
{
    @NotNull
    private ContentReferenceHandlerConfiguration source;
    
    @NotNull
    private ContentReferenceHandlerConfiguration target;
    
    @JsonProperty
    public ContentReferenceHandlerConfiguration getSource()
    {
        return source;
    }
    
    @JsonProperty
    public void setSource(ContentReferenceHandlerConfiguration source)
    {
        this.source = source;
    }

    @JsonProperty
    public ContentReferenceHandlerConfiguration getTarget()
    {
        return target;
    }

    @JsonProperty
    public void setTarget(ContentReferenceHandlerConfiguration target)
    {
        this.target = target;
    }
    
}
