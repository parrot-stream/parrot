# Welcome to **Parrot Streaming**!

**Change Data Capture** (CDC) is an approach to data integration that is based on the identification, capture and delivery of changes made to enterprise data sources.
There are several commercial and proprietary ways to do CDC, directly delivered by database vendor or by data integration software houses, but there is a general lack of solid **open source** solutions.

## Why Parrot?

[**Debezium**](http://debezium.io/) is actually the most promising open source distributed platform for CDC: its goal is streaming changes from several enterprise data sources (ie. MySql, MongoDB, PostgreSql, Oracle, etc.) versus [**Apache Kafka**](https://kafka.apache.org/), where you can attach one of the existing solution to implement the rest of the data streaming pipeline.

There are actually a plethora of highly flexible and powerful solutions to choose:

* [**Kafka Connect**](http://docs.confluent.io/2.0.0/connect/), [**Kafka Streams**](https://kafka.apache.org/documentation/streams/), [**Apache Storm**](http://storm.apache.org/), [**Apache Spark**](https://spark.apache.org/) are all higly general purpose and valid alternatives, which let you (and requires you) to build your full CDC pipeline almost from scratch
* [**Apache Flume**](https://flume.apache.org/), [**Logstash**](https://www.elastic.co/products/logstash), [**Fluentd**](http://www.fluentd.org/) with their agent architecture are mainly used for log streaming, so not really a convenient solution for managing data change streaming pipelines
* [**Apache Chukwa**](http://chukwa.apache.org/), [**Apache NiFi**](https://nifi.apache.org/), [**Gobblin**](https://github.com/linkedin/gobblin), etc. have a focus on ETL and batches or small/micro batches which often compromises the low latency feature which is typically needed by data streaming pipelines

The main goals of Parrot are:

* give you a distribution of existing open source tools to be up & running in minutes with an end to end solution mainly focused with the needs of **Change Data Capture** and **Data Streaming**
* leveraging the power of existing open source tools like Debezium and Kafka Connect to not reinvent the wheel in implementing the solution and, at the same time, to build upon them an enterprise and robust solution
* implementing some Kafka Connect Sink connectors to enable you to define advance pipelines with features like schema evolution, autocreation, streaming data quality, etc.

Actually the following `Kafka Connector Sinks` have been implemented and can be considered at an early development stage:

* **Kudu Connector**
* **HBase Connector**

A lot of work is still needed to achieve a first production ready release, but I think that a first stable one with at least the HBase and Kudu sinks could be ready for the end of 2017!

Follow reading the [**Parrot documentation**](https://parrot-stream.github.io/parrot-docs/) for further information about installation, configuration and contribution to the project!