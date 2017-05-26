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
package io.parrot.debezium.event.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ChangeEventDataSchemaField {

	@JsonProperty(value = "field")
	public String field;
	
	@JsonProperty(value = "type")
	public String type;
	
	@JsonProperty(value = "optional")
	public Boolean optional;
	
	@JsonProperty(value = "name")
	public String name;
	
	@JsonProperty(value = "index")
	public String index;
	
	@JsonProperty(value="schema")
	public ChangeEventDataSchema schema;
	
	@JsonProperty(value="fields")
	public ChangeEventDataSchemaField[] fields;
	
}
