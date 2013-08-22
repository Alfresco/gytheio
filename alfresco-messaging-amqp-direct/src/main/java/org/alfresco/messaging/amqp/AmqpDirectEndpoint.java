package org.alfresco.messaging.amqp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.alfresco.messaging.MessageConsumer;
import org.alfresco.messaging.MessageProducer;
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
                        Object request = objectMapper.readValue(stringMessage, 
                                messageConsumer.getConsumingMessageBodyClass());
                        if (request == null)
                        {
                            logger.error("Request could not be unmarshalled");
                        }
                        else
                        {
                            messageConsumer.onReceive(request);
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
    
    private Sender getSender() throws SenderCreationException, ConnectionException
    {
        if (sender == null)
        {
            sender = getSession().createSender(sendQueueName);
        }
        return sender;
    }
    
    
    public void send(Object message) {
        try
        {
            Writer strWriter = new StringWriter();
            objectMapper.writeValue(strWriter, message);
            String stringMessage = strWriter.toString();
            if (logger.isDebugEnabled())
            {
                logger.debug("Sending message to " + host + ":" + sendQueueName + ": " + stringMessage);
            }
            final Message amqpMessage = new Message(stringMessage);
            getSender().send(amqpMessage);
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
