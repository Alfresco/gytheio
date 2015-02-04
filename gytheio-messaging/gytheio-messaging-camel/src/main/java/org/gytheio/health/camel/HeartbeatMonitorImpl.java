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
package org.gytheio.health.camel;

import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.health.heartbeat.Heartbeat;
import org.gytheio.health.heartbeat.HeartbeatDao;
import org.gytheio.health.heartbeat.HeartbeatMonitor;

/**
 * HeartbeatMonitor implementation which uses Camel to route {@link Heartbeat}
 * messages.
 * 
 * @author Ray Gauss II
 */
public class HeartbeatMonitorImpl implements HeartbeatMonitor
{
    private static final Log logger = LogFactory.getLog(HeartbeatMonitorImpl.class);
    
    private HeartbeatDao heartbeatDao;

    /**
     * Sets the DAO used to persist heartbeats.
     * 
     * @param heartbeatDao
     */
    public void setHeartbeatDao(HeartbeatDao heartbeatDao)
    {
        this.heartbeatDao = heartbeatDao;
    }

    @Override
    @Handler
    public void onReceive(Object message)
    {
        if (!(message instanceof Heartbeat))
        {
           logger.warn("Heartbeat message expected but received: " + message.toString());
           return;
        }
        heartbeatDao.record((Heartbeat) message);
    }

}
