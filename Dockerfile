FROM parrotstream/kafka:0.10.2.0

MAINTAINER Matteo Capitanio <matteo.capitanio@gmail.com>

USER root

ENV KAFKA_CONNECT_DATAMOUNTAINEER_VER 0.2.5
ENV KAFKA_CONNECT_DEBEZIUM_VER 0.5.1

ENV MAVEN_VER 3.5.0
ENV KAFKA_CONNECT_PLUGINS_DIR=/usr/share/java

RUN yum makecache fast
RUN wget http://dl.bintray.com/sbt/rpm/sbt-0.13.5.rpm; \
    yum localinstall -y sbt-0.13.5.rpm
RUN yum update -y

# Install Confluent Schema Registry
RUN yum install -y confluent-schema-registry

# Install Confluent Source & Sink Connectors
RUN yum install -y confluent-kafka-connect-jdbc confluent-kafka-connect-elasticsearch confluent-kafka-connect-hdfs confluent-kafka-connect-s3
RUN yum clean all

# Install Maven
RUN curl -fSL -o /tmp/maven.tar.gz http://it.apache.contactlab.it/maven/maven-3/$MAVEN_VER/binaries/apache-maven-$MAVEN_VER-bin.tar.gz; \
    tar -xzf /tmp/maven.tar.gz -C /opt; \
    mv /opt/apache-maven-$MAVEN_VER /opt/maven
ENV PATH $PATH:/opt/maven/bin

######################################################################################
# Conlfuent Certified Connectors
######################################################################################

# Install Azure Kafka Connector
ENV KAFKA_CONNECT_AZURE_VER 0.6
ENV KAFKA_CONNECT_AZURE_PLUGIN_DIR $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-azure
#RUN mkdir -p $KAFKA_CONNECT_AZURE_PLUGIN_DIR; \
#    curl -fSL -o $KAFKA_CONNECT_AZURE_PLUGIN_DIR/kafka-connect-iothub-assembly_$SCALA_VER-$KAFKA_CONNECT_AZURE_VER.jar \
#                 https://github.com/Azure/toketi-kafka-connect-iothub/releases/download/v$KAFKA_CONNECT_AZURE_VER/kafka-connect-iothub-assembly_$SCALA_VER-$KAFKA_CONNECT_AZURE_VER.jar

# Install Couchbase Source Kafka Connector
ENV KAFKA_CONNECT_COUCHBASE_VER 3.0.0-DP4
ENV KAFKA_CONNECT_COUCHBASE_PLUGIN_DIR $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-couchbase
RUN curl -fSL -o /tmp/kafka-connect-couchbase.zip \
                 https://github.com/couchbase/kafka-connect-couchbase/releases/download/$KAFKA_CONNECT_COUCHBASE_VER/kafka-connect-couchbase-$KAFKA_CONNECT_COUCHBASE_VER.zip; \
    unzip /tmp/kafka-connect-couchbase.zip -d /tmp; \
    mv /tmp/kafka-connect-couchbase-$KAFKA_CONNECT_COUCHBASE_VER/etc/kafka-connect-couchbase /etc/; \
    mv /tmp/kafka-connect-couchbase-$KAFKA_CONNECT_COUCHBASE_VER/share/doc/kafka-connect-couchbase /usr/share/doc/; \
    mv /tmp/kafka-connect-couchbase-$KAFKA_CONNECT_COUCHBASE_VER/share/java/kafka-connect-couchbase $KAFKA_CONNECT_PLUGINS_DIR/

