#!/bin/bash

curl -i -X DELETE $DEBEZIUM_HOSTNAME:8083/connectors/galeve
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" $DEBEZIUM_HOSTNAME:8083/connectors/ --data 'connector1.json'