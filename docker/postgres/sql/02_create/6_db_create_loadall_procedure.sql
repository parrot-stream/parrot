create OR REPLACE FUNCTION parrot.loadall() RETURNS void AS $$
BEGIN
	SET statement_timeout = 0;
	SET lock_timeout = 0;
	SET client_encoding = 'UTF8';
	SET standard_conforming_strings = on;
	SET check_function_bodies = false;
	SET client_min_messages = warning;
	SET row_security = off;
	
	SET search_path = parrot, pg_catalog;

    INSERT INTO table_without_pk(id, attribute_1, attribute_2) VALUES ('Attribute 1', 'Attribute 2');
    
END;
$$ LANGUAGE plpgsql;

commit;