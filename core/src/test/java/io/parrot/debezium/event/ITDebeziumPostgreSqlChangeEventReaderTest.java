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

import java.util.HashMap;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.json.JsonConverter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.debezium.data.Envelope;
import io.parrot.data.ParrotData;
import io.parrot.data.reader.ChangeEventKeyReader;
import io.parrot.data.reader.ChangeEventValueReader;
import io.parrot.data.reader.postgesql.PostgreSqlSourceType;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotHelper;

@RunWith(CamelCdiRunner.class)
public class ITDebeziumPostgreSqlChangeEventReaderTest {

    final static String JSON_PATH_EVENT_KEY_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventKey.json";
    final static String JSON_PATH_EVENT_VALUE_CREATE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueCreate.json";
    final static String JSON_PATH_EVENT_VALUE_UPDATE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueUpdate.json";
    final static String JSON_PATH_EVENT_VALUE_DELETE_POSTGRESQL = "json/postgresql/PostgreSqlDataChangeEventValueDelete.json";

    @Inject
    Logger LOG;
    static JsonConverter jsonConverter;

    @BeforeClass
    public static void ITDebeziumPostgreSqlChangeEventReaderTest() {
        jsonConverter = new JsonConverter();
        jsonConverter.configure(new HashMap<String, Object>(), false);
    }

    @Ignore
    @Test
    public void testDeserializeDebeziumPostgreSqlDataChangeEventKey() {
        try {
            ChangeEventKeyReader dbzKeyReader = new ChangeEventKeyReader(
                    readChangeEvent(JSON_PATH_EVENT_KEY_POSTGRESQL));

            assertEquals(1, dbzKeyReader.getSchemaFields().size());
            assertEquals(Type.INT32, dbzKeyReader.getFieldType("id"));
            assertFalse(dbzKeyReader.isOptional("id"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void testDeserializeDebeziumPostgreSqlDataChangeEventValueCreate() {
        ChangeEventValueReader dbxValueReader = new ChangeEventValueReader(
                readChangeEvent(JSON_PATH_EVENT_VALUE_CREATE_POSTGRESQL));
        assertEquals(1, dbxValueReader.getVersion());
        assertEquals(6, dbxValueReader.getSchemaFields(Envelope.FieldName.SOURCE).size());

        assertEquals(Envelope.Operation.CREATE, dbxValueReader.getOperationType());

        assertNull(dbxValueReader.getBeforeValue());
        assertNotNull(dbxValueReader.getAfterValue());

        assertEquals("parrot_source", dbxValueReader.getSourceName());
        assertEquals(new Long(1482918357011699L),
                dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.TS_USEC.field()));
        assertEquals(new Integer(555), dbxValueReader.getSourceValue().getInt32(PostgreSqlSourceType.TXID.field()));
        assertEquals(new Long("24023128"), dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.LSN.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.SNAPSHOT.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.LAST_SNAPSHOT_RECORD.field()));
    }

    @Ignore
    @Test
    public void testDeserializeDebeziumPostgreSqlDataChangeEventValueUpdate() {
        ChangeEventValueReader dbxValueReader = new ChangeEventValueReader(
                readChangeEvent(JSON_PATH_EVENT_VALUE_UPDATE_POSTGRESQL));
        assertEquals(1, dbxValueReader.getVersion());
        assertEquals(6, dbxValueReader.getSchemaFields(Envelope.FieldName.SOURCE).size());

        assertEquals(Envelope.Operation.UPDATE, dbxValueReader.getOperationType());

        assertNotNull(dbxValueReader.getBeforeValue());
        assertNotNull(dbxValueReader.getAfterValue());

        assertEquals("parrot_source", dbxValueReader.getSourceName());
        assertEquals(new Long(1482918357011699L),
                dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.TS_USEC.field()));
        assertEquals(new Integer(556), dbxValueReader.getSourceValue().getInt32(PostgreSqlSourceType.TXID.field()));
        assertEquals(new Long("26523128"), dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.LSN.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.SNAPSHOT.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.LAST_SNAPSHOT_RECORD.field()));
    }

    @Ignore
    @Test
    public void testDeserializeDebeziumPostgreSqlDataChangeEventValueDelete() {
        ChangeEventValueReader dbxValueReader = new ChangeEventValueReader(
                readChangeEvent(JSON_PATH_EVENT_VALUE_DELETE_POSTGRESQL));
        assertEquals(1, dbxValueReader.getVersion());
        assertEquals(6, dbxValueReader.getSchemaFields(Envelope.FieldName.SOURCE).size());

        assertEquals(Envelope.Operation.DELETE, dbxValueReader.getOperationType());

        assertNotNull(dbxValueReader.getBeforeValue());
        assertNull(dbxValueReader.getAfterValue());

        assertEquals("parrot_source", dbxValueReader.getSourceName());
        assertEquals(new Long(154918657011699L),
                dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.TS_USEC.field()));
        assertEquals(new Integer(557), dbxValueReader.getSourceValue().getInt32(PostgreSqlSourceType.TXID.field()));
        assertEquals(new Long("46523128"), dbxValueReader.getSourceValue().getInt64(PostgreSqlSourceType.LSN.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.SNAPSHOT.field()));
        assertNull(dbxValueReader.getSourceValue().getBoolean(PostgreSqlSourceType.LAST_SNAPSHOT_RECORD.field()));
    }

    SchemaAndValue readChangeEvent(String jsonPath) {
        String json = JsonUtils.readFromFile(jsonPath);
        return jsonConverter.toConnectData("", json.getBytes());
    }

    @Test
    public void testAvro() {
        try {
            ChangeEventKeyReader dbzKeyReader = new ChangeEventKeyReader(
                    readChangeEvent(JSON_PATH_EVENT_KEY_POSTGRESQL));
            ChangeEventValueReader dbxValueReader = new ChangeEventValueReader(
                    readChangeEvent(JSON_PATH_EVENT_VALUE_CREATE_POSTGRESQL));
            ParrotData parrotData = new ParrotData(dbzKeyReader.toParrotKey(), dbxValueReader.toParrotValue());
            System.out.println("\n\n" + ParrotHelper.toAvroSchema(parrotData.key()) + "\n\n");
            parrotData.equals(parrotData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
