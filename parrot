#!/bin/bash

export ID=90
export DOCKER_IMAGE=mcapitanio/parrot

function usage {
    echo -e "#################################################################################################################################################"
    echo -e "  Usage:"
    echo -e "             ./parrot-cli COMMAND [OPTIONS]"
    echo -e ""
    echo -e "  Commands:"
    echo -e "     start             	  Starts Parrot"
    echo -e "     start-kafka       	  Starts Apache Kafka"
    echo -e "     start-debezium    	  Starts Debezium"
    echo -e "     start-kafka-connect-ui  Starts Kafka Connect UI"
    echo -e "     start-hadoop      	  Starts Apache Hadoop"
    echo -e "     start-hbase       	  Starts Apache HBase"
    echo -e "     start-hive        	  Starts Apache Hive"
    echo -e "     start-impala      	  Starts Apache Impala (HDFS)"
    echo -e "     start-impala-kudu		  Starts Apache Impala (Kudu)"
    echo -e "     start-zookeeper    	  Starts Apache ZooKeeper"
    echo -e "     start-postgres    	  Starts PostgreSql"
    echo -e "     start-hue         	  Starts Hue"
    echo -e "     start-all         	  Starts All Services"
    echo -e "     restart           	  Restarts Parrot"
    echo -e "     stop              	  Stops Parrot"
    echo -e "     stop-kafka        	  Stops Apache Kafka"
    echo -e "     stop-debezium     	  Stops Debezium"
    echo -e "     stop-kafka-connect-ui   Stops Kafka Connect UI"
    echo -e "     stop-hadoop       	  Stops Hadoop"
    echo -e "     stop-hbase        	  Stops Apache HBase"
    echo -e "     stop-hive         	  Stops Apache Hive"
    echo -e "     stop-impala       	  Stops Apache Impala (HDFS)"
    echo -e "     stop-impala-kudu		  Stops Apache Impala (Kudu)"
    echo -e "     stop-zookeeper    	  Stops Apache ZooKeeper"
    echo -e "     stop-postgres     	  Stops PostgreSql"
    echo -e "     stop-hue          	  Stops Hue"
    echo -e "     stop-all          	  Stops All Services"
    echo -e "     restart-kafka        	  Restarts Apache Kafka"
    echo -e "     restart-debezium     	  Restarts Debezium"
    echo -e "     restart-hadoop       	  Restarts Hadoop"
    echo -e "     restart-hbase        	  Restarts Apache HBase"
    echo -e "     restart-hive         	  Restarts Apache Hive"
    echo -e "     restart-impala       	  Restarts Apache Impala (HDFS)"
    echo -e "     restart-impala-kudu	  Restarts Apache Impala (Kudu)"
    echo -e "     restart-zookeeper    	  Restarts Apache ZooKeeper"
    echo -e "     restart-postgres     	  Restarts PostgreSql"
    echo -e "     restart-hue          	  Restarts Hue"
    echo -e "     deploy            	  Deploy Parrot"
    echo -e "     test              	  Runs Parrot Integration Tests"
    echo -e "     build             	  Builds Parrot"
    echo -e ""
    echo -e "  Options:"
    echo -e "     -k, --kudu     	Starts Impala with Kudu"
    echo -e "     -d, --detach     	Runs the container in background"
    echo -e "         --debug		Starts Parrot in Debug Mode"
    echo -e "     -m, --mode        	One of:"
    echo -e "                          LOCAL_UNIT"
    echo -e "                                Default: starts Parrot with the configuration for Local Unit Test with Properties.properties.local-ut file."
    echo -e "                          PIPELINE_UNIT"
    echo -e "                                Starts Parrot with the configuration for Trevis Pipeline Unit Test with Properties.properties.pipeline-ut file."
    echo -e ""    
    echo -e " Exposed Ports:"
    echo -e ""
    echo -e "                	8090  	->  JBoss Http"
    echo -e "                     	9990  	->  JBoss Management"
    echo -e "                      	8790  	->  JBoss Debug"
    echo -e "                       	5432  	->  PostgreSQL"
    echo -e "                       	306  	->  MySQL"
    echo -e "                       	1521  	->  Oracle Database"
    echo -e "                       	5500  	->  Oracle Enterprise Manager"
    echo -e "                       	27017 	->  MongoDB"
    echo -e "#################################################################################################################################################"
}

function print_env {
    echo -e "#################################################################################################################################################"
	echo -e "  SERVICE: $SERVICE"
	echo -e "  ID:      $ID"
	echo -e "-------------------------------------------------------------------------------------------------------------------------------------------------"
    echo -e "#################################################################################################################################################"
    echo -e "  Executing with:                                                                                                                                "
    echo -e "           Command             -> $COMMAND"
    echo -e "           Debug               -> $DEBUG"
    echo -e "           Mode                -> $MODE"
    echo -e "           Options             -> `echo $OPTIONS | sed -e 's/^[ \t]*//'`" 
    echo -e "#################################################################################################################################################"
}

function start {
    echo "Starting service '$1'..."
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$1.yml up $2
}

function stop {
    echo "Stopping service '$1'..."
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$1.yml down -v
}

function restart {
    echo "Restarting service '$1'..."
    stop $1
    start $1
}

