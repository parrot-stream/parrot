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
package io.parrot.data.reader;

import java.time.Instant;
import java.util.List;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;

import io.debezium.data.Envelope;
import io.debezium.data.Envelope.FieldName;
import io.debezium.data.Envelope.Operation;
import io.parrot.data.ParrotField;
import io.parrot.data.ParrotValue;

public class ChangeEventValueReader extends ChangeEventReader {

    public ChangeEventValueReader(SchemaAndValue data) {
        super(data);
    }

    /**
     * SCHEMA
     */
    public Field getBeforeSchemaField(String name) {
        for (Field f : getSchemaFields(Envelope.FieldName.BEFORE)) {
            if (f.name().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    public Field getAfterSchemaField(String name) {
        for (Field f : getSchemaFields(Envelope.FieldName.AFTER)) {
            if (f.name().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    public List<Field> getSchemaFields(String name) {
        return data.schema().field(name).schema().fields();
    }

    /**
     * VALUES
     */
    public Struct getBeforeValue() {
        return (Struct) ((Struct) data.value()).get(FieldName.BEFORE);
    }

    public Struct getAfterValue() {
        return (Struct) ((Struct) data.value()).get(FieldName.AFTER);
    }

    public Struct getSourceValue() {
        return (Struct) ((Struct) data.value()).get(FieldName.SOURCE);
    }

    public Operation getOperationType() {
        return Operation.forCode(((Struct) data.value()).getString(FieldName.OPERATION));
    }

    public Long getDebeziumTimestamp() {
        return ((Struct) data.value()).getInt64(FieldName.TIMESTAMP);
    }

    public String getSourceName() {
        return (((Struct) data.value()).getStruct(Envelope.FieldName.SOURCE)).getString("name");
    }

    public ParrotValue toParrotValue() {
        ParrotValue parrotValue = new ParrotValue();
        List<Field> fields = getSchemaFields(Envelope.FieldName.AFTER);
        Struct afterValue = ((Struct) data.value()).getStruct(Envelope.FieldName.AFTER);
        for (Field f : fields) {
            parrotValue.addField(f.name(), new ParrotField(f.name(), afterValue.get(f), f.schema()));
        }
        return parrotValue;
    }

}
