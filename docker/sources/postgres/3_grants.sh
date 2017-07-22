#!/bin/bash
set -e

export PGPASSFILE=.pgpass

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d parrot <<-EOSQL

	GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA parrot TO parrot;
    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA parrot TO parrot;
	
EOSQL