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
package io.parrot.debezium.event.reader.postgresql;

import io.parrot.debezium.event.data.ChangeEventData;
import io.parrot.debezium.event.data.ChangeEventDataSchemaField;
import io.parrot.debezium.event.reader.ChangeEventReader;
import io.parrot.exception.ParrotException;

public class PostgreSqlChangeEventReader extends ChangeEventReader {

	public PostgreSqlChangeEventReader(ChangeEventData data) {
		super(data);
	}

	@Override
	public ChangeEventDataSchemaField getFieldIndex(int index) {
		if (index < 0 || index >= getNumFields()) {
			throw new ParrotException("Column Index out of range [0, " + (getNumFields() - 1) + "]");
		}
		int pos = 0;
		for (ChangeEventDataSchemaField f : data.schema.fields) {
			if (f.index != null) {
				if (String.valueOf(index).equalsIgnoreCase(f.index)) {
					return f;
				}
			} else {
				if (pos == index) {
					return f;
				}
			}
			pos++;
		}
		return null;
	}

}