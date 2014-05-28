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

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration POJO for a component
 * 
 * @author Ray Gauss II
 */
public class ComponentConfiguration
{
    @NotNull
    private String name;
    
    @NotNull
    private String workerClass;
    
    @NotNull
    private boolean enabled;
    
    @NotEmpty
    private String requestQueue;
    
    @NotEmpty
    private String replyQueue;

    @JsonProperty
    public String getName()
    {
        return name;
    }

    @JsonProperty
    public void setName(String name)
    {
        this.name = name;
    }

    @JsonProperty
    public String getWorkerClass()
    {
        return workerClass;
    }

    @JsonProperty
    public void setWorkerClass(String workerClass)
    {
        this.workerClass = workerClass;
    }

    @JsonProperty
    public boolean getEnabled()
    {
        return enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @JsonProperty
    public String getRequestQueue()
    {
        return requestQueue;
    }

    @JsonProperty
    public void setRequestQueue(String requestQueue)
    {
        this.requestQueue = requestQueue;
    }

    @JsonProperty
    public String getReplyQueue()
    {
        return replyQueue;
    }

    @JsonProperty
    public void setReplyQueue(String replyQueue)
    {
        this.replyQueue = replyQueue;
    }

}
