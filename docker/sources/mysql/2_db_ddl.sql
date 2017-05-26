-- TEST TABLEs
CREATE TABLE parrot.table_with_simple_pk
(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	attribute_1 VARCHAR(10),
    attribute_2 VARCHAR(10)
);

CREATE TABLE parrot.table_with_composite_pk
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	attribute_1 VARCHAR(10) NOT NULL,
    attribute_2 VARCHAR(10),
    PRIMARY KEY(id, attribute_1)
);

CREATE TABLE parrot.table_without_pk
(
	id BIGINT NOT NULL,
    attribute_1 VARCHAR(10),
    attribute_2 VARCHAR(10)
);