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
package org.gytheio.health.heartbeat;

import java.util.Map;

/**
 * An object describing service resource behavior at a particular
 * point in time.
 * 
 * @author Ray Gauss II
 */
public class Heartbeat
{
    private String serviceType;
    private String serviceInstanceId;
    private Long timestamp;
    private boolean isAvailable;
    private Map<String, String> details;
    
    public Heartbeat()
    {
    }
    
    public Heartbeat(String serviceType, String serviceInstanceId, 
            boolean isAvailable)
    {
        this.serviceType = serviceType;
        this.serviceInstanceId = serviceInstanceId;
        this.isAvailable = isAvailable;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Heartbeat(String serviceType, String serviceInstanceId, 
            boolean isAvailable, Map<String, String> detail)
    {
        this.serviceType = serviceType;
        this.serviceInstanceId = serviceInstanceId;
        this.isAvailable = isAvailable;
        this.timestamp = System.currentTimeMillis();
        this.details = detail;
    }
    
    /**
     * Gets the service type.
     * 
     * @return the service type
     */
    public String getServiceType()
    {
        return serviceType;
    }
    
    /**
     * Sets the service type.
     * 
     * @param serviceType
     */
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
    
    /**
     * Gets the identifier for a particular instance of a service such as an IP address.
     * 
     * @return the service instance ID
     */
    public String getServiceInstanceId()
    {
        return serviceInstanceId;
    }

    /**
     * Sets the identifier for a particular instance of a component.
     * 
     * @param serviceInstanceId
     */
    public void setServiceInstanceId(String serviceInstanceId)
    {
        this.serviceInstanceId = serviceInstanceId;
    }

    /**
     * Gets the epoch time of the health check.
     * 
     * @return the epoch time of the heartbeat
     */
    public Long getTimestamp()
    {
        return timestamp;
    }
    
    /**
     * Sets the epoch time of the health check.
     * 
     * @param time
     */
    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Gets whether or not the component is available
     * 
     * @return true if available
     */
    public boolean isAvailable()
    {
        return isAvailable;
    }

    /**
     * Sets whether or not the component is available
     * 
     * @param isAvailable
     */
    public void setAvailable(boolean isAvailable)
    {
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the optional status details which might include additional statistics on health.
     * 
     * @return the status message
     */
    public Map<String, String> getDetails()
    {
        return details;
    }

    /**
     * Sets the optional status details which might include additional statistics on health.
     * 
     * @param detail
     */
    public void setDetails(Map<String, String> detail)
    {
        this.details = detail;
    }

}
