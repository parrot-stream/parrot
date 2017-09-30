## Dependencies
Parrot CDC would not exist without these great pieces of software:

* [Debezium](http://debezium.io/): it is thanks to Debezium that data stream from the sources to Kafka Topics.
* [Apache Kafka](https://github.com/apache/kafka.git):  and it is thanks to Apache Kafka that Parrot CDC can read the data change event sources 
* [Apache ZooKeeper](https://github.com/apache/zookeeper.git): needed to do the necessary housekeepeing, like cohordinate the Parrot CDC nodes in a cluster, store the processors informations and status, etc.
* [Apache Camel](http://camel.apache.org/): used to implement the Parrot CDC **processors** and **sinks**, with the necessary logic to convert and route the Kafka events to the targets


