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
package io.parrot.debezium.connectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ActiveTask {

	@JsonProperty(value = "connector")
	String connector;

	@JsonProperty(value = "id")
	Integer id;

	@JsonProperty(value = "state")
	String state;

	@JsonProperty(value = "trace")
	String trace;

	@JsonProperty(value = "task")
	Integer task;

	@JsonProperty(value = "worker_id")
	String workerId;

	public String getConnector() {
		return connector;
	}

	public Integer getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public String getTrace() {
		return trace;
	}

	public Integer getTask() {
		return task;
	}

	public String getWorkerId() {
		return workerId;
	}

	// @JsonProperty(value = "idTask")
	// public TaskId idTask;
}
