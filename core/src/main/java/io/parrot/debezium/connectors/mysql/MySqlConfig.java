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
	String connectorClass;

	/**
	 * BASE PROPERTIES
	 */
	@JsonProperty(value = "database.hostname")
	String databaseHostname;

	@JsonProperty(value = "database.port", defaultValue = "3306")
	String databasePort;

	@JsonProperty(value = "database.user")
	String databaseUser;

	@JsonProperty(value = "database.password")
	String databasePassword;

	@JsonProperty(value = "database.server.id")
	String databaseServerId;

	@JsonProperty(value = "database.server.name")
	String databaseServerName;

	@JsonProperty(value = "database.history.kafka.topic")
	String databaseHistoryKafkaTopic;

	@JsonProperty(value = "database.history.kafka.bootstrap.servers")
	String databaseHistoryKafkaBootstrapServers;

	@JsonProperty(value = "database.whitelist")
	String databaseWhitelist;

	@JsonProperty(value = "database.blacklist")
	String databaseBlacklist;

	@JsonProperty(value = "table.whitelist")
	String tableWhitelist;

	@JsonProperty(value = "table.blacklist")
	String tableBlacklist;

	@JsonProperty(value = "column.blacklist")
	String columnBlacklist;

	@JsonProperty(value = "column.truncate.to.length.chars")
	String columnTruncateToLengthChars;

	@JsonProperty(value = "column.mask.with.length.chars")
	String columnMaskWithLenghtChars;

	@JsonProperty(value = "time.precision.mode", defaultValue = "adaptive")
	String timePrecisionMode;

	@JsonProperty(value = "decimal.handling.mode", defaultValue = "precise")
	String decimalHandlingMode;

	@JsonProperty(value = "include.schema.changes", defaultValue = "true")
	String includeSchemaChanges;

	/**
	 * ADVANCED PROPERTIES
	 */
	@JsonProperty(value = "connect.timeout.ms", defaultValue = "30000")
	String connectTimeoutMs;

	@JsonProperty(value = "gtid.source.includes")
	String gtidSourceIncludes;

	@JsonProperty(value = "gtid.source.excludes")
	String gtidSourceExcludes;

	@JsonProperty(value = "rows.fetch.size", defaultValue = "10240")
	String rowsFetchSize;

	@JsonProperty(value = "max.queue.size", defaultValue = "1024")
	String maxQueueSize;

	@JsonProperty(value = "max.batch.size", defaultValue = "2048")
	String maxBatchSize;

	@JsonProperty(value = "poll.interval.ms", defaultValue = "1000")
	String pollIntervalMs;

	public String getConnectorClass() {
		return connectorClass;
	}

	public String getDatabaseHostname() {
		return databaseHostname;
	}

	public String getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getDatabaseServerId() {
		return databaseServerId;
	}

	public String getDatabaseServerName() {
		return databaseServerName;
	}

	public String getDatabaseHistoryKafkaTopic() {
		return databaseHistoryKafkaTopic;
	}

	public String getDatabaseHistoryKafkaBootstrapServers() {
		return databaseHistoryKafkaBootstrapServers;
	}

	public String getDatabaseWhitelist() {
		return databaseWhitelist;
	}

	public String getDatabaseBlacklist() {
		return databaseBlacklist;
	}

	public String getTableWhitelist() {
		return tableWhitelist;
	}

	public String getTableBlacklist() {
		return tableBlacklist;
	}

	public String getColumnBlacklist() {
		return columnBlacklist;
	}

	public String getColumnTruncateToLengthChars() {
		return columnTruncateToLengthChars;
	}

	public String getColumnMaskWithLenghtChars() {
		return columnMaskWithLenghtChars;
	}

	public String getTimePrecisionMode() {
		return timePrecisionMode;
	}

	public String getDecimalHandlingMode() {
		return decimalHandlingMode;
	}

	public String getIncludeSchemaChanges() {
		return includeSchemaChanges;
	}

	public String getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public String getGtidSourceIncludes() {
		return gtidSourceIncludes;
	}

	public String getGtidSourceExcludes() {
		return gtidSourceExcludes;
	}

	public String getRowsFetchSize() {
		return rowsFetchSize;
	}

	public String getMaxQueueSize() {
		return maxQueueSize;
	}

	public String getMaxBatchSize() {
		return maxBatchSize;
	}

	public String getPollIntervalMs() {
		return pollIntervalMs;
	}

}
