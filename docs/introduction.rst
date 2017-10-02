Introduction
############

**Change Data Capture** (CDC) is an approach to data integration that is based on the identification, capture and delivery of changes made to enterprise data sources. There are several commercial and proprietary ways to do CDC, directly delivered by database vendor or by data integration software houses, but there is a general lack of solid **Open Source** solutions.

Why Parrot Stream?
******************

Debezium_ is actually the most promising open source distributed platform for CDC: its goal is **streaming** changes from several enterprise data sources (ie. MySql, MongoDB, PostgreSql, Oracle, etc.) versus `Apache Kafka`_, where you can attach one of the existing solution to implement the rest of the **data streaming pipeline**.

There are actually a plethora of highly flexible and powerful solutions
to choose:

* `Kafka Connect`_, `Kafka Streams`_, `Apache Storm`_, `Apache Spark`_ are all highly general purpose and valid alternatives, which let you build your full CDC pipeline almost from scratch
* `Apache Flume`_, Logstash_, Fluentd_ with their agent architecture are mainly used for log streaming, so not really a convenient solution to manage data change streaming pipelines
* `Apache Chukwa`_, `Apache NiFi`_, Gobblin_, etc. have a focus on ETL and batches or small/micro batches which often compromise the low latency feature which is typically needed by data streaming pipelines

So the main goals of **Parrot Stream** are:

* give you a **distribution** of *existing open source tools* to be up & running in minutes with an end to end solution mainly focused with the needs of *Change Data Capture*
* leverage the power of existing open source tools like *Debezium* and *Kafka Connect* to not reinvent the wheel in implementing the solution and, at the same time, to build upon them an enterprise and robust solution
* implement some **Kafka Connect Sink** connectors to enable you to define advanced data streaming pipelines with features like *schema evolution*, *autocreation*, *error management*, *streaming data quality*.

Actually the following **Kafka Connector Sinks** have been implemented and can be considered at an early development stage:

-  **Kudu Connector**
-  **Hbase Connector**

A lot of work is still needed to achieve a first production ready release, but a first stable release with at least the HBase and Kudu sinks could be ready for the end of 2017!

.. _Debezium: http://debezium.io/
.. _Apache Kafka: https://kafka.apache.org
.. _Kafka Connect: http://docs.confluent.io/2.0.0/connect
.. _Kafka Streams: https://kafka.apache.org/documentation/streams/
.. _Apache Storm: http://storm.apache.org/
.. _Apache Spark: https://spark.apache.org
.. _Apache Flume: https://flume.apache.org
.. _Logstash: https://www.elastic.co/products/logstash
.. _Fluentd: http://www.fluentd.org
.. _Apache Chukwa: http://chukwa.apache.org/
.. _Apache NiFi: https://nifi.apache.org/
.. _Gobblin: https://github.com/linkedin/gobblin
