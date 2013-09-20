package org.alfresco.content.transform.ffmpeg;

import org.alfresco.content.handler.FileContentReferenceHandlerImpl;
import org.alfresco.content.transform.BaseContentTransformerNode;
import org.alfresco.messaging.amqp.AmqpDirectEndpoint;
import org.alfresco.messaging.amqp.AmqpNodeBootstrapUtils;

public class FfmpegAmqpContentTransformerNodeBootstrap
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        AmqpNodeBootstrapUtils.validateArguments(args);
        
        FfmpegContentTransformerNodeWorker worker = new FfmpegContentTransformerNodeWorker();
        worker.setContentReferenceHandler(new FileContentReferenceHandlerImpl());
        worker.init();
        
        BaseContentTransformerNode node = new BaseContentTransformerNode();
        node.setWorker(worker);
        
        AmqpDirectEndpoint endpoint = AmqpNodeBootstrapUtils.createEndpoint(node, args);
        node.setMessageProducer(endpoint);
        
        endpoint.startListener();
    }
    
}
