# Personalizzazione Template di Progetto

Per personalizzare il template ad uno specifico servizio Galileo modificare:

- in src/main/resources/META-INF/persistence.xml
	--> schema name
- in docker/postgres/init.sh
	--> DBNAME
- in docker/postgres/sql/*
	--> db script
- in docker/jboss/domain.xml
	--> datasource (es. AnagraficaRapportoDS)
- in src/main/resources/Properties.properties
	--> service URL
- in src/main/resources/Properties.properties.pipeline-ut
	--> service URL
- in pom.xml
	--> modifiche varie
- in src/main/webapp/WEB-INF/jboss-web.xml
	--> context-root
- in src/main/webapp/WEB-INF/web.xml
    --> filter-class