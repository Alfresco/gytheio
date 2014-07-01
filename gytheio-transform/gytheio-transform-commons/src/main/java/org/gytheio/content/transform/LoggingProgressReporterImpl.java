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

import java.util.List;

import org.apache.commons.logging.Log;
import org.gytheio.content.ContentWorkResult;

/**
 * Progress reporter which logs via a given logger.
 * 
 * @author Ray Gauss II
 */
public class LoggingProgressReporterImpl implements ContentTransformerWorkerProgressReporter
{
    private Log logger;
    
    public LoggingProgressReporterImpl(Log logger)
    {
        this.logger = logger;
    }

    public void onTransformationStarted()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Starting transformation");
        }
    }
    
    public void onTransformationProgress(float progress)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(progress*100 + "% progress on transformation");
        }
    }
    
    public void onTransformationComplete(List<ContentWorkResult> results)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Completed transformation");
        }
    }

    @Override
    public void onTransformationError(String errorMessage)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Error performing transformation: " + errorMessage);
        }
    }

}
