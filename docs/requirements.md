## **Requirements**

**Parrot Stream** would not exist without:

* [**Debezium**](http://debezium.io/): it is thanks to Debezium Connectors that data stream from the sources to the Kafka topics
* [**Apache Kafka**](https://github.com/apache/kafka.git): it is actually widely used for building real-time data pipelines thanks to its strenght in horizontal scalability, fault tolerance and extremely low latency
* [**Confluent Schema Registry**](http://docs.confluent.io/current/schema-registry/docs/index.html): rovides a serving layer for your metadata. It provides a RESTful interface for storing and retrieving Avro schemas. It stores a versioned history of all schemas, provides multiple compatibility settings and allows evolution of schemas according to the configured compatibility setting. It provides serializers that plug into Kafka clients that handle schema storage and retrieval for Kafka messages that are sent in the Avro format
* [**Confluent Kafka Connect**](http://docs.confluent.io/current/connect/index.html): Kafka Connect is a framework for scalably and reliably streaming data between Apache Kafka and other data systems. Parrot Stream is a Kafka Connect distribution which come with its own source and sink connectors togheter with other Confluent and community ones

