package org.gytheio.content;

import org.gytheio.messaging.MessageConsumer;

/**
 * Defines a component which is a consumer of request for content action messages,
 * delegates that work to a worker, and sends a reply with the results.
 */
public interface Component extends MessageConsumer
{

}
