package org.gytheio.health.camel;

import org.gytheio.health.Heart;
import org.gytheio.health.Heartbeat;
import org.gytheio.messaging.MessageProducer;

/**
 * Heart implementation which uses a Gytheio {@link MessageProducer} to
 * send a {@link Heartbeat} message.
 * 
 * @author rgauss
 */
public class HeartImpl implements Heart
{
    private String componentId;
    private String instanceId;
    private MessageProducer messageProducer;
    
    /**
     * Sets the component ID to be used for {@link Heartbeat} messages.
     * 
     * @param componentId
     */
    public void setComponentId(String componentId)
    {
        this.componentId = componentId;
    }

    /**
     * Sets the component instance ID to be used for {@link Heartbeat} messages.
     * 
     * @param instanceId
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    /**
     * Sets the message producer used to send {@link Heartbeat} messages.
     * 
     * @param messageProducer
     */
    public void setMessageProducer(MessageProducer messageProducer)
    {
        this.messageProducer = messageProducer;
    }

    @Override
    public void beat()
    {
        Heartbeat heartbeat = new Heartbeat(componentId, instanceId);
        messageProducer.send(heartbeat);
    }
    
}
