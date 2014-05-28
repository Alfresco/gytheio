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

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration POJO for a messaging broker
 * 
 * @author Ray Gauss II
 */
public class BrokerConfiguration
{
    @NotEmpty
    private String url;
    
    private String username;
    
    private String password;

    @JsonProperty
    public String getUrl()
    {
        return url;
    }

    @JsonProperty
    public void setUrl(String url)
    {
        this.url = url;
    }

    @JsonProperty
    public String getUsername()
    {
        return username;
    }

    @JsonProperty
    public void setUsername(String username)
    {
        this.username = username;
    }

    @JsonProperty
    public String getPassword()
    {
        return password;
    }

    @JsonProperty
    public void setPassword(String password)
    {
        this.password = password;
    }
}
