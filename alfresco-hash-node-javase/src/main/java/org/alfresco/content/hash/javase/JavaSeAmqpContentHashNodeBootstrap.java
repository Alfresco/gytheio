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
package org.alfresco.content.hash.javase;

import org.alfresco.content.handler.FileContentReferenceHandlerImpl;
import org.alfresco.content.hash.BaseContentHashNode;
import org.alfresco.messaging.amqp.AmqpDirectEndpoint;
import org.alfresco.messaging.amqp.AmqpNodeBootstrapUtils;

/**
 * Starts up an AMQP Java SE hash node via command line arguments
 * 
 * @author Ray Gauss II
 */
public class JavaSeAmqpContentHashNodeBootstrap
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        AmqpNodeBootstrapUtils.validateArguments(args);
        
        JavaSeContentHashNodeWorker worker = new JavaSeContentHashNodeWorker();
        worker.setContentReferenceHandler(new FileContentReferenceHandlerImpl());
        
        BaseContentHashNode node = new BaseContentHashNode();
        node.setWorker(worker);
        
        AmqpDirectEndpoint endpoint = AmqpNodeBootstrapUtils.createEndpoint(node, args);
        node.setMessageProducer(endpoint);
        
        endpoint.startListener();
    }
    
}
