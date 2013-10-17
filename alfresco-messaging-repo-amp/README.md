
Overview
========

An Alfresco repository AMP which defines a Messaging subsystem including an Apache Camel context and
routes with a default configuration sending and receiving AMQP messages
from an ActiveMQ broker.


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
   
