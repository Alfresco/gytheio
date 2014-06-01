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

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * Configuration for the Gytheio node
 * 
 * @author Ray Gauss II
 */
public class NodeConfiguration extends Configuration
{
    
    @Valid
    @NotNull
    private MessagingConfiguration messagingConfig;
    
    @Valid
    @NotNull
    private ContentReferenceHandlersConfiguration contentReferenceHandlersConfig;
    
    @Valid
    private List<ComponentConfiguration> components;
    
    private String version;
    
    @JsonProperty("messaging")
    public MessagingConfiguration getMessagingConfig()
    {
        return messagingConfig;
    }

    @JsonProperty("messaging")
    public void setMessagingConfig(MessagingConfiguration messagingConfig)
    {
        this.messagingConfig = messagingConfig;
    }

    @JsonProperty("contentReferenceHandlers")
    public ContentReferenceHandlersConfiguration getContentReferenceHandlersConfig()
    {
        return contentReferenceHandlersConfig;
    }

    @JsonProperty("contentReferenceHandlers")
    public void setContentReferenceHandlersConfig(ContentReferenceHandlersConfiguration contentReferenceHandlersConfig)
    {
        this.contentReferenceHandlersConfig = contentReferenceHandlersConfig;
    }

    @JsonProperty()
    public List<ComponentConfiguration> getComponents()
    {
        return components;
    }

    @JsonProperty()
    public void setComponents(List<ComponentConfiguration> components)
    {
        this.components = components;
    }

    @JsonProperty
    public String getVersion()
    {
        return version;
    }

    @JsonProperty
    public void setVersion(String version)
    {
        this.version = version;
    }

}
