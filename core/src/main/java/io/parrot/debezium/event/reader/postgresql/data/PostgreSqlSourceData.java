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
package io.parrot.debezium.event.reader.postgresql.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class PostgreSqlSourceData {

	@JsonProperty(value = "name", required = true)
	public String name;

	@JsonProperty(value = "ts_usec")
	public Long tsUsec;

	@JsonProperty(value = "txId")
	public Long txId;

	@JsonProperty(value = "lsn")
	public Long lsn;

	@JsonProperty(value = "snapshot")
	public String snapshot;

	@JsonProperty(value = "last_snapshot_record")
	public Boolean lastSnapshotRecord;

}
