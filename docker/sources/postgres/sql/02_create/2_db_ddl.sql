-- TEST TABLEs
CREATE TABLE parrot.table_with_simple_pk
(
	id BIGSERIAL NOT NULL PRIMARY KEY,
	attribute_1 VARCHAR(100),
    attribute_2 VARCHAR(100)
)
WITH (
  OIDS=FALSE
);

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

CREATE TABLE parrot.table_without_pk
(
	id BIGSERIAL NOT NULL,
    attribute_1 VARCHAR(100),
    attribute_2 VARCHAR(100)
)
WITH (
  OIDS=FALSE
);