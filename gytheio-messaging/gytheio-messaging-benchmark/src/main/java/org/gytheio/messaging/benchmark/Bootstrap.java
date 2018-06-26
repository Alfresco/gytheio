/*
 * Copyright (C) 2005-2018 Alfresco Software Limited.
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
package org.gytheio.messaging.benchmark;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Boostrap which creates and runs a {@link BenchmarkRunner}
 * 
 * @author Ray Gauss II
 */
public class Bootstrap
{
    private static final Log logger = LogFactory.getLog(Bootstrap.class);

    protected static final String USAGE_MESSAGE = 
            "\n\nUSAGE: brokerUrl numMessages [un=username] [pw=password] "
            + "[endpointSend] [endpointReceive] [consume-only] [produce-only]\n\n"
            + "\tbrokerUrl\tThe broker URL, examples: tcp://localhost:61616, amqp://my.host.test:5672, ampqs://my.host.test:5671, amqp+ssl://my.host.test:5671\n"
            + "\tusername\tThe broker username, example: un=admin\n"
            + "\tpassword\tThe broker password, example: pw=mysecretpassword\n"                   
            + "\tnumMessages\tThe number of messages to send and/or expect\n"
            + "\tendpointSend\tThe endpoint to send messages to, default: queue:gytheio.test.benchmark\n"
            + "\tendpointReceive\tThe endpoint to consumer messages from, default: queue:gytheio.test.benchmark\n"
            + "\tconsume-only\tConsume only, do not produce messages\n"
            + "\tproduce-only\tProduce only, do not consumer messages\n";

    public static void main(String[] args)
    {
        BootstrapArguments argsObject = parse(args);
        try
        {
            BenchmarkRunner runner = new BenchmarkRunner(
                    argsObject.brokerUrl, 
                    argsObject.brokerUsername,
                    argsObject.brokerPassword,
                    argsObject.endpointSend, 
                    argsObject.endpointReceive, 
                    argsObject.numMessages, 
                    argsObject.runProducer, 
                    argsObject.runConsumer);
            runner.runBenchmark();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }
    
    public static BootstrapArguments parse(String args[])
    {
        if (args.length < 2)
        {
            throw new IllegalArgumentException(USAGE_MESSAGE);
        }
        
        BootstrapArguments argsObject = new BootstrapArguments();
        
        argsObject.brokerUrl = args[0];
        
        argsObject.numMessages = Integer.valueOf(args[1]);
        
        for (int i = 2; i < 8; i++)
        {
            if (args.length > i)
            {
                if (args[i].startsWith("un="))
                {
                    String[] split = args[i].split("un=");
                    if (split.length == 2)
                    {
                        argsObject.brokerUsername = split[1];
                    }
                }
                else if (args[i].startsWith("pw="))
                {
                    String[] split = args[i].split("pw=");
                    if (split.length == 2)
                    {
                        argsObject.brokerPassword = split[1];
                    }
                }
                else if (args[i].equals("consume-only"))
                {
                    argsObject.runProducer = false;
                }
                else if (args[i].equals("produce-only"))
                {
                    argsObject.runConsumer = false;
                }
                else if (argsObject.endpointSend == null && isSupportedEndpoint(args[i]))
                {
                    argsObject.endpointSend = args[i];
                }
                else if (argsObject.endpointReceive == null && isSupportedEndpoint(args[i]))
                {
                    argsObject.endpointReceive = args[i];
                }
            }
        }
        
        return argsObject;
    }
    
    protected static boolean isSupportedEndpoint(String endpoint)
    {
        return endpoint != null && (endpoint.startsWith("queue") || endpoint.startsWith("topic"));
    }
}
