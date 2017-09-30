To start **Parrot** the required dependent services are **ZooKeeper** and **Kafka**.

1. Start **ZooKeeper** and Kafka:

        ./parrot start -s=zookeeper,kafka

2. Check if **ZooKeeper** is up with:

        curl http://localhost:8080/commands/configuration

    You should get a json respone like the following:

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

        curl http://localhost:8082/topics

    You should get a json empty array:

        []
        
    because there are still no Kafka topics in the cluster.

4. Start **Parrot**:

        ./parrot start
        
5. Check if **Parrot** is up with:

        curl http://localhost:8083/connectors

   You should get a json empty array:

        []
        
   because there are still no **Parrot connectors** defined.

