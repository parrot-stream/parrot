-- TEST TABLEs
DROP TABLE IF EXISTS parrot.table_with_simple_pk CASCADE;
CREATE TABLE parrot.table_with_simple_pk
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	n_smallint SMALLINT,
    n_integer INTEGER,
    n_bigint BIGINT,
    n_decimal DECIMAL(10, 5),
--    n_numeric NUMERIC(10, 5),
    n_real REAL,
    n_double DOUBLE PRECISION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parrot.table_with_simple_pk REPLICA IDENTITY FULL;

DROP TABLE IF EXISTS parrot.table_with_composite_pk CASCADE;
CREATE TABLE parrot.table_with_composite_pk
(
	id BIGSERIAL NOT NULL,
	attribute_1 VARCHAR(100) NOT NULL,
    attribute_2 VARCHAR(100),
    PRIMARY KEY(id, attribute_1)
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