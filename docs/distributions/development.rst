Development Distribution
########################

Preparing the development environment
*************************************

To prepare the development environment you have to install:

* Git_
* Maven_
* `Java SE 7`_
* `Docker Community Edition`_

and clone the `Parrot Stream CSD GitHub repository`_ with:

.. highlight:: bash

::

    git clone https://github.com/parrot-stream/parrot.git


Full Environment
****************

To start a **Full Development Environment** you have just to run:

::

    ./parrot start

At the first time all needed Docker images will be downloaded to prepare your development environment and this will take a while so be patient and go grab a beverage!

The Full Development Environment includes the minimum components needed for running **Parrot Stream** which are:

* Apache ZooKeeper
* Apache Kafka

plus all the components needed to the **Parrot Sink connectors**, such as:

* Apache Hadoop (needed for running Hive and Impala)
* PostgreSQL (needed for configuring the Hive metastore)
* Apache Hive (for running the Parrot HBase/Kudu connect with Hive external table option)
* Apache Impala (for running the Parrot HBase/Kudu connect with Impala external table option)
* Apache HBase (for running the Parrot HBase Sink connector)
* Apache Kudu (for running the Parrot Kudu Sink connector)

plus all the components needed to test a full **data streaming pipeline** with **Debezium** such as:

* MongoDB
* MySQL

plus useful tools to explore the data and the Parrot Stream configurations, such as:

* Hue
* Kafka Topics UI
* Kafka Connect UI
* Schema Registry UI

This is a lot of stuff which requires around **7 GiB** of free memory (but **10 GiB** are recomended) so take this into account before trying to start this kind of environment.

Minimum Environment
*******************

To start a **Minimum Development Environment** you have just to start **ZooKeeper**, **Kafka** and **Parrot Stream** itself.

1. Start **ZooKeeper** and **Kafka**:

   ::

       ./parrot start -s=zookeeper,kafka

2. Check if **ZooKeeper** is up with:

   ::

       curl http://localhost:8080/commands/configuration

   You should get a json respone like the following:

   ::

       {
         "client_port" : 2181,
         "data_dir" : "/tmp/zookeeper/version-2",
         "data_log_dir" : "/tmp/zookeeper/version-2",
         "tick_time" : 2000,
         "max_client_cnxns" : 60,
         "min_session_timeout" : 4000,
         "max_session_timeout" : 40000,
         "server_id" : 0,
         "command" : "configuration",
         "error" : null
       }

3. Check if **Kafka** is up with:

   ::

       curl http://localhost:8082/topics

   You should get a json empty array:

   ::

       []

   because there are still no Kafka topics in the cluster.


4. Start **Parrot**:

   ::

       ./parrot start

5. Check if **Parrot** is up with:

   ::

       curl http://localhost:8083/connectors

   You should get a json empty array:

   ::

        []
        

   because there are still no **Parrot connectors** defined.

   Check that all Kafka Connect topics have been correctly created running:

   ::

         curl http://localhost:8082/topics

   you should now get a json array which lists all the the Kafka topics created by **Parrot Stream**:
   
   ::

       [
         "__consumer_offsets",
         "_schemas",
         "connect-configs",
         "connect-offsets",
         "connect-statuses"
       ]

.. _Development Distribution: distribution/development/index.html
.. _Git: https://git-scm.com/download/linux
.. _Maven: https://maven.apache.org
.. _Java SE 7: http://www.oracle.com/technetwork/java/javase/downloads/index.html
.. _Docker Community Edition: https://www.docker.com/community-edition
.. _Parrot Stream CSD GitHub repository: https://github.com/parrot-stream/parrot.git
