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
package org.gytheio.content;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;

import org.gytheio.messaging.Reply;
import org.gytheio.messaging.Request;

/**
 * Base component which pulls a message off the queue before performing the work, useful
 * when progress reporting is needed as some messaging endpoints will lock the session
 * until the message consumption is complete and progress replies can not be sent.
 *
 * @param <W>
 * @param <RQ>
 * @param <RP>
 */
public abstract class AbstractAsyncComponent<W extends ContentWorker, RQ extends Request<RP>, RP extends Reply> extends AbstractComponent<W>
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractAsyncComponent.class);
    
    protected ExecutorService executorService;
    
    private final BlockingQueue<RQ> localQueue =  new SynchronousQueue<RQ>();
    
    /**
     * Sets the executor service components may optionally need for running
     * separate threads.
     * 
     * @param executorService
     */
    public void setExecutorService(ExecutorService executorService)
    {
        this.executorService = executorService;
    }

    @SuppressWarnings("unchecked")
    protected void onReceiveImpl(Object message)
    {
        RQ request = (RQ) message;
        try
        {
            localQueue.put(request);
        }
        catch (InterruptedException e)
        {
        }
    }
    
    /**
     * Performs the actual work for the request.
     * 
     * @param request
     */
    protected abstract void processRequest(RQ request);
    
    /**
     * Takes from the local concurrent queue and hands them off to be processed
     *
     */
    protected class LocalQueueProcessor implements Runnable
    {
        private final BlockingQueue<RQ> localProcessorQueue;
        
        public LocalQueueProcessor(BlockingQueue<RQ> localProcessorQueue)
        {
            this.localProcessorQueue = localProcessorQueue;
        }
        
        @Override
        public void run()
        {
            logger.debug("Starting local queue processing");
            while(true)
            {
                try
                {
                    RQ request = localProcessorQueue.take();
                    logger.debug("Processing local queue message");
                    processRequest(request);
                    logger.debug("Processing local queue message complete");
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }
    
    public void init()
    {
        super.init();
        executorService.execute(new LocalQueueProcessor(localQueue));
    }

}
