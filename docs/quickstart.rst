Quickstart
##########

**Parrot Stream** would not exist without these great pieces of software:

* Debezium_: it is thanks to **Debezium** Connectors that data stream from the sources to the Kafka topics
* `Apache Kafka`_: **Kafka** is actually widely used for building real-time data pipelines thanks to its strength in horizontal scalability, fault tolerance and extremely low latency
* `Confluent Schema Registry`_: provides a serving layer for your metadata. It provides a RESTful interface for storing and retrieving Avro schemas. It stores a versioned history of all schemas, provides multiple compatibility settings and allows evolution of schemas according to the configured compatibility setting. It provides serializers that plug into Kafka clients that handle schema storage and retrieval for Kafka messages that are sent in the Avro_ format
* `Confluent Kafka Connect`_: **Kafka Connect** is a framework for scalably and reliably streaming data between Apache Kafka and other data systems. Parrot Stream is a Kafka Connect distribution which come with its own source and sink connectors together with other Confluent and community ones

Distributions
*************

**Parrot Stream** comes in three flavours, at the moment:

* a `Cloudera Distribution`_ you can easily distribute Parrot Stream as parcels into a **Cloudera Cluster** and manage it with `Cloudera Manager`_. You can donwload Cloudera Manager and use it in its *Express Edition* to create and manage your cluster in an on premise configuration or you can setup a cluster deployment in `Amazon AWS EC2`_, `Google Cloud Platform`_ or `Microsoft Azure`_ with `Cloudera Director`_.
* a `Dockerized Distribution`_ you can easily distribute Parrot Strea as a docker.
* a `Development Distribution`_ with all the necessary components to make the Parrot Stream up & running in minutes. This kind of distribution is intended just for development and testing purposes.

.. _Debezium: http://debezium.io/
.. _Apache Kafka: https://github.com/apache/kafka.git
.. _Confluent Schema Registry: http://docs.confluent.io/current/schema-registry/docs/index.html
.. _Avro: https://avro.apache.org/
.. _Confluent Kafka Connect: http://docs.confluent.io/current/connect/index.html
.. _Cloudera Distribution: distribution/cloudera-csds/index.html
.. _Cloudera Manager: https://www.cloudera.com/products/product-components/cloudera-manager.html
.. _Amazon AWS EC2: https://aws.amazon.com/ec2/
.. _Google Cloud Platform: https://cloud.google.com/
.. _Microsoft Azure: https://azure.microsoft.com
.. _Cloudera Director: https://www.cloudera.com/products/product-components/cloudera-director.html
.. _Dockerized Distribution: distribution/dockers/index.html
.. _Development Distribution: distribution/development/index.html 
