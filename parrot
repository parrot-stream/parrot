#!/bin/bash

export ID=90
export DOCKER_IMAGE=mcapitanio/parrot

function usage {
 echo -e "#################################################################################################################################################"
    echo -e "  Usage:"
    echo -e "             ./parrot-cli COMMAND [OPTIONS]"
    echo -e ""
    echo -e "  Commands:"
    echo -e "     start             	Starts Parrot"
    echo -e "     start-kafka       	Starts Apache Kafka"
    echo -e "     start-debezium    	Starts Debezium"
    echo -e "     start-hadoop      	Starts Apache Hadoop"
    echo -e "     start-hbase       	Starts Apache HBase"
    echo -e "     start-hive        	Starts Apache Hive"
    echo -e "     start-impala      	Starts Apache Impala (HDFS)"
    echo -e "     start-impala-kudu		Starts Apache Impala (Kudu)"
    echo -e "     start-zookeeper   	Starts Apache ZooKeeper"
    echo -e "     start-postgres    	Starts PostgreSql"
    echo -e "     start-hue         	Starts Hue"
    echo -e "     start-all         	Starts All Services"
    echo -e "     restart           	Restarts Parrot"
    echo -e "     stop              	Stops Parrot"
    echo -e "     stop-kafka        	Stops Apache Kafka"
    echo -e "     stop-debezium     	Stops Debezium"
    echo -e "     stop-hadoop       	Stops Hadoop"
    echo -e "     stop-hbase        	Stops Apache HBase"
    echo -e "     stop-hive         	Stops Apache Hive"
    echo -e "     stop-impala       	Stops Apache Impala (HDFS)"
    echo -e "     stop-impala-kudu		Stops Apache Impala (Kudu)"
    echo -e "     stop-zookeeper    	Stops Apache ZooKeeper"
    echo -e "     stop-postgres     	Stops PostgreSql"
    echo -e "     stop-hue          	Stops Hue"
    echo -e "     stop-all          	Stops All Services"
    echo -e "     deploy            	Deploy Parrot"
    echo -e "     test              	Runs Parrot Integration Tests"
    echo -e "     build             	Builds Parrot"
    echo -e ""
    echo -e "  Options:"
    echo -e "     -d, --detach     	Executes command in background"
    echo -e "         --debug		Starts Parrot in Debug Mode"
    echo -e "     -k, --kudu      	Starts Impala with Kudu Support"
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
    echo -e "			Command           	-> $COMMAND"
    echo -e "			Debug             	-> $DEBUG"
    echo -e "			Mode             	-> $MODE"
    echo -e "			Options			-> $OPTIONS"
    echo -e "#################################################################################################################################################"
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
export DOCKER_COMPOSE_FILE="docker-compose.yml"
export DOCKER_COMPOSE_OPTIONS="-p parrot"
export USE_KUDU="false"
export OPTIONS=""

export SERVICE="parrot"

for i in "$@"
do
case $i in
	start)
	COMMAND="start"
	shift
    ;;
	stop)
	COMMAND="stop"
	shift
    ;;
    stop-kafka)
	COMMAND="stop-kafka"
	shift
    ;;
    start-kafka)
	COMMAND="start-kafka"
	shift
    ;;
    stop-debezium)
    COMMAND="stop-debezium"
	shift
    ;;
    start-debezium)
	COMMAND="start-debezium"
	shift
    ;;
    stop-hbase)
    COMMAND="stop-hbase"
	shift
    ;;
    start-hbase)
	COMMAND="start-hbase"
	shift
    ;;
    stop-hive)
    COMMAND="stop-hive"
	shift
    ;;
    start-hive)
	COMMAND="start-hive"
	shift
    ;;
    stop-hadoop)
    COMMAND="stop-hadoop"
	shift
    ;;
    start-hadoop)
	COMMAND="start-hadoop"
	shift
    ;;
    stop-impala)
    COMMAND="stop-impala"
	shift
    ;;
    start-impala)
	COMMAND="start-impala"
	shift
    ;;
    stop-impala-kudu)
    COMMAND="stop-impala-kudu"
	shift
    ;;
    start-impala-kudu)
	COMMAND="start-impala-kudu"
	shift
    ;;
    stop-zookeeper)
    COMMAND="stop-zookeeper"
	shift
    ;;
    start-zookeeper)
	COMMAND="start-zookeeper"
	shift
    ;;
    stop-postgres)
    COMMAND="stop-postgres"
	shift
    ;;
    start-postgres)
	COMMAND="start-postgres"
	shift
    ;;
    stop-hue)
    COMMAND="stop-hue"
	shift
    ;;
    start-hue)
	COMMAND="start-hue"
	shift
    ;;
    start-all)
	COMMAND="start-all"
	shift
    ;;
    stop-all)
	COMMAND="stop-all"
	shift
    ;;
    restart)
    COMMAND="restart"
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
    -k*|--kudu*)
    export OPTIONS = "$OPTIONS -k"
    export USE_KUDU="true"
    shift
    ;;
    --debug*)
    export OPTIONS = "$OPTIONS --debug"
    export DEBUG="true"
    shift
    ;;
    -d*|--detach*)
    export OPTIONS = "$OPTIONS -d"
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
    export DOCKER_COMPOSE_FILE="docker-compose-pipeline.yml"
    HOSTNAME=docker
