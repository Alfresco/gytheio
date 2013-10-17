/*
 * Copyright (C) 2005-2013 Alfresco Software Limited.
 *
 * This file is part of an Alfresco messaging investigation
 *
 * The Alfresco messaging investigation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Alfresco messaging investigation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Alfresco messaging investigation. If not, see <http://www.gnu.org/licenses/>.
 */
package org.gytheio.messaging.amqp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.qpid.amqp_1_0.client.Connection;
import org.apache.qpid.amqp_1_0.client.ConnectionException;
import org.apache.qpid.amqp_1_0.client.Message;
import org.apache.qpid.amqp_1_0.client.Receiver;
import org.apache.qpid.amqp_1_0.client.Sender;
import org.apache.qpid.amqp_1_0.client.Session;
import org.apache.qpid.amqp_1_0.client.Sender.SenderCreationException;
import org.apache.qpid.amqp_1_0.type.Section;
import org.apache.qpid.amqp_1_0.type.UnsignedInteger;
import org.apache.qpid.amqp_1_0.type.messaging.AmqpValue;
import org.gytheio.messaging.MessageConsumer;
import org.gytheio.messaging.MessageProducer;
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

    private static final int RECEIVER_CREDIT = 2000;

    private String host;
    private String receiveQueueName;
    private String sendQueueName;
    
    private Connection connection;
    private Session session;
    private Sender sender;
    
    private MessageConsumer messageConsumer;
    private AmqpListener listener;
    private ObjectMapper objectMapper;
    
    protected class AmqpListener implements Runnable
    {
        public void run()
        {
            Connection connection = null;
            try
            {
                Receiver receiver = getSession().createReceiver(receiveQueueName);
                receiver.setCredit(UnsignedInteger.valueOf(RECEIVER_CREDIT), false);
                
                while (true)
                {
                    logger.info("Waiting for an AMQP message on " + host + ":" + receiveQueueName);
                    Message message = receiver.receive();
                    logger.debug("Processing AMQP message");
                    String stringMessage = null;
                    List<Section> sections = message.getPayload();
                    for (Section section : sections)
                    {
                        if (section instanceof AmqpValue)
                        {
                            AmqpValue value = (AmqpValue) section;
                            if (!(value.getValue() instanceof String))
                            {
                                logger.error("Only string message bodies supported");
                                continue;
                            }
                            stringMessage = (String) value.getValue();
                        }
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
                                   String replyQueueName = message.getProperties().getReplyTo();
                                   if (!StringUtils.isEmpty(replyQueueName))
                                   {
                                       ((Request<?>) pojoMessage).setReplyTo(replyQueueName);
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
                    
                    receiver.acknowledge(message.getDeliveryTag());
                }
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
            finally
            {
                if (connection != null)
                {
                    connection.close();
                    
                }
            }
        }
    }
    
    public void setHost(String host)
    {
        this.host = host;
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

    private Connection getConnection() throws ConnectionException
    {
        if (connection == null)
        {
            connection = new Connection(host, 5672, "guest", "password");
            
        }
        return connection;
    }
    
    private Session getSession() throws ConnectionException
    {
        if (session == null)
        {
            session = new Session(getConnection(), "RemoteSimpleTransformerSession");
        }
        return session;
    }
    
    private Sender getDefaultSender() throws SenderCreationException, ConnectionException
    {
        if (sender == null)
        {
            sender = getSession().createSender(sendQueueName);
        }
        return sender;
    }
    
    private Sender getSender(String queueName) throws SenderCreationException, ConnectionException
    {
        if (sendQueueName.equals(queueName))
        {
            return getDefaultSender();
        }
        return getSession().createSender(queueName);
    }
    
    public void send(Object message) {
        send(message, sendQueueName);
    }
    
    public void send(Object message, String queueName) {
        try
        {
            Writer strWriter = new StringWriter();
            objectMapper.writeValue(strWriter, message);
            String stringMessage = strWriter.toString();

            ArrayList<Section> sections = new ArrayList<>(2);
            sections.add(new AmqpValue(stringMessage));
            if (StringUtils.isEmpty(queueName))
            {
                queueName = sendQueueName;
            }
            
            final Message amqpMessage = new Message(sections);
            
            if (logger.isDebugEnabled())
            {
                logger.debug("Sending message to " + host + ":" + queueName + ": " + stringMessage);
            }
            getSender(queueName).send(amqpMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void startListener() {
        if (listener == null)
        {
            listener = new AmqpListener();
        }
        listener.run();
    }

}
