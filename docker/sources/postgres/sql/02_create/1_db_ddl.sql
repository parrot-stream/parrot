-- TEST TABLEs
DROP TABLE IF EXISTS parrot.table_numeric_types CASCADE;
CREATE TABLE parrot.table_numeric_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	n_smallint SMALLINT,
    n_integer INTEGER,
    n_bigint BIGINT,
    n_decimal DECIMAL(10, 5),
    n_numeric NUMERIC(10, 5),
    n_real REAL,
    n_double DOUBLE PRECISION,
    m_money MONEY,
    b_boolean BOOLEAN
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_numeric_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_character_types CASCADE;
CREATE TABLE parrot.table_character_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	c_varchar VARCHAR(10),
    c_char CHAR(10),
    c_text TEXT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_character_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_binary_types CASCADE;
CREATE TABLE parrot.table_binary_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	b_bytea BYTEA
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_binary_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_datetime_types CASCADE;
CREATE TABLE parrot.table_datetime_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	dt_timestamp TIMESTAMP,
	dt_timestamp_with_tz TIMESTAMP WITH TIME ZONE,
	dt_date DATE,
	dt_time TIME,
	dt_time_with_tz TIME WITH TIME ZONE,
	dt_interval INTERVAL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_datetime_types REPLICA IDENTITY FULL;

CREATE TYPE enumeration AS ENUM ('Enum_1', 'Enum_2', 'Enum_3');
DROP TABLE IF EXISTS parrot.table_enumerated_types CASCADE;
CREATE TABLE parrot.table_enumerated_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	en_enumeration enumeration
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_enumerated_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_geometric_types CASCADE;
CREATE TABLE parrot.table_geometric_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	g_point POINT,
	g_line LINE,
	g_lseg LSEG,
	g_box BOX,
	g_path PATH.
	g_polygon POLYGON,
	g_circle CIRCLE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_geometric_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_network_types CASCADE;
CREATE TABLE parrot.table_network_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	g_cidr POINT,
	g_inet LINE,
	g_macaddr LSEG
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_network_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_bitstring_types CASCADE;
CREATE TABLE parrot.table_bitstring_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	b_bit BIT(5),
	b_bit_varying BIT VARYING(5)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_bitstring_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_range_types CASCADE;
CREATE TABLE parrot.table_range_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	r_int4range INT4RANGE,
	r_int8range INT8RANGE,
	r_numrange NUMRANGE,
	r_tsrange TSRANGE,
	r_tstzrange TSTZRANGE,
	r_daterange DATERANGE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_range_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_arrays_types CASCADE;
CREATE TABLE parrot.table_arrays_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	a_int1 INTEGER[],
	a_int2 INTEGER[][]
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_arrays_types REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_other_types CASCADE;
CREATE TABLE parrot.table_other_types
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	o_uuid UUID,
	o_xml XML,
	o_json JSON,
	o_jsonb JSONB
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_other_types REPLICA IDENTITY FULL;


DROP TABLE IF EXISTS parrot.table_with_composite_pk CASCADE;
CREATE TABLE parrot.table_with_composite_pk
(
	id_1 BIGSERIAL NOT NULL,
	id_2 VARCHAR(10) NOT NULL,
    attribute VARCHAR(100),
    PRIMARY KEY(id_1, id_2)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_with_composite_pk REPLICA IDENTITY FULL;


DROP TABLE IF EXISTS ] name parrot.table_without_pk CASCADE;
CREATE TABLE parrot.table_without_pk
(
	id BIGSERIAL NOT NULL,
    attribute_1 VARCHAR(100),
    attribute_2 VARCHAR(100)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_without_pk REPLICA IDENTITY FULL;