-- TEST TABLEs
CREATE TABLE parrot.table_with_simple_pk
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	attribute_1 VARCHAR(10),
    attribute_2 VARCHAR(10)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE parrot.table_with_composite_pk
(
	id BIGSERIAL NOT NULL,
	attribute_1 VARCHAR(10) NOT NULL,
    attribute_2 VARCHAR(10),
    PRIMARY KEY(id, attribute_1)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE parrot.table_without_pk
(
	id BIGSERIAL NOT NULL,
    attribute_1 VARCHAR(10),
    attribute_2 VARCHAR(10)
)
WITH (
  OIDS=FALSE
);

-- CAMEL TABLEs
CREATE TABLE parrot.camel_messageprocessed
(
	id BIGINT NOT NULL PRIMARY KEY,
	createdat TIMESTAMP NOT NULL,
    messageid VARCHAR(100) NOT NULL,
	processorname VARCHAR(100) NOT NULL
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE parrot.hibernate_sequence START 1 INCREMENT BY 1;