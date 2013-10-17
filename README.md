
Overview
========

Gytheio (pronounced yee-thee-oh) is a distributed content/file investigation and manipulation framework which leverages messaging queues to perform common low-level tasks like:

  * Content change handling
  * Transformations
  * Metadata extraction
  * Hash / checksum computation
…

These tasks can be resource intensive so the focus of the project is on nodes that are as lightweight as possible allowing for an extremely scalable platform.

There are several conceptual pieces to the framework which can be mixed and matched in whatever manner best fits your needs, though several, common end-to-end implementations are available.

* Client: Task messages are generated and sent which contain a reference to the source content and other options needed depending on the task such as a target reference, media type, hash algorithm, etc.  That content reference could be anything the task nodes know how to deal with: a file on a shared volume, an S3 path, a CMIS document ID, etc.

* Routing: A message routing system then directs the request to the appropriate queue for consumption by processing nodes.

* Task Nodes: Processing nodes listen for messages on a relevant queue and call on workers to perform the task on the source content reference, possibly sending a reply which can be consumed by the original requestor or elsewhere.

Of course, you can produce messages by other means, plugin a different messaging system and routing, or even use node workers locally and forgo messaging altogether.


Project Layout
==============

Commons
-------

* `gytheio-commons`
* `gytheio-commons-s3`

Serve as the minimal base for many content definitions, handling, 
and utility operations.

Transform Commons
----------------------

* `gytheio-transform-commons`

Contains the basic definitions of `TransformationRequest` and `TransformationReply`
objects and the transformation option objects to be sent to content transformers.

Hash Commons
----------------------

* `gytheio-hash-commons`

Contains the basic definitions of `HashRequest` and `HashReply`
objects to be sent to content hash nodes.

Messaging
---------

* `gytheio-messaging-commons`
* `gytheio-messaging-camel`
* `gytheio-messaging-amqp-direct`

Defines generic `MessageConsumer` and `MessageProducer` interfaces and Camel and
AMQP implementations which process and send messages and contains a Jackson-based JSON marshaller.

Transformer Nodes
-----------------

* `gytheio-transform-node-commons`
* `gytheio-transform-node-ffmpeg`

Content transformer node which acts as a `MessageConsumer` to process 
`TransformationRequest` objects and sends `TransformationReply` objects 
to a specified `MessageProducer`.

Hash Nodes
-----------------

* `gytheio-hash-node-commons`
* `gytheio-hash-node-javase`

Content hash node which acts as a `MessageConsumer` to process 
`HashRequest` objects and sends `HashReply` objects 
to a specified `MessageProducer`.

ActiveMQ Broker
---------------

* `messaging-broker-activemq`

A convenience project which can start an ActiveMQ broker with a single Maven command.


