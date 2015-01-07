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
package org.gytheio.messaging.amqp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.qpid.amqp_1_0.jms.Connection;
import org.apache.qpid.amqp_1_0.jms.ConnectionFactory;
import org.apache.qpid.amqp_1_0.jms.Session;
import org.apache.qpid.amqp_1_0.jms.TextMessage;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;

import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.MessageProducer;
import org.gytheio.messaging.MessagingException;
import org.gytheio.messaging.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A {@link MessageProducer} and message listener which interacts with AMQP
 * queues directly using Apache Qpid.
 * 
 * @author Ray Gauss II
 *
 */
public class AmqpDirectEndpoint implements MessageProducer
{
    private static final Log logger = LogFactory.getLog(AmqpDirectEndpoint.class);

    private static final int DEFAULT_PORT = 5672;
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "password";

    private String host;
    private int port = DEFAULT_PORT;
    private String username = DEFAULT_USERNAME;
    private String password = DEFAULT_PASSWORD;
    private String receiveQueueName;
    private String sendQueueName;
    
    private Connection consumerConnection;
    private Connection producerConnection;
    private Session consumerSession;
    private Session producerSession;
    private org.apache.qpid.amqp_1_0.jms.MessageProducer defaultMessageProducer;
    
    private MessageConsumer messageConsumer;
    private AmqpListener listener;
    private ObjectMapper objectMapper;
    
    protected class AmqpListener implements Runnable
    {
        protected boolean isInitialized = false;
        
        public void run()
        {
            try
            {
                Queue receiveQueue = getConsumerSession().createQueue(receiveQueueName);
                org.apache.qpid.amqp_1_0.jms.MessageConsumer receiver = 
                        getConsumerSession().createConsumer(receiveQueue);
                
                isInitialized = true;
                
                logger.info("Waiting for an AMQP message on " + host + ":" + receiveQueueName);
                receiver.setMessageListener(new MessageListener()
                {
                    public void onMessage(final javax.jms.Message message)
                    {
                        try
                        {
                            logger.trace("Processing AMQP message");
                            String stringMessage = null;
                            
                            if (message instanceof TextMessage)
                            {
                                stringMessage = ((TextMessage) message).getText();
                            }
                            if (stringMessage != null)
                            {
                                Object pojoMessage = objectMapper.readValue(stringMessage, 
                                        messageConsumer.getConsumingMessageBodyClass());
                                if (pojoMessage == null)
                                {
                                    logger.error("Request could not be unmarshalled");
                                }
                                else
                                {
                                    if (pojoMessage instanceof Request<?>)
                                    {
                                        // Check for a reply to queue message header
                                       if (StringUtils.isEmpty(((Request<?>) pojoMessage).getReplyTo()))
                                       {
                                           if (message.getJMSReplyTo() != null)
                                               
                                           {
                                               String replyQueueName = message.getJMSReplyTo().toString();
                                               if (!StringUtils.isEmpty(replyQueueName))
                                               {
                                                   ((Request<?>) pojoMessage).setReplyTo(replyQueueName);
                                               }
                                           }
                                       }
                                    }
                                    
                                    messageConsumer.onReceive(pojoMessage);
                                }
                            }
                            else
                            {
                                logger.error("No valid message body found in " + message.toString());
                            }
                        }
                        catch (JMSException | IOException e)
                        {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
                
                getConsumerConnection().start();
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setReceiveQueueName(String receiveQueueName)
    {
        this.receiveQueueName = receiveQueueName;
    }

    public void setSendQueueName(String sendQueueName)
    {
        this.sendQueueName = sendQueueName;
    }

    public void setMessageConsumer(MessageConsumer messageConsumer)
    {
        this.messageConsumer = messageConsumer;
    }
    
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    private Connection getConsumerConnection() throws JMSException
    {
        if (consumerConnection == null)
        {
            ConnectionFactory connectionFactory = 
                    new ConnectionFactoryImpl(host, port, username, password);
            consumerConnection = connectionFactory.createConnection();
            
        }
        return consumerConnection;
    }
    
    private Connection getProducerConnection() throws JMSException
    {
        if (producerConnection == null)
        {
            ConnectionFactory connectionFactory = 
                    new ConnectionFactoryImpl(host, port, username, password);
            producerConnection = connectionFactory.createConnection();
            
        }
        return producerConnection;
    }
    
    private Session getConsumerSession() throws JMSException
    {
        if (consumerSession == null)
        {
            consumerSession = getConsumerConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        return consumerSession;
    }
    
    private Session getProducerSession() throws JMSException
    {
        if (producerSession == null)
        {
            producerSession = getProducerConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        }
        return producerSession;
    }
    
    private org.apache.qpid.amqp_1_0.jms.MessageProducer getDefaultMessageProducer() throws JMSException
    {
        if (defaultMessageProducer == null)
        {
            Queue sendQueue = getProducerSession().createQueue(sendQueueName);
            defaultMessageProducer = getProducerSession().createProducer(sendQueue);
        }
        return defaultMessageProducer;
    }
    
    private org.apache.qpid.amqp_1_0.jms.MessageProducer getMessageProducer(String queueName) throws JMSException
    {
        if (sendQueueName.equals(queueName))
        {
            return getDefaultMessageProducer();
        }
        Queue queue = getProducerSession().createQueue(queueName);
        return getProducerSession().createProducer(queue);
    }
    
    public void send(Object message) {
        send(message, sendQueueName);
    }
    
    @Override
    public void send(Object message, Map<String, Object> headers) throws MessagingException
    {
        send(message, sendQueueName, headers);
    }
    
    @Override
    public void send(Object message, String queueName, Map<String, Object> headers) throws MessagingException
    {
        // TODO: Would need to inspect all headers sent in to detect type and/or JMS property key
        throw new UnsupportedOperationException("Headers not currently supported");
    }
    
    public void send(Object message, String queueName) {
        try
        {
            Writer strWriter = new StringWriter();
            objectMapper.writeValue(strWriter, message);
            String stringMessage = strWriter.toString();

            if (StringUtils.isEmpty(queueName))
            {
                queueName = sendQueueName;
            }
            
            TextMessage textMessage = getProducerSession().createTextMessage(stringMessage);
            
            if (logger.isTraceEnabled())
            {
                logger.trace("Sending message to " + host + ":" + queueName + ": " + stringMessage);
            }
            getMessageProducer(queueName).send(textMessage);
        }
        catch (Exception e)
        {
            throw new MessagingException("Error sending message", e);
        }
    }
    
    public void startListener() {
        if (listener == null)
        {
            listener = new AmqpListener();
        }
        listener.run();
    }
    
    public AmqpListener getListener()
    {
        if (listener == null)
        {
            listener = new AmqpListener();
        }
        return listener;
    }
    
    public boolean isInitialized()
    {
        return listener != null && listener.isInitialized;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName() + "[");
        builder.append("host: " + host);
        builder.append(", ");
        builder.append("port: " + port);
        builder.append(", ");
        builder.append("username: " + username);
        builder.append(", ");
        builder.append("sendQueueName: " + sendQueueName);
        builder.append(", ");
        builder.append("receiveQueueName: " + receiveQueueName);
        builder.append(", ");
        builder.append("isInitialized: " + isInitialized());
        builder.append("]");
        return builder.toString();
    }

}
