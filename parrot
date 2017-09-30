#!/bin/bash

export DOCKER_IMAGE=parrotstream/parrot

function usage {
  echo -e "#################################################################################################################################################"
  echo -e "  Usage:"
  echo -e "             ./parrot COMMAND [OPTIONS]"
  echo -e ""
  echo -e "  Commands:"
  echo -e "     start                           Starts Parrot (without dependent services)"
  echo -e "     stop                            Stops Parrot (without dependent services)"
  echo -e "     restart                         Restarts Parrot (without dependent services)"
  echo -e "     start-all                       Starts Parrot (with all dependent services)"
  echo -e "     stop-all                        Stops Parrot (with all dependent services)"
  echo -e "     restart-all                     Restarts Parrot (with all dependent services)"
  echo -e "     start -s|--services=SERVICES    Starts a comma-separated list of services"
  echo -e ""
  echo -e "            Example:  start -s=zookeeper,kafka,topics-ui"
  echo -e ""
  echo -e "     stop -s|--services=SERVICES     Starts a comma-separated list of services"
  echo -e ""
  echo -e "            Example:  stop -s=zookeeper,kafka,topics-ui"
  echo -e ""
  echo -e "     restart -s|--services=SERVICES  Restarts a comma-separated list of services"
  echo -e ""
  echo -e "            Example:  restart -s=zookeeper,kafka,topics-ui"
  echo -e ""
  echo -e "     logs -s|--services=SERVICE      View the logs of a service"
  echo -e ""
  echo -e "     test                            Runs Parrot Integration Tests"
  echo -e "     build                           Builds Parrot"
  echo -e "     init-postgres                   Init Postgres with Unit Test data structures"
  echo -e ""
  echo -e "  Options:"
  echo -e "     -s, --services  Comma-separated list of services to start|stop|restart"
  echo -e "     -k, --kudu     	Starts Impala with Kudu when use with 'start-all' or 'restart-all'"
  echo -e ""
  echo -e "  Available services:"
  echo -e "      zookeeper"
  echo -e "      kafka"
  echo -e "      kafka-topics-ui"
  echo -e "      kafka-metrics-ui"
  echo -e "      kafka-connect-ui"
  echo -e "      schema-registry-ui"
  echo -e "      hadoop"
  echo -e "      hbase"
  echo -e "      hive"
  echo -e "      hue"
  echo -e "      impala"
  echo -e "      impala-kudu"
  echo -e "      postgres"
  echo -e "      mysql"
  echo -e "      mongodb"
  echo -e ""    
  echo -e " Exposed Ports:"
  echo -e ""
  echo -e "      5432  	->  PostgreSQL"
  echo -e "      3306  	->  MySQL"
  echo -e "      27017 	->  MongoDB"
  echo -e "      1521  	->  Oracle Database"
  echo -e "      5500  	->  Oracle Enterprise Manager"
  echo -e "#################################################################################################################################################"
}

function print_env {
  echo -e "#################################################################################################################################################"
  echo -e "  SERVICE: parrot"
  echo -e "-------------------------------------------------------------------------------------------------------------------------------------------------"
  echo -e "  Executing with:                                                                                                                                "
  echo -e "           Command             -> $COMMAND"
  echo -e "           Services            -> $SERVICES"
  echo -e "           Options             -> `echo $OPTIONS | sed -e 's/^[ \t]*//'`" 
  echo -e "#################################################################################################################################################"
}

function start {
  echo "Starting service '$1'..."
  docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$1.yml up -d
  if [ $? -ne 0 ]; then
    exit 1
  fi
}

function logs {
  echo "Viewing logging of service '$1'..."
  docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$1.yml logs -f
}

function stop {
  if [ ! -f docker/$1.yml ]; then
    echo -e "\nService $1 does not exists!\n"
    usage
    exit 0
  else
    echo "Stopping service '$1'..."
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$1.yml down -v
  fi
}

function restart {
  if [ ! -f docker/$1.yml ]; then
    echo -e "\nService $1 does not exists!\n"
    usage
    exit 0
  else
    echo "Restarting service '$1'..."
    stop $1
  fi
  start $1
}

function start_all {
  docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 2> /dev/null
  docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/parrot.yml build
  start zookeeper
  start hadoop
  start kafka
  start postgres
  start hive
  if [[ ($USE_KUDU = "true") ]]; then
    start kudu
    start impala-kudu
  else
    start impala
  fi
  start hbase
  start hue
  start parrot
  start kafka-connect-ui
  start schema-registry-ui
  start kafka-topics-ui
  start kafka-metrics-ui
}

