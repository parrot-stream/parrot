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

import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.SchemaBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ChangeEventSchema {

    @JsonProperty(value = "field")
    String field;

    @JsonProperty(value = "type")
    String type;

    @JsonProperty(value = "optional")
    Boolean optional;

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "index")
    String index;

    @JsonProperty(value = "defaultValue")
    String defaultValue;

    @JsonProperty(value = "doc")
    String doc;

    @JsonProperty(value = "schema")
    ChangeEventSchema schema;

    @JsonProperty(value = "version")
    Integer version;

    @JsonProperty(value = "fields")
    List<ChangeEventSchema> fields;

    @JsonProperty(value = "parameters")
    Map<String, String> parameters;

    public ChangeEventSchema() {
    }

    public ChangeEventSchema(String pName, Type pType) {
        name = pName;
        type = pType.name();
    }

    public Type type() {
        return Type.valueOf(type.toUpperCase());
    }

    public String name() {
        return name;
    }

    public ChangeEventSchema schema() {
        return schema;
    }

    public Integer version() {
        return version;
    }

    public List<ChangeEventSchema> fields() {
        return fields;
    }

    public Map<String, String> parameters() {
        return parameters;
    }

    public ChangeEventSchema getField(String pFieldName) {
        for (ChangeEventSchema f : fields()) {
            if (f.field().equalsIgnoreCase(pFieldName)) {
                return f;
            }
        }
        return null;
    }

    public boolean contains(String pFieldName) {
        for (ChangeEventSchema f : fields()) {
            if (f.field().equalsIgnoreCase(pFieldName)) {
                return true;
            }
        }
        return false;
    }

    public Schema toKafkaConnectSchema() {
        SchemaBuilder schemaBuilder = SchemaBuilder.type(type()).version(version()).name(name());
        if (schema() != null) {
            schemaBuilder.field(schema().name(), schema().toKafkaConnectSchema());
        } else {
            if (fields() != null) {
                for (ChangeEventSchema f : fields()) {
                    schemaBuilder.field(f.field(), f.toKafkaConnectSchema());
                }
            }
        }
        return schemaBuilder.build();
    }

    public boolean isOptional() {
        return optional;
    }

    public Object defaultValue() {
        return defaultValue;
    }

    public String doc() {
        return doc;
    }

    public String index() {
        return index;
    }

    public String field() {
        return field;
    }

    public enum Parameters {
        SCALE
    }

}
