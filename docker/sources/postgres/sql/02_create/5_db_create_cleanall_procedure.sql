create OR REPLACE FUNCTION parrot.cleanall() RETURNS void AS $$
begin
	TRUNCATE TABLE  parrot.table_with_simple_pk,
					parrot.table_with_composite_pk,
					parrot.table_without_pk;
	ALTER SEQUENCE parrot.table_with_simple_pk_id_seq RESTART WITH 1;
	ALTER SEQUENCE parrot.table_with_composite_pk_id_seq RESTART WITH 1;
	ALTER SEQUENCE parrot.table_without_pk_id_seq RESTART WITH 1;
END;
$$ LANGUAGE plpgsql;

commit;
