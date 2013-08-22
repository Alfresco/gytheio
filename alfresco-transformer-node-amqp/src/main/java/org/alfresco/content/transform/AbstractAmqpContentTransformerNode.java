package org.alfresco.content.transform;

import org.alfresco.content.transform.AbstractContentTransformerNode;
import org.alfresco.messaging.amqp.AmqpDirectEndpoint;
import org.alfresco.messaging.jackson.ObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractAmqpContentTransformerNode extends AbstractContentTransformerNode
{
    protected String host;
    protected String receiveQueueName;
    protected String replyQueueName;
    protected ObjectMapper objectMapper = ObjectMapperFactory.createInstance();
    
    public void setHost(String host)
    {
        this.host = host;
    }

    public void setReceiveQueueName(String receiveQueueName)
    {
        this.receiveQueueName = receiveQueueName;
    }

    public void setReplyQueueName(String replyQueueName)
    {
        this.replyQueueName = replyQueueName;
    }

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public void init()
    {
        messageProducer = new AmqpDirectEndpoint();
        ((AmqpDirectEndpoint) messageProducer).setHost(host);
        ((AmqpDirectEndpoint) messageProducer).setReceiveQueueName(receiveQueueName);
        ((AmqpDirectEndpoint) messageProducer).setSendQueueName(replyQueueName);
        ((AmqpDirectEndpoint) messageProducer).setMessageConsumer(this);
        ((AmqpDirectEndpoint) messageProducer).setObjectMapper(objectMapper);
        ((AmqpDirectEndpoint) messageProducer).startListener();
    }
    
    protected static void validateArguments(String[] args)
    {
        if (args.length < 3)
        {
            throw new IllegalArgumentException("USAGE: host receiveQueueName replyQueueName");
        }
    }
    
}
