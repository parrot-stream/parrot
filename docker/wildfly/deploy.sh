#!/bin/bash

pwd=`pwd`

echo "Deploying..."
$WILDFLY_HOME/bin/jboss-cli.sh -u=parrot -p=parrot --file="$pwd"/docker/wildfly/deploy-standalone.cli

exit 0
