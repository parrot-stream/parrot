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
package io.parrot.debezium.connectors.oracle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.parrot.debezium.connectors.Config;

@JsonInclude(Include.NON_NULL)
class OracleDbConfig extends Config {

	@JsonProperty(value = "connector.class", defaultValue = "io.debezium.connector.oracle.OracleDbConnector")
	public String connectorClass;

	/**
	 * BASE PROPERTIES
	 */
	@JsonProperty(value = "mongodb.hosts")
	public String mongoDbHosts;

	@JsonProperty(value = "mongodb.name")
	public String mongoDbName;

	@JsonProperty(value = "mongodb.user")
	public String mongoDbUser;

	@JsonProperty(value = "mongodb.password")
	public String mongoDbPassword;

	@JsonProperty(value = "collection.whitelist")
	public String collectionWhitelist;

	@JsonProperty(value = "collection.blacklist")
	public String collectionBlacklist;

	@JsonProperty(value = "initial.sync.max.threads", defaultValue = "1")
	public String initialSyncMaxThreads;

	/**
	 * ADVANCED PROPERTIES
	 */
	@JsonProperty(value = "connect.backoff.initial.delay.ms", defaultValue = "1000")
	public String connectBackoffInitialDelayMs;

	@JsonProperty(value = "connect.backoff.max.delay.ms", defaultValue = "1000")
	public String connectBackoffMaxDelayMs;

	@JsonProperty(value = "connect.max.attempts", defaultValue = "10")
	public String connectMaxAttempts;

	@JsonProperty(value = "mongodb.members.auto.discover", defaultValue = "true")
	public String mongoDbMembersAutoDiscover;

	@JsonProperty(value = "max.queue.size", defaultValue = "1024")
	public String maxQueueSize;

	@JsonProperty(value = "max.batch.size", defaultValue = "2048")
	public String maxBatchSize;

	@JsonProperty(value = "poll.interval.ms", defaultValue = "1000")
	public String pollIntervalMs;

}
