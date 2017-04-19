#!/bin/bash

wait-for-it.sh $DB_HOSTNAME:5432 -t 120
rc=$?
if [ $rc -ne 0 ]; then
    echo -e "\n--------------------------------------------"
    echo -e "      Postgres not ready! Exiting..."
    echo -e "--------------------------------------------"
    exit $rc
fi

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
echo -e "#                 STARTING INIT POSTGRES...                      #"
echo -e "#                                                                #"
echo -e "##################################################################\n"

supervisorctl start init-postgres

if [[ $DEBUG = "true" ]]; then
	supervisorctl start jboss-debug
else
	supervisorctl start jboss
fi


wait-for-it.sh localhost:9990 -t 120

rc=$?
if [ $rc -ne 0 ]; then
    echo -e "\n--------------------------------------------"
    echo -e "      JBoss not ready! Exiting..."
    echo -e "--------------------------------------------"
    exit $rc
fi

sleep 15

supervisorctl start init-jboss

supervisorctl start init-debezium
