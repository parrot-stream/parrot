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

import java.util.List;

import io.parrot.debezium.connectors.Connector;
import io.parrot.debezium.connectors.mongodb.MongoDbConnector;
import io.parrot.debezium.connectors.mysql.MySqlConnector;
import io.parrot.debezium.connectors.oracle.OracleDbConnector;
import io.parrot.debezium.connectors.postgresql.PostgreSqlConnector;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface DebeziumApiService {

	/**
	 * Get the list of Debezium connectors
	 * 
	 * @return The list of Debezium connectors
	 */
	@Headers("Content-Type: application/json")
	@GET("connectors")
	Call<List<String>> listConnectors();

	/**
	 * Creates a new PostgreSQL connector
	 * 
	 * @param connector
	 *            The PostgreSQL connector to create
	 * @return The newly created PostgreSQL connector
	 */
	@Headers("Content-Type: application/json")
	@POST("connectors")
	Call<PostgreSqlConnector> addConnector(@Body PostgreSqlConnector connector);

	/**
	 * Creates a new MySql connector
	 * 
	 * @param connector
	 *            The MySql connector to create
	 * @return The newly created MySql connector
	 */
	@Headers("Content-Type: application/json")
	@POST("connectors")
	Call<MySqlConnector> addConnector(@Body MySqlConnector connector);

	/**
	 * Creates a new Oracle connector
	 * 
	 * @param connector
	 *            The Oracle connector to create
	 * @return The newly created MySql connector
	 */
	@Headers("Content-Type: application/json")
	@POST("connectors")
	Call<OracleDbConnector> addConnector(@Body OracleDbConnector connector);

	/**
	 * Creates a new MongoDb connector
	 * 
	 * @param connector
	 *            The MongoDb connector to create
	 * @return The newly created MongoDb connector
	 */
	@Headers("Content-Type: application/json")
	@POST("connectors")
	Call<MongoDbConnector> addConnector(@Body MongoDbConnector connector);

	/**
	 * Deletes an existing connector
	 * 
	 * @param connectorName
	 *            The connector to delete
	 * @return
	 */
	@Headers("Content-Type: application/json")
	@DELETE("connectors/{connector}")
	Call<ResponseBody> deleteConnector(@Path("connector") String connectorName);

	/**
	 * Restarts an existing connector
	 * 
	 * @param connectorName
	 *            The connector to be restarted
	 * @return
	 */
	@Headers("Content-Type: application/json")
	@POST("connectors/{connector}/restart")
	Call<ResponseBody> restartConnector(@Path("connector") String connectorName);

	/**
	 * Pauses an existing connector
	 * 
	 * @param connectorName
	 *            The connector to be paused
	 * @return
	 */
	@Headers("Content-Type: application/json")
	@PUT("connectors/{connector}/pause")
	Call<ResponseBody> pauseConnector(@Path("connector") String connectorName);

	/**
	 * Resumes an existing connector
	 * 
	 * @param connectorName
	 *            The connector to be resumed
	 * @return
	 */
	@Headers("Content-Type: application/json")
	@PUT("connectors/{connector}/resume")
	Call<ResponseBody> resumeConnector(@Path("connector") String connectorName);

	/**
	 * Get the status of an existing connector
	 * 
	 * @param connectorName
	 *            The connector for which the status has to be retrieved
	 * @return The connector status
	 */
	@Headers("Content-Type: application/json")
	@GET("connectors/{connector}/status")
	Call<Connector> getConnectorStatus(@Path("connector") String connectorName);
}