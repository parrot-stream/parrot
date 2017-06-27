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
package io.parrot.data;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ChangeEventData {

    @JsonProperty(value = "schema")
    ChangeEventSchema schema;

    @JsonProperty(value = "payload")
    Map<String, Object> payload;

    public ChangeEventData() {
    }

    public ChangeEventData(ChangeEventSchema pSchema) {
        schema = pSchema;
        payload = new HashMap<String, Object>();
    }

    public ChangeEventSchema schema() {
        return schema;
    }

    public Map<String, Object> payload() {
        return payload;
    }

    public void addPayloadData(String pName, Object pValue) {
        payload.put(pName, pValue);
    }
    
}