
Overview
========

This project is spread out across several functional areas that should be useful for inclusion in many implementations but can be combined for the end result of an **Alfresco messaging content transformer for video**.

The messaging content transformer uses a content transport implementation to make the content available to remote nodes, i.e. shared drive, S3, etc. then use **[Apache Camel](http://camel.apache.org/)** to route transformation requests to **[AMQP](http://www.amqp.org/)** queues on an **[ActiveMQ](http://activemq.apache.org/) broker**. 

Those transformation request messages are then processed by **[FFmpeg](http://www.ffmpeg.org/)** transformer nodes that also send replies to AMQP queues.

Those transformation reply messages are then received by the messaging content transformer to associate the result as a completed content transformation.

Project Layout
==============

Commons
-------

* `alfresco-commons`
* `alfresco-commons-s3`

Serve as the minimal base for many content definitions, handling, 
and utility operations.

Transformation Commons
----------------------

* `alfresco-transformations-commons`

Contains the basic definitions of `TransformationRequest` and `TransformationReply`
objects and the transformation option objects to be sent to content transformers.

Messaging
---------

* `alfresco-messaging-commons`
* `alfresco-messaging-camel`
* `alfresco-messaging-amqp-direct`

Defines generic `MessageConsumer` and `MessageProducer` interfaces and Camel and
AMQP implementations which process and send messages and contains a Jackson-based JSON marshaller.

Transformer Nodes
-----------------

* `alfresco-transformer-node-commons`
* `alfresco-transformer-node-amqp`
* `alfresco-transformer-node-ffmpeg`

Content transformer node which acts as a `MessageConsumer` to process 
`TransformationRequest` objects and sends `TransformationReply` objects 
to a specified `MessageProducer`.

Alfresco Repository AMP
-----------------------

* `alfresco-messaging-repo-amp`

Defines and configures the repository pieces needed for Camel routing and 
a messaging content transformer for video.

ActiveMQ Broker
---------------

* `messaging-broker-activemq`

A convenience project which can start an ActiveMQ broker with a single Maven command.


Usage
=====

1. Ensure FFmpeg is installed
2. Build all modules from the root: `mvn clean install`
3. Build and apply the AMP, see `alfresco-messaging-repo-amp`
4. Start the ActiveMQ broker, see `messaging-broker-activemq`
5. Start at least one transformer node, see ``alfresco-transformer-node-ffmpeg`
6. Start the repository
7. Perform some video transformation, see usage in `alfresco-messaging-repo-amp`

