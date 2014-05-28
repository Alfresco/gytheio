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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ComponentUnavailableAction which terminates the JVM with the specified exit code, not to be used lightly.
 * 
 * @author Ray Gauss II
 */
public class ComponentUnavailableActionTerminate implements ComponentUnavailableAction
{
    private static final Log logger = LogFactory.getLog(ComponentUnavailableActionTerminate.class);
    
    private static final Integer DEFAULT_EXIT_STATUS_CODE = new Integer(1);
    
    @Override
    public void execute(Throwable e)
    {
        logger.fatal("Terminating due to " + e.getClass().getSimpleName() + ": " + e.getMessage());
        Integer statusCode = null;
        if (e instanceof ComponentUnavailableException)
        {
            statusCode = ((ComponentUnavailableException) e).getExitStatusCode();
        }
        if (statusCode == null)
        {
            statusCode = DEFAULT_EXIT_STATUS_CODE;
        }
        System.exit(statusCode);
    }

}