fi

if [[ ($MODE = "PRODUCTION") ]]; then
    export PRODUCTION="true"
fi

if [[ ($COMMAND = "restart") || ($COMMAND = "stop") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE down -v
fi

if [[ ($COMMAND = "stop-zookeeper") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/zookeeper.yml down -v
fi

if [[ ($COMMAND = "start-zookeeper") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/zookeeper.yml up $DETACH
fi

if [[ ($COMMAND = "stop-kafka") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/kafka.yml down -v
fi

if [[ ($COMMAND = "start-kafka") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/kafka.yml up $DETACH
fi

if [[ ($COMMAND = "stop-debezium") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/debezium.yml down -v
fi

if [[ ($COMMAND = "start-debezium") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/debezium.yml up $DETACH
fi

if [[ ($COMMAND = "stop-hbase") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hbase.yml down -v
fi

if [[ ($COMMAND = "start-hbase") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hbase.yml up $DETACH
fi

if [[ ($COMMAND = "stop-hive") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hive.yml down -v
fi

if [[ ($COMMAND = "start-hive") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hive.yml up $DETACH
fi

if [[ ($COMMAND = "stop-hadoop") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hadoop.yml down -v
fi

if [[ ($COMMAND = "start-hadoop") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hadoop.yml up $DETACH
fi

if [[ ($COMMAND = "start-impala") ]]; then
    if [[ ($USE_KUDU = "true") ]]; then
	   docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala-kudu.yml up $DETACH
	else
	   docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala.yml up $DETACH
	fi
fi

if [[ ($COMMAND = "stop-impala") ]]; then
    if [[ ($USE_KUDU = "true") ]]; then
	   docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala-kudu.yml down -v
	else
	   docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala.yml down -v
	fi
fi

if [[ ($COMMAND = "stop-hue") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hue.yml down -v
fi

if [[ ($COMMAND = "start-hue") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hue.yml up $DETACH
fi

if [[ ($COMMAND = "stop-postgres") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/postgres.yml down -v
fi

if [[ ($COMMAND = "start-postgres") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/postgres.yml up $DETACH
fi

if [[ ($COMMAND = "stop-all") ]]; then
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hue.yml down -v
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala-kudu.yml down -v
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala.yml down -v
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hadoop.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hive.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hbase.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hadoop.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/debezium.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/postgres.yml down -v
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/kafka.yml down -v
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/zookeeper.yml down -v
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE down -v
fi

if [[ ($COMMAND = "stop") || ($COMMAND = "stop-all") || ($COMMAND = "stop-postgres") || ($COMMAND = "start-postgres") || ($COMMAND = "stop-zookeeper") || ($COMMAND = "start-zookeeper") || ($COMMAND = "stop-hive") || ($COMMAND = "start-hive") || ($COMMAND = "stop-kafka") || ($COMMAND = "start-kafka") || ($COMMAND = "start-hadoop") || ($COMMAND = "stop-hadoop")  || ($COMMAND = "start-debezium") || ($COMMAND = "stop-debezium") || ($COMMAND = "start-hbase") || ($COMMAND = "stop-hbase") || ($COMMAND = "start-impala") || ($COMMAND = "stop-impala") || ($COMMAND = "start-impala-kudu") || ($COMMAND = "stop-impala-kudu") || ($COMMAND = "start-hue") || ($COMMAND = "stop-hue")  ]]; then
	exit 0
fi

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
		docker-compose -f ./docker/$DOCKER_COMPOSE_FILE push
	fi
	exit 0
fi

if [[ ($COMMAND = "start") || ($COMMAND = "restart") ]]; then
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE build
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE up -d $SERVICE
    docker/wait-for-it.sh $HOSTNAME:99$ID -t 120

	docker exec -it docker_${SERVICE}_1 bash /opt/parrot/docker/wildfly/deploy.sh

	if [[ ($MODE = "LOCAL_UNIT") || ($MODE = "LOCAL_INTEGRATION") ]]; then
		docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE logs -f
    fi
fi

if [[ ($COMMAND = "start-all") || ($COMMAND = "restart-all") ]]; then
	docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 2> /dev/null
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE build
	docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/zookeeper.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/kafka.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/postgres.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/debezium.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hive.yml up -d
    if [[ ($USE_KUDU = "true") ]]; then
        docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hadoop.yml up -d
    		docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala-kudu.yml up -d
    	else
    		docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/impala.yml up -d
    fi
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hbase.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/hue.yml up -d
    docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE up -d $SERVICE
    docker/wait-for-it.sh $HOSTNAME:99$ID -t 120

	docker exec -it parrot_${SERVICE}_1 bash /opt/parrot/docker/wildfly/deploy.sh

	if [[ ($MODE = "LOCAL_UNIT") || ($MODE = "LOCAL_INTEGRATION") ]]; then
		docker-compose $DOCKER_COMPOSE_OPTIONS -f docker/$DOCKER_COMPOSE_FILE logs -f
    fi
fi