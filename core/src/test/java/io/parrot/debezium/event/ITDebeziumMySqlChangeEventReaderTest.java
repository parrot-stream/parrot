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

import java.util.HashMap;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.kafka.connect.data.Schema.Type;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.json.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.parrot.data.reader.ChangeEventKeyReader;
import io.parrot.utils.JsonUtils;

@RunWith(CamelCdiRunner.class)
public class ITDebeziumMySqlChangeEventReaderTest {

    final static String JSON_PATH_EVENT_KEY_MYSQL = "json/mysql/MySqlDataChangeEventKey.json";
    final static String JSON_PATH_EVENT_VALUE_CREATE_MYSQL = "json/mysql/MySqlDataChangeEventValueCreate.json";
    final static String JSON_PATH_EVENT_VALUE_UPDATE_MYSQL = "json/mysql/MySqlDataChangeEventValueUpdate.json";
    final static String JSON_PATH_EVENT_VALUE_DELETE_MYSQL = "json/mysql/MySqlDataChangeEventValueDelete.json";

    @Inject
    Logger LOG;

    JsonConverter jsonConverter;

    ITDebeziumMySqlChangeEventReaderTest() {
        jsonConverter = new JsonConverter();
        jsonConverter.configure(new HashMap<String, Object>(), false);
    }

    // @Ignore
    @Test
    public void testDeserializeDebeziumMySqlDataChangeEventKey() {
        try {
            ChangeEventKeyReader dbxKeyReader = new ChangeEventKeyReader(readChangeEvent(JSON_PATH_EVENT_KEY_MYSQL));
            assertEquals(1, dbxKeyReader.getSchemaFields().size());
            assertEquals(Type.INT32, dbxKeyReader.getFieldType("id"));
            assertFalse(dbxKeyReader.isOptional("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * @Ignore
     * 
     * @Test(expected = ParrotException.class) public void
     * testIndexOutOfBoundsException() { MySqlKeyReader dbxKeyReader = new
     * MySqlKeyReader(readChangeEvent(JSON_PATH_EVENT_KEY_MYSQL)); //
     * dbxKeyReader.getKeyColumnName(1); }
     * 
     * // @Ignore
     * 
     * @Test public void
     * testDeserializeDebeziumMySqlDataChangeEventValueCreate() {
     * MySqlValueReader dbxValueReader = new
     * MySqlValueReader(readChangeEvent(JSON_PATH_EVENT_VALUE_CREATE_MYSQL));
     * assertEquals(1, dbxValueReader.getVersion()); assertEquals(6,
     * dbxValueReader.getSchemaFields(ChangeEventValueFieldType.SOURCE.toString(
     * )).size());
     * 
     * assertEquals(ChangeEventType.CREATE,
     * ChangeEventType.valueFrom(dbxValueReader.getPayloadData().getOp()));
     * 
     * assertNull(dbxValueReader.getPayloadData().getBefore());
     * assertNotNull(dbxValueReader.getPayloadData().getAfter());
     * 
     * assertEquals("mysql-bin.000003",
     * dbxValueReader.getPayloadData().source.file); assertEquals(new Long(0),
     * dbxValueReader.getPayloadData().source.tsSec);
     * assertEquals("mysql-server-1",
     * dbxValueReader.getPayloadData().source.name); assertEquals(new Long(0),
     * dbxValueReader.getPayloadData().source.serverId); assertEquals(new
     * Long(154), dbxValueReader.getPayloadData().source.pos);
     * assertEquals(Integer.valueOf(0),
     * dbxValueReader.getPayloadData().source.row);
     * assertNull(dbxValueReader.getPayloadData().source.gtid);
     * assertTrue(dbxValueReader.getPayloadData().source.snapshot); }
     * 
     * // @Ignore
     * 
     * @Test public void
     * testDeserializeDebeziumMySqlDataChangeEventValueUpdate() {
     * MySqlValueReader dbxValueReader = new
     * MySqlValueReader(readChangeEvent(JSON_PATH_EVENT_VALUE_UPDATE_MYSQL));
     * assertEquals(1, dbxValueReader.getVersion()); assertEquals(6,
     * dbxValueReader.getSchemaFields(ChangeEventValueFieldType.SOURCE.toString(
     * )).size());
     * 
     * assertEquals(ChangeEventType.UPDATE,
     * ChangeEventType.valueFrom(dbxValueReader.getPayloadData().getOp()));
     * 
     * assertNotNull(dbxValueReader.getPayloadData().getBefore());
     * assertNotNull(dbxValueReader.getPayloadData().getAfter());
     * 
     * assertEquals("mysql-bin.000003",
     * dbxValueReader.getPayloadData().source.file); assertEquals(new
     * Long(1465581), dbxValueReader.getPayloadData().source.tsSec);
     * assertEquals("mysql-server-1",
     * dbxValueReader.getPayloadData().source.name); assertEquals(new
     * Long(223344), dbxValueReader.getPayloadData().source.serverId);
     * assertEquals(new Long(484), dbxValueReader.getPayloadData().source.pos);
     * assertEquals(Integer.valueOf(0),
     * dbxValueReader.getPayloadData().source.row);
     * assertNull(dbxValueReader.getPayloadData().source.gtid);
     * assertNull(dbxValueReader.getPayloadData().source.snapshot); }
     * 
     * // @Ignore
     * 
     * @Test public void
     * testDeserializeDebeziumMySqlDataChangeEventValueDelete() {
     * MySqlValueReader dbxValueReader = new
     * MySqlValueReader(readChangeEvent(JSON_PATH_EVENT_VALUE_DELETE_MYSQL));
     * assertEquals(1, dbxValueReader.getVersion()); assertEquals(6,
     * dbxValueReader.getSchemaFields(ChangeEventValueFieldType.SOURCE.toString(
     * )).size());
     * 
     * assertEquals(ChangeEventType.DELETE,
     * ChangeEventType.valueFrom(dbxValueReader.getPayloadData().getOp()));
     * 
     * assertNotNull(dbxValueReader.getPayloadData().getBefore());
     * assertNull(dbxValueReader.getPayloadData().getAfter());
     * 
     * assertEquals("mysql-bin.000003",
     * dbxValueReader.getPayloadData().source.file); assertEquals(new
     * Long(1465581), dbxValueReader.getPayloadData().source.tsSec);
     * assertEquals("mysql-server-1",
     * dbxValueReader.getPayloadData().source.name); assertEquals(new
     * Long(223344), dbxValueReader.getPayloadData().source.serverId);
     * assertEquals(new Long(805), dbxValueReader.getPayloadData().source.pos);
     * assertEquals(Integer.valueOf(0),
     * dbxValueReader.getPayloadData().source.row);
     * assertNull(dbxValueReader.getPayloadData().source.gtid);
     * assertNull(dbxValueReader.getPayloadData().source.snapshot); }
     */

    SchemaAndValue readChangeEvent(String jsonPath) {
        String json = JsonUtils.readFromFile(jsonPath);
        return jsonConverter.toConnectData("", json.getBytes());
    }
}