function start_all {
    export DETACH="true"
	docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 2> /dev/null
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE build
	start zookeeper -d
	start hadoop -d
    start kafka -d
    start postgres -d
    start debezium -d
    start kafka-connect-ui -d
    start kafka-schema-registry-ui -d
    start kafka-topics-ui -d
    start hive -d
    if [[ ($USE_KUDU = "true") ]]; then
      start impala-kudu -d
    else
      start impala -d
    fi
    start hbase -d
    start hue -d
    start parrot
}

function stop_all {
    stop hue
	stop impala-kudu
	stop impala
	stop hadoop
    stop hive
    stop hbase
    stop hadoop
    stop debezium
    stop kafka-connect-ui
    stop kafka-schema-registry-ui
    stop kafka-topics-ui
    stop postgres
    stop kafka
	stop zookeeper
	stop parrot
}

export DEBUG="false"
export STOP="false"
export MODE="LOCAL_UNIT"
export LOCAL_UNIT="false"
export LOCAL_INTEGRATION="false"
export PIPELINE_UNIT="false"
export PIPELINE_INTEGRATION="false"
export PRODUCTION="false"
export HOSTNAME=localhost
export DOCKER_COMPOSE_FILE="parrot.yml"
export DOCKER_COMPOSE_OPTIONS="-p parrot"
export USE_KUDU="false"
export OPTIONS=""
export DETACH=""
export SERVICE="parrot"

for i in "$@"
do
case $i in
	start*)
	COMMAND=$i
	shift
    ;;
	stop*)
	COMMAND=$i
	shift
    ;;
    restart*)
	COMMAND=$i
	shift
    ;;
    test)
    COMMAND="test"
    shift
    ;;
    build)
    COMMAND="build"
    shift
    ;;
    deploy)
    COMMAND="deploy"
    shift
    ;;
    -m*|--mode*)
    set -- "$i" 
    IFS="="; declare -a Array=($*)
    MODE=${Array[1]}
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
    -d*|--detach*)
    export OPTIONS="$OPTIONS -d"
    export DETACH="-d"
    shift
    ;;
    -stop*)
    STOP="true"
    shift
    ;;
    *)
      if [ "$1" = "--help" ]; then
        usage
        print_env
      else
        usage
      fi
      exit 0
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
  print_env
  exit 0
fi

if [[ ($MODE = "LOCAL_UNIT") ]]; then
  export LOCAL_UNIT="true"
fi

if [[ ($MODE = "PIPELINE_UNIT") ]]; then
  export PIPELINE_UNIT="true"
  HOSTNAME=docker
fi

if [[ ($MODE = "PRODUCTION") ]]; then
  export PRODUCTION="true"
fi

if [[ $COMMAND =~ ^stop.* ]] ; then
  if [[ ($COMMAND = "stop") ]]; then
    stop parrot
  elif [[ ($COMMAND = "stop-all") ]]; then
    stop_all
  else
    IFS='-' read -a splitted <<< "$COMMAND"
    stop ${splitted[1]}
  fi
fi

if [[ $COMMAND =~ ^start.* ]]; then
  if [[ ($COMMAND = "restart") ]]; then
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/parrot.yml build
    start parrot
  elif [[ ($COMMAND = "start-all") ]]; then
    start_all
  else
    IFS='-' read -a splitted <<< "$COMMAND"
    start ${splitted[1]}
  fi
fi

if [[ $COMMAND =~ ^restart.* ]]; then
  if [[ ($COMMAND = "restart") ]]; then
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/parrot.yml build
    restart parrot
  elif [[ ($COMMAND = "restart-all") ]]; then
    stop_all
    start_all
  else
    IFS='-' read -a splitted <<< "$COMMAND"
    restart ${splitted[1]}
  fi
fi

case $COMMAND in
  stop*)
  exit 0
  ;;
  start-*)
  exit 0  
  ;;
esac

mvn clean package -DskipTests

if [ $? -ne 0 ]; then
  exit 1
fi

if [[ ($COMMAND = "test") ]]; then
  mvn clean verify -PskipDeploy -DPIPELINE_UNIT=$PIPELINE_UNIT -DPIPELINE_INTEGRATION=$PIPELINE_INTEGRATION -DLOCAL_UNIT=$LOCAL_UNIT -DLOCAL_INTEGRATION=$LOCAL_INTEGRATION -Dwildfly.hostname=$HOSTNAME -Dwildfly.port=99$ID -Dpostgresql.hostname=$HOSTNAME -Dpostgresql.port=54$ID -U
  if [ $? -ne 0 ]; then
    exit 1
  fi
fi

if [[ ($COMMAND = "deploy") ]]; then
  mvn clean install -DskipTests -DPIPELINE_UNIT=$PIPELINE_UNIT -DPIPELINE_INTEGRATION=$PIPELINE_INTEGRATION -DLOCAL_UNIT=$LOCAL_UNIT -DLOCAL_INTEGRATION=$LOCAL_INTEGRATION -Dwildfly.hostname=$HOSTNAME -Dwildfly.port=99$ID -Dpostgresql.hostname=$HOSTNAME -Dpostgresql.port=54$ID -U
  if [ $? -ne 0 ]; then
    exit 1
  fi
fi

if [[ ($COMMAND = "build") ]]; then
  docker-compose -f docker/docker-compose.yml build
  if [[ ($DOCKER_TAG -ne "local") ]]; then
    docker-compose -f ./docker/parrot.yml push
  fi
  exit 0
fi