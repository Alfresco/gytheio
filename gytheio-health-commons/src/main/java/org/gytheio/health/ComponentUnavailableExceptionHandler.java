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
package org.gytheio.health;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Determines the {@link ComponentUnavailableAction} to execute when a 
 * {@link ComponentUnavailableException} is thrown
 * 
 * @author rgauss
 */
public class ComponentUnavailableExceptionHandler
{
    private static final Log logger = LogFactory.getLog(ComponentUnavailableExceptionHandler.class);
    
    private Map<String, ComponentUnavailableAction> policies;

    /**
     * A map of ComponentUnavailableException canonical class names to the
     * ComponentUnavailableAction that should be executed.
     * 
     * @param policies
     */
    public void setPolicies(Map<String, ComponentUnavailableAction> policies)
    {
        this.policies = policies;
    }
    
    /**
     * Determines the ComponentUnavailableAction to execute based on the given exception
     * and executes it.
     * 
     * @param e
     */
    public void handle(Throwable e)
    {
        if (e == null)
        {
            return;
        }
        String actionKey = e.getClass().getCanonicalName();
        if (policies == null || !policies.containsKey(actionKey))
        {
            logger.error("Received " + actionKey + 
                    " but no corresponding action registered: " + e.getMessage(), e);
        }
        else
        {
            policies.get(actionKey).execute(e);
        }
    }

}
