package org.alfresco.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Endpoint which simply logs dead letter messages
 * 
 * @author Ray Gauss II
 */
public class LoggingDeadLetterQueue
{
    private static final Log logger = LogFactory.getLog(LoggingDeadLetterQueue.class);

    public void onReceive(Object message)
    {
        if (logger.isDebugEnabled() && message != null)
        {
            logger.debug("Received:\n\n" + message.toString() + "\n\n");
        }
    }

}
