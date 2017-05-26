#/bin/bash

export PGPASSFILE=.pgpass

pwd=`pwd`
filelist=(`ls ${pwd}/docker/sources/postgres/sql/01_init/*_*.sql | sort -n -t _ -k 1`)

POSTGRESQL_PORT=5432
if [[ $1 ]]; then
	POSTGRESQL_HOSTNAME=$1
fi
if [[ $2 ]]; then
	POSTGRESQL_PORT=$2
fi

for f in "${filelist[@]}"; do
	echo $f
	psql -h $POSTGRESQL_HOSTNAME -p $POSTGRESQL_PORT -U postgres -f $f
done

filelist=(`ls ${pwd}/docker/sources/postgres/sql/02_create/*_*.sql | sort -n -t _ -k 1`)

for f in "${filelist[@]}"; do
   psql -h $POSTGRESQL_HOSTNAME -p $POSTGRESQL_PORT -U postgres -d $DB_NAME -f $f
done

exit 0