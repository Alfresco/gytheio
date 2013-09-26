
Overview
========

This project is spread out across several functional areas that should be useful for inclusion in many implementations but can be combined for the end result of an **Alfresco messaging content transformer for video** and an **Alfresco messaging hash service**.

Messaging Content Transformer
-----------------------------

The messaging content transformer uses a content transport implementation to make the content available to remote nodes, i.e. shared drive, S3, etc. then uses **[Apache Camel](http://camel.apache.org/)** to route transformation requests to **[AMQP](http://www.amqp.org/)** queues on an **[ActiveMQ](http://activemq.apache.org/) broker**. 

Those transformation request messages are then processed by **[FFmpeg](http://www.ffmpeg.org/)** transformer nodes that also send replies to AMQP queues.

Those transformation reply messages are then received by the messaging content transformer to associate the result as a completed content transformation.

![AlfrescoOne Messaging Transformations](https://github.com/Alfresco/alfresco-transformations/blob/master/doc/resources/images/transformations-alfresoone-messaging.png?raw=true)

Messaging Hash Service
----------------------

The messaging hash service uses a new `HashService` implementation to make the content available to remote nodes, i.e. shared drive, S3, etc. then uses **[Apache Camel](http://camel.apache.org/)** to route hash requests to **[AMQP](http://www.amqp.org/)** queues on an **[ActiveMQ](http://activemq.apache.org/) broker**. 

Those hash request messages are then processed by **Java SE** hash nodes that also send replies to AMQP queues.

Those hash reply messages are then received by the `HashService` which are currently just logged but in the future will be recorded in a property on the content node. 


Project Layout
==============

Commons
-------

* `alfresco-commons`
* `alfresco-commons-s3`

Serve as the minimal base for many content definitions, handling, 
and utility operations.

Transform Commons
----------------------

* `alfresco-transform-commons`

Contains the basic definitions of `TransformationRequest` and `TransformationReply`
objects and the transformation option objects to be sent to content transformers.

Hash Commons
----------------------

* `alfresco-hash-commons`

Contains the basic definitions of `HashRequest` and `HashReply`
objects to be sent to content hash nodes.

Messaging
---------

* `alfresco-messaging-commons`
* `alfresco-messaging-camel`
* `alfresco-messaging-amqp-direct`

Defines generic `MessageConsumer` and `MessageProducer` interfaces and Camel and
AMQP implementations which process and send messages and contains a Jackson-based JSON marshaller.

Transformer Nodes
-----------------

* `alfresco-transform-node-commons`
* `alfresco-transform-node-ffmpeg`

Content transformer node which acts as a `MessageConsumer` to process 
`TransformationRequest` objects and sends `TransformationReply` objects 
to a specified `MessageProducer`.

Hash Nodes
-----------------

* `alfresco-hash-node-commons`
* `alfresco-hash-node-javase`

Content hash node which acts as a `MessageConsumer` to process 
`HashRequest` objects and sends `HashReply` objects 
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


FFmpeg Transformation Usage
===========================

1. Ensure FFmpeg is installed
2. Build all modules from the root: `mvn clean install`
3. Build and apply the AMP, see `alfresco-messaging-repo-amp`
4. Start the ActiveMQ broker, see `messaging-broker-activemq`
5. Start at least one transformer node, see ``alfresco-transform-node-ffmpeg`
6. Start the repository
7. Perform some video transformation, see usage in `alfresco-messaging-repo-amp`

