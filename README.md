# Project build

### Generazione dei sorgenti

I sorgenti per i servizi JAX-RS possono essere generati con il seguente comando:

    mvn clean generate-sources -Papi

I sorgenti delle entità JPA possono essere generate a partire dal DB con il seguente comando:

    mvn clean generate-sources -Pjpa

I sorgenti delle classi QueryDSL possono essere generati con il seguente comando:

    mvn clean generate-sources -Pqdsl
    
Per generare JAX-RS + QueryDSL il comando è:

    mvn clean generate-sources -Papi,qdsl
    
> **NOTA**: Al fine di generare i sorgeti JPA è necessario aver avviato il Docker del servizio (vedi sezione **Start servizio con Docker**).

### Esecuzione dei test

All'interno del progetto sono definite due tipologie di TEST:

- **Unit Test**: si tratta di tutti quei test che non hanno bisogno del container per essere eseguiti.
- **Integration Test**: si tratta di tutti quei test che devono essere eseguiti sul container. Vengono considerati come integration test tutte le classi che iniziano con il suffisso `IT`. Tutte le altre classi di test saranno considerate unit test.

Gli unit-test sono automaticamente eseguiti quando viene lanciato il comando: 

    mvn clean package

Per lanciare gli integration-test si può usare il seguente comando:

    mvn verify

Affinché si possano eseguire gli integration test è necessario aver prima avviato il servizio Docker (vedi sezione **Start servizio con Docker**).

# Database Naming Conventions

Esempio per le tabelle FINANZIAMENTO e STATO_FINANZIAMENTO

**FINANZIAMENTO**

| Nome Colonna		              |	Tipo Dato	  | Descrizione
|---------------------------------|---------------|------------
| **id**			              | bigserial	  | E' la chiave tecnica, costituita sempre da una sola colonna di nome id
| **cod_finanziamento**		      | varchar(100)  | E' il codice di business dell'entità. Nel caso del finanziamento è un varchar
| **id_stato_finanziamento**      | biginteger	  | E' il codice corrispondente alla pk della tabella STATO_FINANZIAMENTO.
| **dat_creazione_finanziamento** |	date		  | E' la data di creazione del finanziamento
| **imp_finanziamento**		      | decimal(15,2) |	E' l'importo del finanziamento
| **cod_prodotto**			      | varchar(100)  |	Il codice prodotto del finanziamento
| **num_mesi_finanziamento**	  | integer		  | E' la durata del finanziamento in mesi
| **dat_stipula**			      | date		  | Data stipula contratto di finanziamento

**STATO_FINANZIAMENTO**

| Nome Colonna		             | Tipo Dato	| Descrizione
|--------------------------------|--------------|------------
| **id**				         | bigserial	| E' la chiave tecnica, costituita sempre da una sola colonna di nome id
| **cod_stato_finanziamento**	 | varchar(100)	| E' il codice di business dell'entità. Nel caso dello stato finanziamento è un varchar
| **desc_stato_finanziamento**   | varchar(100)	| E' la descrizione dello stato del finanziameno

# 
# Start servizio con Docker

Per avviare il servizio "Anagrafica Rapporti" tramite Docker, eseguire la seguente sequenza di comandi, a partire dalla root di progetto:

    cd docker
    docker-compose build
    docker-compose up

Per fermare il servizio "Anagrafica Rapporti" eseguire:

    docker-compose down -v --remove-orphans
    
Spiegazione dei comandi:

    docker-compose build    il comando esegue le istruzioni contenute in Dockerfile al fine di generare l'immagine Docker del servizio
    docker-compose up       il comando avvia l'immagine Docker precedentemente creata. Durante l'avvio vengono inizializzati PosgtgreSQL e JBoss.
                            In Postgres vengono eseguite le DDL e DML contenute in docker/postgres/sql/create e in docker/postgres/sql/update.
                            In JBoss viene installato il driver PostgreSQL e creato il relativo datasource.
    docker-compose down     il comando ferma il servizio, eliminando i volumi Docker ed eventuali processi Docker orfani al fine di ripristinare
                            una situazione "pulita" per un successivo avvio del servizio.

Per collegarsi al Docker:

    docker exec -it docker_galileo_1 bash
            
## PostgreSQL

E' possibile accedere al Dababase PostgreSQL locale con pgAdmin3 indicando nei dati di connessione:

    Host    localhost
    Port    15432
    
## JBoss EAP 7

E' possibile accedere all'istanza di JBoss EAP 7 tramite URL:

    http://localhost:9990
    
con le seguenti credenziali:

    Username:   galileo
    Password:   galileo
    
## Troubleshooting

Nel caso in cui all'avvio di un Docker si riscostrasse la segnalazione di una o più porte già in uso, dovute ad un precende avvio del container, effettuare un restart del Docker daemon con:

    service docker restart