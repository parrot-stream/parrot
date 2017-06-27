/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.parrot;

public class ParrotBaseTest {

	// Debezium Connectors
	protected static final String POSTGTRESQL_CONNECTOR_NAME = "PostgreSql_Connector";
	protected static final String MYSQL_CONNECTOR_NAME = "MySql_Connector";
	protected static final String MONGODB_CONNECTOR_NAME = "MongoDb_Connector";
	protected static final String CASSANDRA_CONNECTOR_NAME = "Cassandra_Connector";

	protected static final String JSON_PATH_CONNECTOR_POSTGTRESQL = "json/connectors/postgresql.json";
	protected static final String JSON_PATH_CONNECTOR_MONGODB = "json/connectors/mongodb.json";
	protected static final String JSON_PATH_CONNECTOR_MYSQL = "json/connectors/mysql.json";
	protected static final String JSON_PATH_CONNECTOR_ORACLE = "json/connectors/oracle.json";

	// Processors
	protected static final String POSTGTRESQL2HBASE_PROCESSOR_NAME = "Processor-PostgresqlToHBase";
	protected static final String SINK_HBASE_NAME = "Sink-HBase";

	protected static final String JSON_PATH_PROCESSOR_CONFIG_POSTGRESQL_TO_HBASE = "json/processors/PostgresqlToHBase.json";

	protected static final String SOURCE_LOGICAL_NAME = "parrot_source";
	protected static final String SOURCE_BOOTRSTRAP_SERVERS = "localhost:9092";
	protected static final String SOURCE_TABLE_WHITELIST = "parrot.table_with_simple_pk";

	protected static final String TOPIC_NAME_1_1 = SOURCE_LOGICAL_NAME + ".parrot.table_with_simple_pk";

}
