
Overview
========

Contains an abstract content transformer node which uses the AMQP direct endpoint
from the `alfresco-messaging-amqp-direct` project to consume, unmarshal,
then pass Java object messages to the specified `MessageConsumer` and send replies
via the AMQP direct `MessageProducer`.