# Install DataMountaineer Cassandra Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME cassandra
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DbVisit Oracle Source Kafka Connector
ENV KAFKA_CONNECT_DBVISIT_ORACLE_VER 2.9.00
RUN git clone https://github.com/dbvisitsoftware/replicate-connector-library.git /tmp/replicate-connector-library; \
    git clone https://github.com/dbvisitsoftware/replicate-connector-for-kafka.git /tmp/replicate-connector-for-kafka; \
    cd /tmp/replicate-connector-library; \
    mvn versions:set -DnewVersion=$KAFKA_CONNECT_DBVISIT_ORACLE_VER; \
    mvn -DskipTests install; \
    cd /tmp/replicate-connector-for-kafka; \
    mvn -DskipTests package; \
    unzip /tmp/replicate-connector-for-kafka/target/kafka-connect-dbvisitreplicate-$KAFKA_CONNECT_DBVISIT_ORACLE_VER-package.zip -d /tmp/kafka-connect-dbvisitreplicate; \
    mv /tmp/kafka-connect-dbvisitreplicate/etc/kafka-connect-dbvisitreplicate /etc/; \
    mv /tmp/kafka-connect-dbvisitreplicate/share/doc/kafka-connect-dbvisitreplicate /usr/share/doc/; \
    mv /tmp/kafka-connect-dbvisitreplicate/share/java/kafka-connect-dbvisitreplicate $KAFKA_CONNECT_PLUGINS_DIR

