
General Overview
================

This project uses several components of Gytheio for the end result of an
 **Alfresco messaging content transformer for video** and an **Alfresco messaging hash service**.


Messaging Content Transformer
-----------------------------

The messaging content transformer uses a content transport implementation 
to make the content available to remote nodes, i.e. shared drive, S3, etc. 
then uses **[Apache Camel](http://camel.apache.org/)** to route 
transformation requests to **[AMQP](http://www.amqp.org/)** queues on 
an **[ActiveMQ](http://activemq.apache.org/) broker**. 

Those transformation request messages are then processed by 
**[FFmpeg](http://www.ffmpeg.org/)** transformer nodes that also send 
replies to AMQP queues.

Those transformation reply messages are then received by the messaging 
content transformer to associate the result as a completed content transformation.

![AlfrescoOne Messaging Transformations](https://github.com/Alfresco/alfresco-transformations/blob/master/doc/resources/images/transformations-alfresoone-messaging.png?raw=true)

Messaging Hash Service
----------------------

The messaging hash service uses a new `HashService` implementation to 
make the content available to remote nodes, i.e. shared drive, S3, etc. 
then uses **[Apache Camel](http://camel.apache.org/)** to route hash requests 
to **[AMQP](http://www.amqp.org/)** queues on an **[ActiveMQ](http://activemq.apache.org/) broker**. 

Those hash request messages are then processed by **Java SE** hash nodes 
that also send replies to AMQP queues.

Those hash reply messages are then received by the `HashService` which are 
currently just logged but in the future will be recorded in a property on the content node. 


Technical Overview
==================

An Alfresco repository AMP which:

1. Defines a Messaging subsystem which defines an Apache Camel context and
routes with a default configuration sending and receiving AMQP messages
from an ActiveMQ broker.
2. Defines abstract, *asynchronous* Alfresco content transformer and worker
interfaces and classes.
3. Defines a `ContentTransport` which uses a `ContentReferenceHandler` to
read and write from `ContentReader` and `ContentWriter` objects to 
(possibly remote) `ContentReference` objects.
4. Defines a `MessagingContentTransformerWorkerImpl` which uses a `MessageProducer`
to send `TransformationRequest` objects and can consume `TransformationReply` objects
to update status and read the result of a completed transformation.
5. Configures an *instance* of a messaging content transformer which supports
various video transformations.
6. Defines a `HashService` interface for computing hashes of `ContentReader` objects
7. Defines a `HashServiceMessagingImpl` which uses a `MessageProducer` to send `HashRequest` objects and can consume `HashReply` objects to retrieve the results of the hash computation

Building
========

To build the AMP:

   `mvn clean package`
   
and the result will reside in:

   `target/alfresco-messaging-repo-amp-1.0-SNAPSHOT.amp`
   
You can use the `applyAmps` profile to apply the AMP to a specified `war.file.*type*` property, i.e.:

   `mvn clean install -DapplyAmps -Dwar.file.share=/opt/tomcat/webapps/alfresco.war`

or let the plugin use your `CURRENT_PROJECT` environment variable for devenv and just execute:

   `mvn clean install -DrunTests -DapplyAmps -P wars-from-devenv-exploded`

or let the plugin pull the artifacts from Maven using:

   `mvn clean install -DapplyAmps -P wars-from-maven`
   
Usage
=====

Be sure an ActiveMQ broker (see `messaging-broker-activemq`) and at least one 
transformer node (`alfresco-transformer-node-ffmpeg`) are running.

Drop a video into Share then refresh and you should see a thumbnail.

Any action which would request a video transformation should work, such
as a copy and transform action.

Hashes are requested `onContentUpdate` but the results are currently only logged.  
In the future the value will be stored in a property on the content node, 
and in the case of `Versionable` nodes directly with the version aspect.

