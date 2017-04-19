#!/bin/bash

curl -i -X DELETE $DEBEZIUM_HOSTNAME:8083/connectors/galeve
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" $DEBEZIUM_HOSTNAME:8083/connectors/ -d '{ "name": "parrot", "config": { "connector.class": "io.debezium.connector.postgresql.PostgresConnector", "database.hostname": "postgres", "database.port": "5432", "database.user": "postgres", "database.password": "postgres", "database.dbname": "parrot", "database.server.name": "parrot_name", "schema.whitelist": "parrot", "table.blacklist": "parrot.camel_messageprocessed"} }'