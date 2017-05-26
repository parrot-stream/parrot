-- TEST TABLEs
CREATE TABLE parrot.table_with_simple_pk
(
	id NUMBER(10) NOT NULL PRIMARY KEY,
	attribute_1 VARCHAR2(10),
    attribute_2 VARCHAR2(10)
);

CREATE TABLE parrot.table_with_composite_pk
(
	id NUMBER(10) NOT NULL,
	attribute_1 VARCHAR2(10) NOT NULL,
    attribute_2 VARCHAR2(10),
    PRIMARY KEY(id, attribute_1)
);

CREATE TABLE parrot.table_without_pk
(
	id NUMBER(10) NOT NULL,
    attribute_1 VARCHAR2(10),
    attribute_2 VARCHAR2(10)
);