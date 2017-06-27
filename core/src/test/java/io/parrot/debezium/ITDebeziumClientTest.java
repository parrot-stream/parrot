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
package io.parrot.debezium;

//import static io.parrot.utils.JsonUtils.jsonToObject;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.cdi.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.parrot.ParrotBaseTest;
import io.parrot.debezium.connectors.Connector;
import io.parrot.debezium.connectors.ConnectorStateType;
import io.parrot.debezium.connectors.mongodb.MongoDbConnector;
import io.parrot.debezium.connectors.mysql.MySqlConnector;
import io.parrot.debezium.connectors.postgresql.PostgreSqlConnector;
import io.parrot.exception.ParrotApiException;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotLogFormatter;

@RunWith(CamelCdiRunner.class)
public class ITDebeziumClientTest extends ParrotBaseTest {

	@Inject
	Logger LOG;

	@Inject
	DebeziumApiClient dbzClient;

	/*****************************************************************************************************
	 * PostgreSql Connector
	 *****************************************************************************************************/
	// @Ignore
	@Test
	@Order(1)
	public void testAddNewPostgreSqlConnector() {
		String json = JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL);

		PostgreSqlConnector connector = (PostgreSqlConnector) dbzClient
				.addConnector(JsonUtils.jsonToObject(json, PostgreSqlConnector.class));

		List<String> connectors = dbzClient.listConnectors();
		assertEquals(1, connectors.size());

		// LOG.info(ParrotLogFormatter.formatLog("PostgreSql Connector",
		// JsonUtils.objectToJson(connector, true)));
	}

	// @Ignore
	@Test(expected = ParrotApiException.class)
	@Order(2)
	public void testAddExistentPostgreSqlConnector() {
		PostgreSqlConnector connector = (PostgreSqlConnector) dbzClient.addConnector(
				JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL), PostgreSqlConnector.class));
	}

	// @Ignore
	@Test
	@Order(3)
	public void testGetPostgreSqlConnectorStatus() {
		String connectorName = JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).getName();
		Connector connector = dbzClient.getConnectorStatus(connectorName);
		List<String> connectors = dbzClient.listConnectors();
		assertEquals(1, connectors.size());
		assertEquals("RUNNING", connector.getStatus().getState());
	}

	// @Ignore
/*	@Test
	@Order(4)
	public void testRestartPostgreSqlConnectorStatus() throws InterruptedException {
		String connectorName = jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).name;
		dbxClient.restartConnector(connectorName);
		Connector connector = dbxClient.getConnectorStatus(connectorName);
		while (!"RUNNING".equalsIgnoreCase(connector.status.state)) {
			Thread.sleep(1000);
			connector = dbxClient.getConnectorStatus(connectorName);
		}
		assertEquals("RUNNING", connector.status.state);
	}*/

	// @Ignore
	@Test
	@Order(5)
	public void testPausePostgreSqlConnectorStatus() throws InterruptedException {
		String connectorName = JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).getName();
		dbzClient.pauseConnector(connectorName);
		Connector connector = dbzClient.getConnectorStatus(connectorName);
		while (!ConnectorStateType.PAUSED.equals(connector.getStatus().getState())) {
			Thread.sleep(1000);
			connector = dbzClient.getConnectorStatus(connectorName);
		}
		assertEquals(ConnectorStateType.PAUSED, connector.getStatus().getState());
	}

	// @Ignore
	@Test
	@Order(6)
	public void testResumePostgreSqlConnectorStatus() throws InterruptedException {
		String connectorName = JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).getName();
		dbzClient.resumeConnector(connectorName);

		Connector connector = dbzClient.getConnectorStatus(connectorName);
		while (!ConnectorStateType.PAUSED.equals(connector.getStatus().getState())) {
			Thread.sleep(1000);
			connector = dbzClient.getConnectorStatus(connectorName);
		}
		assertEquals(ConnectorStateType.RUNNING, connector.getStatus().getState());
	}

	// @Ignore
	@Test
	@Order(7)
	public void testDeleteExistentPostgreSqlConnector() {
		String connectorName = JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).getName();
		dbzClient.deleteConnector(connectorName);

		List<String> connectors = dbzClient.listConnectors();
		assertEquals(0, connectors.size());
	}

	// @Ignore
	@Test(expected = ParrotApiException.class)
	@Order(8)
	public void testDeleteNotExistentPostgreSqlConnector() {
		String connectorName = JsonUtils.jsonToObject(JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL),
				PostgreSqlConnector.class).getName();
		dbzClient.deleteConnector(connectorName);
	}

	/*****************************************************************************************************
	 * MongoDb Connector
	 *****************************************************************************************************/
	@Ignore
	@Test
	@Order(5)
	public void testAddMongoDbConnector() {
		String json = JsonUtils.readFromFile(JSON_PATH_CONNECTOR_MONGODB);
		LOG.info(ParrotLogFormatter.formatLog("MongoDb Connector", json));
		MongoDbConnector connector = (MongoDbConnector) dbzClient
				.addConnector(JsonUtils.jsonToObject(json, MongoDbConnector.class));
	}

	/*****************************************************************************************************
	 * MySql Connector
	 *****************************************************************************************************/
	@Ignore
	@Test
	@Order(0)
	public void testAddMySqlConnector() {
		String json = JsonUtils.readFromFile(JSON_PATH_CONNECTOR_MYSQL);
		LOG.info(ParrotLogFormatter.formatLog("MySql Connector", json));
		MySqlConnector connector = dbzClient.addConnector(JsonUtils.jsonToObject(json, MySqlConnector.class));
	}

	/*****************************************************************************************************
	 * Oracle Connector
	 *****************************************************************************************************/
	@Ignore
	@Test
	@Order(0)
	public void testAddOracleConnector() {
		String json = JsonUtils.readFromFile(JSON_PATH_CONNECTOR_ORACLE);
		LOG.info(ParrotLogFormatter.formatLog("MySql Connector", json));
		MySqlConnector connector = dbzClient.addConnector(JsonUtils.jsonToObject(json, MySqlConnector.class));
	}
}
