#/bin/bash

export PGPASSFILE=.pgpass

pwd=`pwd`
if [[ $SERVICE ]]; then
  filelist=(`ls "$pwd"/$SERVICE/docker/postgres/sql/01_init/*_*.sql | sort -n -t _ -k 1`)
else
  filelist=(`ls "$pwd"/docker/postgres/sql/01_init/*_*.sql | sort -n -t _ -k 1`)
fi

DB_PORT=5432
if [[ $1 ]]; then
	DB_HOSTNAME=$1
fi
if [[ $2 ]]; then
	DB_PORT=$2
fi

for f in "${filelist[@]}"; do
	psql -h $DB_HOSTNAME -p $DB_PORT -U postgres -f $f
done

if [ -n "$SERVICE" ]; then
  filelist=(`ls "$pwd"/$SERVICE/docker/postgres/sql/02_create/*_*.sql | sort -n -t _ -k 1`)
else
  filelist=(`ls "$pwd"/docker/postgres/sql/02_create/*_*.sql | sort -n -t _ -k 1`)
fi
for f in "${filelist[@]}"; do
   psql -h $DB_HOSTNAME -p $DB_PORT -U postgres -d $DB_NAME -f $f
done

if [[ $SERVICE ]]; then
  dirlist=(`ls -d "$pwd"/$SERVICE/docker/postgres/sql/03_update/*/ | sort`)
else
  dirlist=(`ls -d "$pwd"/docker/postgres/sql/03_update/*/ | sort`)
fi

for d in "${dirlist[@]}"; do
  psql -h $DB_HOSTNAME -p $DB_PORT -U postgres -d $DB_NAME -f $d/db_ddl.sql
  psql -h $DB_HOSTNAME -p $DB_PORT -U postgres -d $DB_NAME -f $d/db_ddl_envers.sql
  psql -h $DB_HOSTNAME -p $DB_PORT -U postgres -d $DB_NAME -f $d/db_dml.sql
done

exit 0