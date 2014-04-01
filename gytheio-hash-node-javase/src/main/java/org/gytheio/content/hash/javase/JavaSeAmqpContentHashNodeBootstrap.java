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
package org.gytheio.content.hash.javase;

import org.gytheio.content.hash.BaseContentHashNode;
import org.gytheio.content.transform.AbstractSimpleAmqpNodeBootstrap;
import org.gytheio.messaging.MessageConsumer;

/**
 * Starts up an AMQP Java SE hash node via command line arguments
 * 
 * @author Ray Gauss II
 */
public class JavaSeAmqpContentHashNodeBootstrap extends AbstractSimpleAmqpNodeBootstrap<JavaSeContentHashNodeWorker>
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        JavaSeAmqpContentHashNodeBootstrap bootstrap = new JavaSeAmqpContentHashNodeBootstrap();
        bootstrap.run(args);
    }

    @Override
    protected MessageConsumer getMessageConsumer()
    {
        JavaSeContentHashNodeWorker worker = createWorker();
        worker.setContentReferenceHandler(
                createFileContentReferenceHandler(AbstractSimpleAmqpNodeBootstrap.PROP_WORKER_DIR_SOURCE));
        
        BaseContentHashNode node = new BaseContentHashNode();
        node.setWorker(createWorker());
        
        return node;
    }
    
}
