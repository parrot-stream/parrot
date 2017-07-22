#!/bin/bash

: ${LISTENERS:=http://0.0.0.0:8081}
: ${KAFKASTORE_CONNECTION_URL:=zookeeper:2181}

sed -i -r "s@(^|^#)(kafkastore.connection.url)=(.*)@\2=${KAFKASTORE_CONNECTION_URL}@g" /etc/schema-registry/schema-registry.properties

echo "access.control.allow.methods=GET,POST,PUT,OPTIONS" >> /etc/schema-registry/schema-registry.properties
echo "access.control.allow.origin=*" >> /etc/schema-registry/schema-registry.properties

echo "Schema Registry started with the following environment variables:
    LISTENERS=$LISTENERS
    KAFKASTORE_CONNECTION_URL=$KAFKASTORE_CONNECTION_URL"
    
schema-registry-start /etc/schema-registry/schema-registry.properties