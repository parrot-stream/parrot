#!/bin/bash
set -e

export PGPASSFILE=.pgpass

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER parrot;
    CREATE DATABASE parrot;
    GRANT ALL PRIVILEGES ON DATABASE parrot TO parrot;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d parrot <<-EOSQL
    CREATE SCHEMA parrot;
    GRANT USAGE ON SCHEMA parrot TO parrot;
EOSQL