# Install SAP HANA Source & Sink Kafka Connector
RUN git clone https://github.com/santi81/kafka-connect-hana.git /tmp/kafka-connect-hana; \
    cd /tmp/kafka-connect-hana; \
    sed -i -e "s%<scala.binary.version>2.10</scala.binary.version>%<scala.binary.version>2.11</scala.binary.version>%" pom.xml; \
    sed -i -e "s%<scala.version>2.10.4</scala.version>%<scala.version>2.11.11</scala.version>%" pom.xml; \
    mvn -DskipTests package; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-hana; \
    mv /tmp/kafka-connect-hana/target/*.jar $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-hana

######################################################################################
# Community Connectors
######################################################################################

# Jeremy Custenborder connectors (Confluent)

# Install Spool Dir Source Kafka Connector
ENV KAFKA_CONNECT_SPOOLDIR_VER 1.0.16
RUN git clone -b $KAFKA_CONNECT_SPOOLDIR_VER https://github.com/jcustenborder/kafka-connect-spooldir.git /tmp/kafka-connect-spooldir; \
    cd /tmp/kafka-connect-spooldir; \
    mvn -DskipTests package; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-spooldir; \
    mv /tmp/kafka-connect-spooldir/target/kafka-connect-target/etc/kafka-connect-spooldir /etc/; \
    mv /tmp/kafka-connect-spooldir/target/kafka-connect-target/usr/share/doc/kafka-connect-spooldir /usr/share/doc/; \
    mv /tmp/kafka-connect-spooldir/target/kafka-connect-target/usr/share/java/kafka-connect-spooldir $KAFKA_CONNECT_PLUGINS_DIR

# Install Amazon Kinesis Source Kafka Connector
ENV KAFKA_CONNECT_KINESIS_VER 0.1.0.1
RUN curl -fSL -o /tmp/kafka-connect-kinesis.tar.gz \
                 https://github.com/jcustenborder/kafka-connect-kinesis/releases/download/$KAFKA_CONNECT_KINESIS_VER/kafka-connect-kinesis-$KAFKA_CONNECT_KINESIS_VER.tar.gz; \
    mkdir /tmp/kafka-connect-kinesis; \
    tar -xvf /tmp/kafka-connect-kinesis.tar.gz -C /tmp/kafka-connect-kinesis; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-kinesis; \
    mv /tmp/kafka-connect-kinesis/etc/kafka-connect-kinesis /etc/; \
    mv /tmp/kafka-connect-kinesis/usr/share/doc/kafka-connect-kinesis /usr/share/doc/; \
    mv /tmp/kafka-connect-kinesis/usr/share/java/kafka-connect-kinesis $KAFKA_CONNECT_PLUGINS_DIR

# Install RabbitMQ Source Kafka Connector
ENV KAFKA_CONNECT_RABBITMQ 0.0.2.6
RUN git clone -b $KAFKA_CONNECT_RABBITMQ https://github.com/jcustenborder/kafka-connect-rabbitmq.git /tmp/kafka-connect-rabbitmq; \
    cd /tmp/kafka-connect-rabbitmq; \
    mvn -DskipTests package; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-rabbitmq; \
    mv /tmp/kafka-connect-rabbitmq/target/kafka-connect-target/etc/kafka-connect-rabbitmq /etc/; \
    mv /tmp/kafka-connect-rabbitmq/target/kafka-connect-target/usr/share/doc/kafka-connect-rabbitmq /usr/share/doc/; \
    mv /tmp/kafka-connect-rabbitmq/target/kafka-connect-target/usr/share/java/kafka-connect-rabbitmq $KAFKA_CONNECT_PLUGINS_DIR

# Install SalesForce Source Kafka Connector
ENV KAFKA_CONNECT_SALESFORCE_VER 0.3.15
RUN curl -fSL -o /tmp/kafka-connect-salesforce.tar.gz \
                 https://github.com/jcustenborder/kafka-connect-salesforce/releases/download/$KAFKA_CONNECT_SALESFORCE_VER/kafka-connect-salesforce-$KAFKA_CONNECT_SALESFORCE_VER.tar.gz; \
    mkdir /tmp/kafka-connect-salesforce; \
    tar -xvf /tmp/kafka-connect-salesforce.tar.gz -C /tmp/kafka-connect-salesforce; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-salesforce; \
    mv /tmp/kafka-connect-salesforce/etc/kafka-connect-salesforce /etc/; \
    mv /tmp/kafka-connect-salesforce/usr/share/doc/kafka-connect-salesforce /usr/share/doc/; \
    mv /tmp/kafka-connect-salesforce/usr/share/java/kafka-connect-salesforce $KAFKA_CONNECT_PLUGINS_DIR

# Install Solr Sink Kafka Connector
ENV KAFKA_CONNECT_SOLR 0.1.13
RUN git clone -b $KAFKA_CONNECT_SOLR https://github.com/jcustenborder/kafka-connect-solr.git /tmp/kafka-connect-solr; \
    cd /tmp/kafka-connect-solr; \
    mvn -DskipTests package; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-solr; \
    mv /tmp/kafka-connect-solr/target/kafka-connect-target/etc/kafka-connect-solr /etc/; \
    mv /tmp/kafka-connect-solr/target/kafka-connect-target/usr/share/doc/kafka-connect-solr /usr/share/doc/; \
    mv /tmp/kafka-connect-solr/target/kafka-connect-target/usr/share/java/kafka-connect-solr $KAFKA_CONNECT_PLUGINS_DIR
    
# Install Splunk Source & Sink Kafka Connector
ENV KAFKA_CONNECT_SPLUNK_VER 0.2.0.24
RUN curl -fSL -o /tmp/kafka-connect-splunk.tar.gz \
                 https://github.com/jcustenborder/kafka-connect-splunk/releases/download/$KAFKA_CONNECT_SPLUNK_VER/kafka-connect-splunk-$KAFKA_CONNECT_SPLUNK_VER.tar.gz; \
    mkdir /tmp/kafka-connect-splunk; \
    tar -xvf /tmp/kafka-connect-splunk.tar.gz -C /tmp/kafka-connect-splunk; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-splunk; \
    mv /tmp/kafka-connect-splunk/etc/kafka-connect-splunk /etc/; \
    mv /tmp/kafka-connect-splunk/usr/share/doc/kafka-connect-splunk /usr/share/doc/; \
    mv /tmp/kafka-connect-splunk/usr/share/java/kafka-connect-splunk $KAFKA_CONNECT_PLUGINS_DIR

# Install Syslog Source Kafka Connector
ENV KAFKA_CONNECT_SYSLOG 0.2.9
RUN git clone -b $KAFKA_CONNECT_SYSLOG https://github.com/jcustenborder/kafka-connect-syslog.git /tmp/kafka-connect-syslog; \
    cd /tmp/kafka-connect-syslog; \
    mvn -DskipTests package; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-syslog; \
    mv /tmp/kafka-connect-syslog/target/kafka-connect-target/etc/kafka-connect-syslog /etc/; \
    mv /tmp/kafka-connect-syslog/target/kafka-connect-target/usr/share/doc/kafka-connect-syslog /usr/share/doc/; \
    mv /tmp/kafka-connect-syslog/target/kafka-connect-target/usr/share/java/kafka-connect-syslog $KAFKA_CONNECT_PLUGINS_DIR
 
# Install Twitter Source Kafka Connector
ENV KAFKA_CONNECT_TWITTER_VER 0.2.21
RUN git clone -b $KAFKA_CONNECT_TWITTER_VER https://github.com/jcustenborder/kafka-connect-twitter.git /tmp/kafka-connect-twitter; \
    cd /tmp/kafka-connect-twitter; \
    mvn -DskipTests package; \
    mkdir $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-twitter; \
    mv /tmp/kafka-connect-twitter/target/kafka-connect-target/etc/kafka-connect-twitter /etc/; \
    mv /tmp/kafka-connect-twitter/target/kafka-connect-target/usr/share/doc/kafka-connect-twitter /usr/share/doc/; \
    mv /tmp/kafka-connect-twitter/target/kafka-connect-target/usr/share/java/kafka-connect-twitter $KAFKA_CONNECT_PLUGINS_DIR
    
# --------------------------------------------------------------------------------------------------------------------------------------------------

###################################################################
# DataMountaineer Connectors
###################################################################

# Install DataMountaineer Blockchain Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME blockchain
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Azure Document DB Sink Kafka Connector
RUN curl -fSL -o /tmp/kafka-connect-azure-documentdb.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-azure-documentdb-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-azure-documentdb; \
    tar -xzf /tmp/kafka-connect-azure-documentdb.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-azure-documentdb

# Install DataMountaineer Bloomberg Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME bloomberg
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Coap Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME coap
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Druid Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME druid
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer ElasticSearch Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME elastic
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Ftp Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME ftp
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Hazelcast Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME hazelcast
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer HBase Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME hbase
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer InfuxDb Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME influxdb
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer JMS Source Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME jms
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME
      
# Install DataMountaineer Kudu Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME kudu
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Kudu Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME kudu
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer MongoDb Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME mongodb
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer MQTT Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME mqtt
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Redis Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME redis
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Rethink DB Source & Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME rethink
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer VoltDB Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME voltdb
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME

# Install DataMountaineer Yahoo Sink Kafka Connector
ENV DATAMOUNTAINEER_CONNECTOR_NAME yahoo
RUN curl -fSL -o /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz \
                 https://github.com/datamountaineer/stream-reactor/releases/download/v$KAFKA_CONNECT_DATAMOUNTAINEER_VER/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME-$KAFKA_CONNECT_DATAMOUNTAINEER_VER-$CONFLUENT_PLATFORM_VER-all.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME; \
    tar -xzf /tmp/kafka-connect-$DATAMOUNTAINEER_CONNECTOR_NAME.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-datamountaineer-$DATAMOUNTAINEER_CONNECTOR_NAME


# --------------------------------------------------------------------------------------------------------------------------------------


################################################################
# Debezium Source Connectors
################################################################

# Install Debezium MongoDB Source Kafka Connector
ENV DEBEZIUM_CONNECTOR_NAME mongodb
ENV MAVEN_CENTRAL https://repo1.maven.org/maven2
RUN curl -fSL -o /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz \
                 $MAVEN_CENTRAL/io/debezium/debezium-connector-$DEBEZIUM_CONNECTOR_NAME/$KAFKA_CONNECT_DEBEZIUM_VER/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium; \
    tar -xzf /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium --strip 1

# Install Debezium MySql Source Kafka Connector
ENV DEBEZIUM_CONNECTOR_NAME mysql
ENV MAVEN_CENTRAL https://repo1.maven.org/maven2
RUN curl -fSL -o /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz \
                 $MAVEN_CENTRAL/io/debezium/debezium-connector-$DEBEZIUM_CONNECTOR_NAME/$KAFKA_CONNECT_DEBEZIUM_VER/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium; \
    tar -xzf /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium --strip 1

# Install Debezium PostgreSql Source Kafka Connector
ENV DEBEZIUM_CONNECTOR_NAME postgres
ENV MAVEN_CENTRAL https://repo1.maven.org/maven2
RUN curl -fSL -o /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz \
                 $MAVEN_CENTRAL/io/debezium/debezium-connector-$DEBEZIUM_CONNECTOR_NAME/$KAFKA_CONNECT_DEBEZIUM_VER/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium; \
    tar -xzf /tmp/debezium-connector-$DEBEZIUM_CONNECTOR_NAME-$KAFKA_CONNECT_DEBEZIUM_VER-plugin.tar.gz -C $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-debezium --strip 1


# --------------------------------------------------------------------------------------------------------------------------------------------------

################################################################
# Other Connectors
################################################################

# Install Ignite Source & Sink Kafka Connector
ENV KAFKA_CONNECT_IGNITE_VER 2.0.0
RUN mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-ignite; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-ignite/kafka-connect-ignite.jar \
                 http://central.maven.org/maven2/org/apache/ignite/ignite-kafka/$KAFKA_CONNECT_IGNITE_VER/ignite-kafka-$KAFKA_CONNECT_IGNITE_VER.jar; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-ignite/ignite-core.jar \
                 http://central.maven.org/maven2/org/apache/ignite/ignite-core/$KAFKA_CONNECT_IGNITE_VER/ignite-core-$KAFKA_CONNECT_IGNITE_VER.jar; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-ignite/ignite-spring.jar \
                 http://central.maven.org/maven2/org/apache/ignite/ignite-spring/$KAFKA_CONNECT_IGNITE_VER/ignite-spring-$KAFKA_CONNECT_IGNITE_VER.jar

# Install Amazon Dynamo DB Source & Sink Kafka Connector
ENV KAFKA_CONNECT_DYNAMODB_VER 0.2.0
RUN curl -fSL -o /tmp/kafka-connect-dynamodb.zip \
                 https://github.com/shikhar/kafka-connect-dynamodb/releases/download/$KAFKA_CONNECT_DYNAMODB_VER/kafka-connect-dynamodb-$KAFKA_CONNECT_DYNAMODB_VER.zip; \
    unzip /tmp/kafka-connect-dynamodb.zip -d $KAFKA_CONNECT_PLUGINS_DIR

# Install File System Source Kafka Connector
ENV KAFKA_CONNECT_FS_VER 0.1.2
RUN git clone -b v$KAFKA_CONNECT_FS_VER https://github.com/mmolimar/kafka-connect-fs.git /tmp/kafka-connect-fs; \
    cd /tmp/kafka-connect-fs; \
    mvn -DskipTests package; \
    mv /tmp/kafka-connect-fs/target/kafka-connect-fs-$KAFKA_CONNECT_FS_VER-package/etc/kafka-connect-fs /etc/; \
    mv /tmp/kafka-connect-fs/target/kafka-connect-fs-$KAFKA_CONNECT_FS_VER-package/share/doc/kafka-connect-fs /usr/share/doc/; \
    mv /tmp/kafka-connect-fs/target/kafka-connect-fs-$KAFKA_CONNECT_FS_VER-package/share/java/kafka-connect-fs $KAFKA_CONNECT_PLUGINS_DIR
 
# Install PubSub Source and Sink Kafka Connector
ENV KAFKA_CONNECT_PUBSUB 1.0.0
RUN git clone https://github.com/GoogleCloudPlatform/pubsub.git /tmp/kafka-connect-pubsub; \
    cd /tmp/kafka-connect-pubsub/kafka-connector; \
    mvn -DskipTests package; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-pubsub;\
    cp /tmp/kafka-connect-pubsub/kafka-connector/target/*.jar $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-pubsub

# Install MongoDB Source & Sink Kafka Connector
ENV KAFKA_CONNECT_MONGODB 1.3.5
RUN git clone -b $KAFKA_CONNECT_MONGODB https://github.com/teambition/kafka-connect-mongo.git /tmp/kafka-connect-mongo; \
    cd /tmp/kafka-connect-mongo; \
    ./gradlew clean distTar; \
    mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-mongodb; \
    mkdir -p /etc/kafka-connect-mongodb; \
    cp /tmp/kafka-connect-mongo/build/libs/*.jar $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-mongodb; \
    cp /tmp/kafka-connect-mongo/etc/connect-mongo* /etc/kafka-connect-mongodb/

# Install Radar MongoDB Sink Kafka Connector
ENV KAFKA_CONNECT_MONGODB_VER 0.1
RUN mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-radar-mongodb; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-radar-mongodb/kafka-connect-mongodb-sink.jar \
                 https://github.com/RADAR-CNS/MongoDb-Sink-Connector/releases/download/$KAFKA_CONNECT_MONGODB_VER/kafka-connect-mongodb-sink-$KAFKA_CONNECT_MONGODB_VER.jar
                 
# Install PubNub Sink Kafka Connector
ENV KAFKA_CONNECT_PUBNUB_VER 0.1.0
RUN mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-pubnub; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-pubnub/kafka-connect-pubnub.jar \
                 https://github.com/Segence/kafka-connect-pubnub/releases/download/$KAFKA_CONNECT_PUBNUB_VER/kafka-connect-pubnub-$KAFKA_CONNECT_PUBNUB_VER.jar

# Install Redis Source Kafka Connector
RUN git clone https://github.com/Aegeaner/kafka-connector-redis.git /tmp/kafka-connect-redis; \
    cd /tmp/kafka-connect-redis; \
    sed -i -e "s%<kafka.version>0.10.2.0-SNAPSHOT</kafka.version>%<kafka.version>0.10.2.1</kafka.version>%" pom.xml; \
    mvn -DskipTests package

# Install Nats Source & Sink Kafka Connector
ENV KAFKA_CONNECT_NATS_VER 0.3.1
RUN mkdir -p $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-nats; \
    curl -fSL -o $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-nats/kafka-connect-nats.jar \
                 https://github.com/oystparis/kafka-connect-nats/releases/download/$KAFKA_CONNECT_NATS_VER/kafka-connect-nats-$KAFKA_CONNECT_NATS_VER-jar-with-dependencies.jar

# --------------------------------------------------------------------------------------------------------------------------------------------------

################################################################
# Parrot Connectors
################################################################

WORKDIR /tmp/parrot

ADD pom.xml ./
ADD core/pom.xml ./core/
ADD kafka-connect-kudu/pom.xml ./kafka-connect-kudu/
ADD smt/pom.xml ./smt/
ADD assemblies ./assemblies
ADD checkstyle ./checkstyle
RUN cd /tmp/parrot; \
    mvn compile
    
ADD core ./core
ADD kafka-connect-kudu ./kafka-connect-kudu
ADD smt ./smt

RUN cd /tmp/parrot; \
    mvn -DskipTests package; \
    mv /tmp/parrot/kafka-connect-kudu/target/kafka-connect-target/etc/kafka-connect-parrot-kudu /etc/; \
    mv /tmp/parrot/kafka-connect-kudu/target/kafka-connect-target/usr/share/java/kafka-connect-parrot-kudu $KAFKA_CONNECT_PLUGINS_DIR/; \
    rm -rf /tmp/parrot

# Clean install artifacts of all previous connectors
#RUN rm -rf /tmp/kafka-connect-*; \
#    rm -rf /tmp/replicate-connector*

COPY docker/etc/ /etc/
COPY docker/*.sh /

RUN chmod +x /*.sh; \
    rm -rf /root/.m2

WORKDIR /

ENTRYPOINT ["supervisord", "-c", "/etc/supervisord.conf", "-n"]