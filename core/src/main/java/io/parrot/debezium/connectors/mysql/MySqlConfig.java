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
package io.parrot.debezium.connectors.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.parrot.debezium.connectors.Config;

@JsonInclude(Include.NON_NULL)
class MySqlConfig extends Config {

	@JsonProperty(value = "connector.class", defaultValue = "io.debezium.connector.mysql.MySqlConnector")
	public String connectorClass;

	/**
	 * BASE PROPERTIES
	 */
	@JsonProperty(value = "database.hostname")
	public String databaseHostname;

	@JsonProperty(value = "database.port", defaultValue = "3306")
	public String databasePort;

	@JsonProperty(value = "database.user")
	public String databaseUser;

	@JsonProperty(value = "database.password")
	public String databasePassword;

	@JsonProperty(value = "database.server.id")
	public String databaseServerId;

	@JsonProperty(value = "database.server.name")
	public String databaseServerName;

	@JsonProperty(value = "database.history.kafka.topic")
	public String databaseHistoryKafkaTopic;

	@JsonProperty(value = "database.history.kafka.bootstrap.servers")
	public String databaseHistoryKafkaBootstrapServers;

	@JsonProperty(value = "database.whitelist")
	public String databaseWhitelist;

	@JsonProperty(value = "database.blacklist")
	public String databaseBlacklist;

	@JsonProperty(value = "table.whitelist")
	public String tableWhitelist;

	@JsonProperty(value = "table.blacklist")
	public String tableBlacklist;

	@JsonProperty(value = "column.blacklist")
	public String columnBlacklist;

	@JsonProperty(value = "column.truncate.to.length.chars")
	public String columnTruncateToLengthChars;

	@JsonProperty(value = "column.mask.with.length.chars")
	public String columnMaskWithLenghtChars;

	@JsonProperty(value = "time.precision.mode", defaultValue = "adaptive")
	public String timePrecisionMode;

	@JsonProperty(value = "decimal.handling.mode", defaultValue = "precise")
	public String decimalHandlingMode;

	@JsonProperty(value = "include.schema.changes", defaultValue = "true")
	public String includeSchemaChanges;

	/**
	 * ADVANCED PROPERTIES
	 */
	@JsonProperty(value = "connect.timeout.ms", defaultValue = "30000")
	public String connectTimeoutMs;

	@JsonProperty(value = "gtid.source.includes")
	public String gtidSourceIncludes;

	@JsonProperty(value = "gtid.source.excludes")
	public String gtidSourceExcludes;

	@JsonProperty(value = "rows.fetch.size", defaultValue = "10240")
	public String rowsFetchSize;

	@JsonProperty(value = "max.queue.size", defaultValue = "1024")
	public String maxQueueSize;

	@JsonProperty(value = "max.batch.size", defaultValue = "2048")
	public String maxBatchSize;

	@JsonProperty(value = "poll.interval.ms", defaultValue = "1000")
	public String pollIntervalMs;

}
