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
package io.parrot.debezium.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.parrot.debezium.event.data.ChangeEventData;
import io.parrot.debezium.event.data.enums.OperationType;
import io.parrot.debezium.event.reader.postgresql.PostgreSqlChangeEventKeyReader;
import io.parrot.debezium.event.reader.postgresql.PostgreSqlChangeEventValueReader;
import io.parrot.exception.ParrotException;
import io.parrot.utils.JsonUtils;

@RunWith(CamelCdiRunner.class)
public class ITDebeziumPostgreSqlChangeEventReaderTest {

	final static String JSON_PATH_EVENT_KEY_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventKey.json";
	final static String JSON_PATH_EVENT_VALUE_CREATE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueCreate.json";
	final static String JSON_PATH_EVENT_VALUE_UPDATE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueUpdate.json";
	final static String JSON_PATH_EVENT_VALUE_DELETE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueDelete.json";

	@Inject
	Logger LOG;

	// @Ignore
	@Test
	public void testDeserializeDebeziumPostgreSqlDataChangeEventKey() {
		try {
			PostgreSqlChangeEventKeyReader dbxKeyReader = new PostgreSqlChangeEventKeyReader(
					readChangeEvent(JSON_PATH_EVENT_KEY_POSTGRESQL));
			assertEquals((Integer) 1, dbxKeyReader.getNumFields());
			assertEquals("id", dbxKeyReader.getKeyColumnName(0));
			assertEquals("int32", dbxKeyReader.getKeyColumnType(0).toLowerCase());
			assertFalse(dbxKeyReader.isOptional(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Ignore
	@Test(expected = ParrotException.class)
	public void testIndexOutOfBoundsException() {
		PostgreSqlChangeEventKeyReader dbxKeyReader = new PostgreSqlChangeEventKeyReader(
				readChangeEvent(JSON_PATH_EVENT_KEY_POSTGRESQL));
		dbxKeyReader.getKeyColumnName(1);
	}

	// @Ignore
	@Test
	public void testDeserializeDebeziumPostgreSqlDataChangeEventValueCreate() {
		PostgreSqlChangeEventValueReader dbxValueReader = new PostgreSqlChangeEventValueReader(
				readChangeEvent(JSON_PATH_EVENT_VALUE_CREATE_POSTGRESQL));
		assertEquals((Integer) 1, dbxValueReader.getVersion());
		assertEquals((Integer) 5, dbxValueReader.getNumFields());

		assertEquals(OperationType.CREATE, OperationType.getOperationType(dbxValueReader.getPayloadData().op));

		assertNull(dbxValueReader.getPayloadData().before);
		assertNotNull(dbxValueReader.getPayloadData().after);

		assertEquals("PostgreSQL_server", dbxValueReader.getPayloadData().source.name);
		assertEquals(new Long(1482918357011699L), dbxValueReader.getPayloadData().source.tsUsec);
		assertEquals(new Long(555), dbxValueReader.getPayloadData().source.txId);
		assertEquals(new Long("24023128"), dbxValueReader.getPayloadData().source.lsn);
		assertNull(dbxValueReader.getPayloadData().source.snapshot);
		assertNull(dbxValueReader.getPayloadData().source.lastSnapshotRecord);
	}

	// @Ignore
	@Test
	public void testDeserializeDebeziumPostgreSqlDataChangeEventValueUpdate() {
		PostgreSqlChangeEventValueReader dbxValueReader = new PostgreSqlChangeEventValueReader(
				readChangeEvent(JSON_PATH_EVENT_VALUE_UPDATE_POSTGRESQL));
		assertEquals((Integer) 1, dbxValueReader.getVersion());
		assertEquals((Integer) 5, dbxValueReader.getNumFields());

		assertEquals(OperationType.UPDATE, OperationType.getOperationType(dbxValueReader.getPayloadData().op));

		assertNotNull(dbxValueReader.getPayloadData().before);
		assertNotNull(dbxValueReader.getPayloadData().after);

		assertEquals("PostgreSQL_server", dbxValueReader.getPayloadData().source.name);
		assertEquals(new Long(1482918357011699L), dbxValueReader.getPayloadData().source.tsUsec);
		assertEquals(new Long(556), dbxValueReader.getPayloadData().source.txId);
		assertEquals(new Long("26523128"), dbxValueReader.getPayloadData().source.lsn);
		assertNull(dbxValueReader.getPayloadData().source.snapshot);
		assertNull(dbxValueReader.getPayloadData().source.lastSnapshotRecord);
	}

	// @Ignore
	@Test
	public void testDeserializeDebeziumPostgreSqlDataChangeEventValueDelete() {
		PostgreSqlChangeEventValueReader dbxValueReader = new PostgreSqlChangeEventValueReader(
				readChangeEvent(JSON_PATH_EVENT_VALUE_DELETE_POSTGRESQL));
		assertEquals((Integer) 1, dbxValueReader.getVersion());
		assertEquals((Integer) 5, dbxValueReader.getNumFields());

		assertEquals(OperationType.DELETE, OperationType.getOperationType(dbxValueReader.getPayloadData().op));

		assertNotNull(dbxValueReader.getPayloadData().before);
		assertNull(dbxValueReader.getPayloadData().after);

		assertEquals("PostgreSQL_server", dbxValueReader.getPayloadData().source.name);
		assertEquals(new Long(154918657011699L), dbxValueReader.getPayloadData().source.tsUsec);
		assertEquals(new Long(557), dbxValueReader.getPayloadData().source.txId);
		assertEquals(new Long("46523128"), dbxValueReader.getPayloadData().source.lsn);
		assertNull(dbxValueReader.getPayloadData().source.snapshot);
		assertNull(dbxValueReader.getPayloadData().source.lastSnapshotRecord);
	}

	ChangeEventData readChangeEvent(String jsonPath) {
		try {
			String json = JsonUtils.readFromFile(jsonPath);
			return JsonUtils.jsonToObject(json, ChangeEventData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
