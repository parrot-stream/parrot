#!/bin/bash

#rc=$?
#if [ $rc -ne 0 ]; then
#    echo -e "\n--------------------------------------------"
#    echo -e "      Oracle XE not ready! Exiting..."
#    echo -e "--------------------------------------------"
#    exit $rc
#fi

wait-for-it.sh $KAFKA_BROKER_HOSTNAME:9092 -t 120
rc=$?
if [ $rc -ne 0 ]; then
    echo -e "\n--------------------------------------------"
    echo -e "      Apache Kafka not ready! Exiting..."
    echo -e "--------------------------------------------"
    exit $rc
fi

wait-for-it.sh $DEBEZIUM_HOSTNAME:8083 -t 120
rc=$?
if [ $rc -ne 0 ]; then
    echo -e "\n--------------------------------------------"
    echo -e "      Debezium not ready! Exiting..."
    echo -e "--------------------------------------------"
    exit $rc
fi

echo -e "\n##################################################################"
echo -e "#                                                                #"
echo -e "#                 STARTING INIT SOURCES ...                      #"
echo -e "#                                                                #"
echo -e "##################################################################\n"

supervisorctl start init-sources

#supervisorctl start init-debezium
