#!/bin/bash

pwd=`pwd`
if [[ $SERVICE ]]; then
  echo "Skipping deployment..."
else
  "$pwd"/docker/wildfly/deploy.sh
fi