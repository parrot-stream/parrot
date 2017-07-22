#!/bin/bash

supervisorctl start schema-registry

./wait-for-it.sh localhost:$SCHEMA_REGISTRY_PORT -t 120
rc=$?
if [ $rc -ne 0 ]; then
    echo -e "\n---------------------------------------"
    echo -e "  Schema Registry not ready! Exiting..."
    echo -e "---------------------------------------"
    exit $rc
fi

supervisorctl start parrot

echo -e "\n\n------------------------------------------------------------"
echo -e "You can now access to the Schema Registry REST:\n"
echo -e "\tSchema Registry REST:   http://localhost:$SCHEMA_REGISTRY_PORT"
echo -e "\nMantainer:   Matteo Capitanio <matteo.capitanio@gmail.com>"
echo -e "--------------------------------------------------------------\n\n"