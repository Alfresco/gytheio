
Overview
========

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

Hashes are requested `onContentUpdate` but the results are currently only logged.  In the future the value will be stored in a property on the content node, and in the case of `Versionable` nodes directly with the version aspect.