function stop_all {
  stop hue
  stop kudu
  stop impala-kudu
  stop impala
  stop hadoop
  stop hive
  stop hbase
  stop hadoop
  stop kafka-connect-ui
  stop schema-registry-ui
  stop kafka-topics-ui
  stop kafka-metrics-ui
  stop postgres
  stop kafka
  stop zookeeper
  stop parrot
}

function build {
  mvn -N versions:update-child-modules
  mvn clean package
  if [ $? -ne 0 ]; then
    exit 1
  fi
  docker-compose -f docker/parrot.yml build
  if [ $? -ne 0 ]; then
    exit 1
  fi
  if [[ ($DOCKER_TAG -ne "local") ]]; then
    docker-compose -f ./docker/parrot.yml push
  fi
}

export KAFKA_TOPICS_UI_PORT=9000
export SCHEMA_REGISTRY_UI_PORT=9001
export KAFKA_CONNECT_UI_PORT=9002
export KAFKA_METRICS_UI_PORT=9003
export SCHEMA_REGISTRY_PORT=8081
export PARROT_PORT=8083

export STOP="false"
export HOSTNAME=localhost
export DOCKER_COMPOSE_OPTIONS="-p parrot"
export USE_KUDU="false"
export OPTIONS=""
export SERVICES=""

for i in "$@"
do
case $i in
	start*|stop*|restart*|test|build|logs)
    COMMAND=$i
    shift
    ;;
    -s*|--services*)
    set -- "$i" 
    IFS="="; declare -a Array=($*)
    SERVICES=${Array[1]}
    shift
    ;;
    --debug*)
    export OPTIONS="$OPTIONS --debug"
    export DEBUG="true"
    shift
    ;;
    -k*|--kudu*)
    export OPTIONS="$OPTIONS -k"
    export USE_KUDU="true"
    shift
    ;;
    -h|--help)
    usage
    exit 0
    shift
    ;;
esac
done

print_env

docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 2> /dev/null

if [[ (-z "$DOCKER_TAG") ]]; then
  export DOCKER_TAG=local
fi

if [[ (-z "$COMMAND") ]]; then
  usage
  exit 0
fi

if [[ $COMMAND =~ ^stop.* ]] ; then
  if [[ ($COMMAND = "stop") && (-z "$SERVICES") ]]; then
    stop parrot
  elif [[ ($COMMAND = "stop-all") ]]; then
    stop_all
  else
    IFS=',' read -a splitted <<< "$SERVICES"
    for s in "${splitted[@]}"
    do
      stop ${s}
    done
  fi
fi

if [[ $COMMAND =~ ^start.* ]]; then
  if [[ ($COMMAND = "start") && (-z "$SERVICES") ]]; then
    build
    start parrot
    exit 0
  elif [[ ($COMMAND = "start-all") ]]; then
    start_all
  else
    IFS=',' read -a splitted <<< "$SERVICES"
    for s in "${splitted[@]}"
    do
      if [ ! -f docker/${s}.yml ]; then
        echo -e "\nService ${s} does not exists!\n"
        usage
        exit 0
      fi
    done
    for s in "${splitted[@]}"
    do
      start ${s}
    done
  fi
fi

if [[ $COMMAND =~ ^restart.* ]]; then
  if [[ ($COMMAND = "restart") && (-z "$SERVICES") ]]; then
    build
    restart parrot
  elif [[ ($COMMAND = "restart-all") ]]; then
    stop_all
    start_all
  else
    IFS=',' read -a splitted <<< "$SERVICES"
    for s in "${splitted[@]}"
    do
      restart ${s}
    done
  fi
fi

case $COMMAND in
  stop*)
  exit 0
  ;;
  start*)
  exit 0  
  ;;
esac

if [ $? -ne 0 ]; then
  exit 1
fi

if [[ ($COMMAND = "logs") ]]; then
  IFS=',' read -a splitted <<< "$SERVICES"
  if [ ${#splitted[@]} -gt 1 ]; then
    echo -e "\nYou must specify only one service to view logs\n!"
    exit 1
  elif [ ${#splitted[@]} -eq 0 ]; then
    logs parrot
  else
    for s in "${splitted[@]}"
    do
      logs ${s}
    done
  fi
fi

if [[ ($COMMAND = "test") ]]; then
  mvn clean verify -PskipDeploy -DPIPELINE_UNIT=$PIPELINE_UNIT -DPIPELINE_INTEGRATION=$PIPELINE_INTEGRATION -DLOCAL_UNIT=$LOCAL_UNIT -DLOCAL_INTEGRATION=$LOCAL_INTEGRATION -Dwildfly.hostname=$HOSTNAME -Dwildfly.port=99$ID -Dpostgresql.hostname=$HOSTNAME -Dpostgresql.port=54$ID -U
  if [ $? -ne 0 ]; then
    exit 1
  fi
fi

if [[ ($COMMAND = "build") ]]; then
  build
  exit 0
fi