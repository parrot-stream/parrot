Parrot is built on the following concepts:

1. **source**: a ```Source``` implements the necessary logic specific to the data change originating system. Each originating system (ie. MySql, PostgreSql, MongoDB, Cassandra, Oracle, etc.), apart from some common properties, has its own specific set of information which have to be treated differently. In practice a **source definition** contains:

    * the logical name of a Debezium connector, that is the value of the **database.server.name** property of its configuration
    * the Parrot Java class which implements the source

2. **sink**: a Parrot Sink implements the necessary logic to copy the data change event read from Kafka to the specific target system. So a **sink definition** containes:

    * the Parrot Java class which implements the sink
    * the configuration parameters which defines the sink

3. **processor**:  a Parrot Processor is the combination of a *source* and a *sink* with an *identifier*. Each processor is a Camel route instance which:

    * retrieves data change event reading them from the Kafka topics which corresponds to the Debezium logical connector name defined in the Parrot source
    * routes each data change event through the Parrot Sink

Each Parrot node in a cluster can run several different Processor. The same Processor runs on all node in a cluster, to gain horizontal scalability.
The following picture represents the scenario with one node running Kafka Connect with four Debezium Connectors and one node running Parrot with six Processors:

|Debezium Connector|Source|Type|Parrot Processor|Target|Type|
|------------------|------|----|----------------|------|----|
|DBZ.MS1	   |MS1   |MySql|PRT.DBZ.MS1.K1<br>PRT.DBZ.MS1.HB1|HB1|Kudu<br>HBase|
|DBZ.P1            |P1    |PostgreSql|PRT.DBZ.P1.HB2|HB2|HBase|
|DBZ.P2            |P2    |PostgreSql|PRT.DBZ.P1.K2|K2|Kudu|
|DBZ.MO1           |MO1   |MongoDB|PRT.DBZ.MO1.HB2<br>PRT.DBZ.MO1.K2|HB2<br>K2|HBase<br>Kudu|

![ParrotScenario][parrot_scenario]

Each Parrot Processor works as a Kafka Consumer and belongs to a Kafka Group with id equals to the Processor identifier. In this way the same processor running on multiple nodes in a Parrot cluster can consume a data change event just once.
  
Parrot is deliverable as a Java Enterprise Application, so you have potentially more then one choice to build a Parrot cluster:

* you can distribute the Enterprise Application Archive (ear) in multiple nodes of an existing JEE Application Server cluster
* you can run running Parrot in Docker containers and distribute them in a container management system such as Amazon ECS, OpenShift, Swarm, etc.





[parrot_scenario]: img/parrot_scenario.png "Parrot Scenario"
