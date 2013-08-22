package org.alfresco.content.transform.ffmpeg;

import org.alfresco.content.handler.FileContentReferenceHandlerImpl;
import org.alfresco.content.transform.AbstractAmqpContentTransformerNode;

public class FfmpegAmqpContentTransformerNode extends AbstractAmqpContentTransformerNode
{
    
    public FfmpegAmqpContentTransformerNode(String host, String receiveQueueName, String replyQueueName)
    {
        this.transformerWorker = new FfmpegContentTransformerNodeWorker();
        ((FfmpegContentTransformerNodeWorker) this.transformerWorker).setContentReferenceHandler(
                new FileContentReferenceHandlerImpl());
        ((FfmpegContentTransformerNodeWorker) this.transformerWorker).init();
        this.host = host;
        this.receiveQueueName = receiveQueueName;
        this.replyQueueName = replyQueueName;
        init();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        validateArguments(args);
        new FfmpegAmqpContentTransformerNode(args[0], args[1], args[2]);
    }
    
}
