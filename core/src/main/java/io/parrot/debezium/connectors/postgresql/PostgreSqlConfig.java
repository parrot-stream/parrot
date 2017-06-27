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
package io.parrot.debezium.connectors.postgresql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.parrot.debezium.connectors.Config;

@JsonInclude(Include.NON_NULL)
public class PostgreSqlConfig extends Config {

	@JsonProperty(value = "connector.class", defaultValue = "io.debezium.connector.postgresql.PostgresConnector", required = true)
	String connectorClass;

	/**
	 * BASE PROPERTIES
	 */
	@JsonProperty(value = "plugin.name", defaultValue = "decoderbufs")
	String pluginName;

	@JsonProperty(value = "slot.name", defaultValue = "debezium")
	String slotName;

	@JsonProperty(value = "slot.drop_on_stop", defaultValue = "false")
	String slotDropOnStop;

	@JsonProperty(value = "database.hostname", required=true)
	String databaseHostname;

	@JsonProperty(value = "database.port", defaultValue = "5432", required=true)
	String databasePort;

	@JsonProperty(value = "database.user", required=true)
	String databaseUser;

	@JsonProperty(value = "database.password", required=true)
	String databasePassword;

	@JsonProperty(value = "database.dbname", required=true)
	String databaseDbName;

	@JsonProperty(value = "database.server.name")
	String databaseServerName;

	@JsonProperty(value = "database.sslmode", defaultValue = "disabled")
	String databaseSslMode;

	@JsonProperty(value = "database.sslcert")
	String databaseSslCert;

	@JsonProperty(value = "database.sslkey")
	String databaseSslKey;

	@JsonProperty(value = "database.sslpassword")
	String databaseSslPassword;

	@JsonProperty(value = "database.sslrootcert")
	String databaseSslRootCert;

	@JsonProperty(value = "schema.whitelist")
	String schemaWhitelist;

	@JsonProperty(value = "schema.blacklist")
	String schemaBlacklist;

	@JsonProperty(value = "table.whitelist")
	String tableWhitelist;

	@JsonProperty(value = "table.blacklist")
	String tableBlacklist;

	@JsonProperty(value = "column.whitelist")
	String columnWhitelist;

	@JsonProperty(value = "column.blacklist")
	String columnBlacklist;

	@JsonProperty(value = "time.precision.mode", defaultValue = "adaptive")
	String timePrecisionMode;

	/**
	 * ADVANCED PROPERTIES
	 */
	@JsonProperty(value = "snapshot.mode", defaultValue = "initial")
	String snapshotMode;

	@JsonProperty(value = "snapshot.lock.timeout.ms", defaultValue = "10000")
	String snapshotLockTimeoutMs;

	@JsonProperty(value = "rows.fetch.size", defaultValue = "10240")
	String rowsFetchSize;

	@JsonProperty(value = "max.queue.size", defaultValue = "20240")
	String maxQueueSize;

	@JsonProperty(value = "max.batch.size", defaultValue = "10240")
	String maxBatchSize;

	@JsonProperty(value = "poll.interval.ms", defaultValue = "1000")
	String pollIntervalMs;

	public String getConnectorClass() {
		return connectorClass;
	}

	public String getPluginName() {
		return pluginName;
	}

	public String getSlotName() {
		return slotName;
	}

	public String getSlotDropOnStop() {
		return slotDropOnStop;
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

	public String getDatabaseDbName() {
		return databaseDbName;
	}

	public String getDatabaseServerName() {
		return databaseServerName;
	}

	public String getDatabaseSslMode() {
		return databaseSslMode;
	}

	public String getDatabaseSslCert() {
		return databaseSslCert;
	}

	public String getDatabaseSslKey() {
		return databaseSslKey;
	}

	public String getDatabaseSslPassword() {
		return databaseSslPassword;
	}

	public String getDatabaseSslRootCert() {
		return databaseSslRootCert;
	}

	public String getSchemaWhitelist() {
		return schemaWhitelist;
	}

	public String getSchemaBlacklist() {
		return schemaBlacklist;
	}

	public String getTableWhitelist() {
		return tableWhitelist;
	}

	public String getTableBlacklist() {
		return tableBlacklist;
	}

	public String getColumnWhitelist() {
		return columnWhitelist;
	}

	public String getColumnBlacklist() {
		return columnBlacklist;
	}

	public String getTimePrecisionMode() {
		return timePrecisionMode;
	}

	public String getSnapshotMode() {
		return snapshotMode;
	}

	public String getSnapshotLockTimeoutMs() {
		return snapshotLockTimeoutMs;
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
