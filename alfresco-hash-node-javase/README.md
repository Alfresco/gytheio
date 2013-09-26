
Overview
========

A Java SE AMQP content hash node.

The `JavaSeAmqpContentHashNodeBootstrap` itself is really just a convenience class
to configure and instantiate an AMQP hash node and start it's message listener.

The `JavaSeContentHashNodeWorker` contains the code which computes a hash 
of File objects.

Usage
=====

First build the jar via Maven:

    mvn clean install

either from the parent folder or this project.

Start an AMQP broker.  See the `messaging-broker-activemq` project.

The single jar with dependencies can be launched with parameters passed for the 
AMQP broker host, request queue, and reply queue, i.e.:

    java -jar target/alfresco-hash-node-javase-1.0-SNAPSHOT-jar-with-dependencies.jar localhost alfresco.hash.request alfresco.hash.reply

You should see a message indicating that the node is waiting for a message.

Once a message is received the node will perform the hash computation and send a reply
with the value.