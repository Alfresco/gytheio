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

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gytheio.content.transform.AbstractFileContentTransformerWorker;
import org.gytheio.util.exec.RuntimeExec;
import org.gytheio.util.exec.RuntimeExec.ExecutionResult;

/**
 * Base class that uses a command line {@link RuntimeExec} to perform the transform 
 * 
 * @author Ray Gauss II
 */
public abstract class AbstractRuntimeExecContentTransformerWorker extends AbstractFileContentTransformerWorker
{
    private static final Log logger = LogFactory.getLog(AbstractRuntimeExecContentTransformerWorker.class);
 
    /** the system command executer */
    protected RuntimeExec executer;
    /** the check command executer */
    protected RuntimeExec versionDetailsExecuter;
    
    /**
     * Default constructor
     */
    public AbstractRuntimeExecContentTransformerWorker()
    {
        // Intentionally empty
    }
    
    /**
     * Set the runtime command executer that must be executed.
     * Whether or not this is the full path to the convertCommand
     * or just the convertCommand itself depends the environment setup.
     * <p>
     *  
     * @param executer the system command executer
     */
    public void setExecuter(RuntimeExec executer)
    {
        this.executer = executer;
    }
    
    /**
     * Sets the command that must be executed in order to retrieve version information from the converting executable
     * and thus test that the executable itself is present.
     * 
     * @param versionDetailsExecuter
     *            command executer to retrieve version information
     */
    public void setVersionDetailsExecuter(RuntimeExec versionDetailsExecuter)
    {
        this.versionDetailsExecuter = versionDetailsExecuter;
    }
    
    protected abstract void initializeExecuter();
    protected abstract void initializeVersionDetailsExecuter();
    protected abstract void initializationTest();
    
    /**
     * Checks for the JMagick and ImageMagick dependencies, using the common
     * {@link #transformInternal(File, File) transformation method} to check
     * that the sample image can be converted. 
     */
    @Override
    public void initialize()
    {
        super.initialize();
        initializeExecuter();
        initializeVersionDetailsExecuter();
        initializeVersionDetailsString();
        initializationTest();
    }
    
    @Override
    protected void initializeVersionDetailsString()
    {
        try
        {
            // On some platforms / versions, the -version command seems to return an error code whilst still
            // returning output, so let's not worry about the exit code!
            ExecutionResult result = this.versionDetailsExecuter.execute();
            this.versionDetailsString = result.getStdOut().trim();
        }
        catch (Throwable e)
        {
            setIsAvailable(false);
            logger.error(getClass().getSimpleName() + " not available: "
                    + (e.getMessage() != null ? e.getMessage() : ""));
            // debug so that we can trace the issue if required
            logger.debug(e);
        }
    }
}
