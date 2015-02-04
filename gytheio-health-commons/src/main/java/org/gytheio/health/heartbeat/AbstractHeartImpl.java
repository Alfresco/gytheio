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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract heart implementation for sending {@link Heartbeat} messages.
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractHeartImpl implements Heart
{
    private static final Log logger = LogFactory.getLog(AbstractHeartImpl.class);

    protected static final long DEFAULT_TIMER_DELAY_MS = 2000;
    protected static final long DEFAULT_TIMER_PERIOD_MS = 5000;

    protected String serviceType;
    protected String serviceInstanceId;
    protected Timer timer;
    protected long timerPeriodMs = DEFAULT_TIMER_PERIOD_MS;
    
    /**
     * Sets the service type to be used for {@link Heartbeat} messages.
     * 
     * @param serviceType
     */
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    /**
     * Sets the service instance ID to be used for {@link Heartbeat} messages.
     * 
     * @param serviceInstanceId
     */
    public void setServiceInstanceId(String serviceInstanceId)
    {
        this.serviceInstanceId = serviceInstanceId;
    }

    /**
     * The period of heartbeat messages
     * 
     * @param timerPeriodMs
     */
    public void setTimerPeriodMs(long timerPeriodMs)
    {
        this.timerPeriodMs = timerPeriodMs;
    }

    @Override
    public void start()
    {
        logger.info("Starting heartbeat for " + serviceType + " id=" + serviceInstanceId);
        timer = new Timer();
        timer.schedule(new HeartbeatTask(), DEFAULT_TIMER_DELAY_MS, timerPeriodMs);
    }
    
    @Override
    public void stop()
    {
        timer.cancel();
    }
    
    /**
     * Gathers any useful health metrics for the details section of 
     * each heartbeat messages.
     * 
     * @return the status details
     */
    protected abstract Map<String, String> gatherDetails();
    
    /**
     * Sends the given heartbeat message
     * 
     * @param heartbeat
     */
    protected abstract void send(Heartbeat heartbeat);
    
    protected class HeartbeatTask extends TimerTask
    {
        @Override
        public void run()
        {
            try
            {
                Map<String, String> details = gatherDetails();
                Heartbeat heartbeat = 
                        new Heartbeat(serviceType, serviceInstanceId, true, details);
                send(heartbeat);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                timer.cancel();
            }
        }
        
    }
}
