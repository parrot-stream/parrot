#!/bin/bash

: ${BOOTSTRAP_SERVERS:=kafka:9092}
: ${GROUP_ID:=1}
: ${KEY_CONVERTER_SCHEMA_REGISTRY_URL:=http://localhost:8081}
: ${VALUE_CONVERTER_SCHEMA_REGISTRY_URL:=http://localhost:8081}

sed -i -r "s@(^|^#)(bootstrap.servers)=(.*)@\2=${BOOTSTRAP_SERVERS}@g" /etc/schema-registry/connect-avro-distributed.properties
sed -i -r "s@(^|^#)(group.id)=(.*)@\2=${GROUP_ID}@g" /etc/schema-registry/connect-avro-distributed.properties
sed -i -r "s@(^|^#)(key.converter.schema.registry.url)=(.*)@\2=${KEY_CONVERTER_SCHEMA_REGISTRY_URL}@g" /etc/schema-registry/connect-avro-distributed.properties
sed -i -r "s@(^|^#)(value.converter.schema.registry.url)=(.*)@\2=${VALUE_CONVERTER_SCHEMA_REGISTRY_URL}@g" /etc/schema-registry/connect-avro-distributed.properties

echo "Parrot started with the following environment variables:
    BOOTSTRAP_SERVERS=$BOOTSTRAP_SERVERS
    GROUP_ID=$GROUP_ID
    KEY_CONVERTER_SCHEMA_REGISTRY_URL=$KEY_CONVERTER_SCHEMA_REGISTRY_URL
    VALUE_CONVERTER_SCHEMA_REGISTRY_URL=$VALUE_CONVERTER_SCHEMA_REGISTRY_URL"

KAFKA_CONNECT_PLUGINS_DIR=/usr/share/java

# Add Kafka Connect plugins to CLASSPATH
for pluginDir in $KAFKA_CONNECT_PLUGINS_DIR/kafka-connect-*/ ; do
    echo "Using plugin at $pluginDir"
    CLASSPATH=$CLASSPATH:$pluginDir*
done

echo "Current classpath: $CLASSPATH"

connect-distributed /etc/schema-registry/connect-avro-distributed.properties