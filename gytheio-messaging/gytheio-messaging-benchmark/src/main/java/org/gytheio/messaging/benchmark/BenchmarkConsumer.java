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
package org.gytheio.messaging.benchmark;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.gytheio.messaging.MessageConsumer;

/**
 * Consumer of {@link BenchmarkMessage}s which maintains a count of messages received.
 */
public class BenchmarkConsumer implements MessageConsumer
{
    private static final Log logger = LogFactory.getLog(BenchmarkConsumer.class);
    
    protected static final int LOG_AFTER_NUM_MESSAGES = 1000;

    private int messageCount = 0;
    
    @Override
    public void onReceive(Object message)
    {
        if (logger.isTraceEnabled())
        {
            logger.trace("Receiving message, current messageCount=" + messageCount + "...");
        }
        if (message == null || ((BenchmarkMessage) message).getValue() == null ||
                !((BenchmarkMessage) message).getValue().equals(BenchmarkMessage.DEFAULT_VALUE))
        {
            throw new IllegalArgumentException("Could not verify message");
        }
        
        messageCount++;
        
        if (messageCount > 0 && messageCount % LOG_AFTER_NUM_MESSAGES == 0)
        {
            logger.debug("Received " + messageCount + " messages...");
        }
        else
        {
            if (logger.isTraceEnabled())
            {
                logger.trace("Received " + messageCount + " messages...");
            }
        }
    }

    @Override
    public Class<?> getConsumingMessageBodyClass()
    {
        return BenchmarkMessage.class;
    }

    public int getMessageCount()
    {
        return messageCount;
    }
}
