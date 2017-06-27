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

import java.util.List;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;

import io.parrot.data.ParrotField;
import io.parrot.data.ParrotKey;

public class ChangeEventKeyReader extends ChangeEventReader {

    public ChangeEventKeyReader(SchemaAndValue data) {
        super(data);
    }

    public List<Field> getSchemaFields() {
        return data.schema().fields();
    }

    public ParrotKey toParrotKey() {
        ParrotKey key = new ParrotKey();
        for (Field f : getSchemaFields()) {
            key.addKey(f.name(), new ParrotField(f.name(), getValue(f.name()), f.schema()));
        }
        return key;
    }

    public Field getSchemaField(String name) {
        for (Field f : data.schema().fields()) {
            if (f.name().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    public boolean isOptional(String pName) {
        return getSchemaField(pName).schema().isOptional();
    }

    public Type getFieldType(String pFieldName) {
        return getSchemaField(pFieldName).schema().type();
    }

    /**
     * VALUES
     */
    public Object getValue(String pField) {
        return ((Struct) data.value()).get(pField);
    }
}
