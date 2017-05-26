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
	public String connectorClass;

	/**
	 * BASE PROPERTIES
	 */
	@JsonProperty(value = "plugin.name", defaultValue = "decoderbufs")
	public String pluginName;

	@JsonProperty(value = "slot.name", defaultValue = "debezium")
	public String slotName;

	@JsonProperty(value = "slot.drop_on_stop", defaultValue = "false")
	public String slotDropOnStop;

	@JsonProperty(value = "database.hostname", required=true)
	public String databaseHostname;

	@JsonProperty(value = "database.port", defaultValue = "5432", required=true)
	public String databasePort;

	@JsonProperty(value = "database.user", required=true)
	public String databaseUser;

	@JsonProperty(value = "database.password", required=true)
	public String databasePassword;

	@JsonProperty(value = "database.dbname", required=true)
	public String databaseDbName;

	@JsonProperty(value = "database.server.name")
	public String databaseServerName;

	@JsonProperty(value = "database.sslmode", defaultValue = "disabled")
	public String databaseSslMode;

	@JsonProperty(value = "database.sslcert")
	public String databaseSslCert;

	@JsonProperty(value = "database.sslkey")
	public String databaseSslKey;

	@JsonProperty(value = "database.sslpassword")
	public String databaseSslPassword;

	@JsonProperty(value = "database.sslrootcert")
	public String databaseSslRootCert;

	@JsonProperty(value = "schema.whitelist")
	public String schemaWhitelist;

	@JsonProperty(value = "schema.blacklist")
	public String schemaBlacklist;

	@JsonProperty(value = "table.whitelist")
	public String tableWhitelist;

	@JsonProperty(value = "table.blacklist")
	public String tableBlacklist;

	@JsonProperty(value = "column.whitelist")
	public String columnWhitelist;

	@JsonProperty(value = "column.blacklist")
	public String columnBlacklist;

	@JsonProperty(value = "time.precision.mode", defaultValue = "adaptive")
	public String timePrecisionMode;

	/**
	 * ADVANCED PROPERTIES
	 */
	@JsonProperty(value = "snapshot.mode", defaultValue = "initial")
	public String snapshotMode;

	@JsonProperty(value = "snapshot.lock.timeout.ms", defaultValue = "10000")
	public String snapshotLockTimeoutMs;

	@JsonProperty(value = "rows.fetch.size", defaultValue = "10240")
	public String rowsFetchSize;

	@JsonProperty(value = "max.queue.size", defaultValue = "20240")
	public String maxQueueSize;

	@JsonProperty(value = "max.batch.size", defaultValue = "10240")
	public String maxBatchSize;

	@JsonProperty(value = "poll.interval.ms", defaultValue = "1000")
	public String pollIntervalMs;

}
