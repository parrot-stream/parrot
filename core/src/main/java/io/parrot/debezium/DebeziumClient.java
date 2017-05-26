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

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;

import io.parrot.config.IParrotConfigProperties;
import io.parrot.debezium.connectors.Connector;
import io.parrot.debezium.connectors.Error;
import io.parrot.debezium.connectors.mongodb.MongoDbConnector;
import io.parrot.debezium.connectors.mysql.MySqlConnector;
import io.parrot.debezium.connectors.postgresql.PostgreSqlConnector;
import io.parrot.exception.ParrotException;
import io.parrot.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@ApplicationScoped
public class DebeziumClient {

	@Inject
	ApplicationMessages message;

	@Inject
	Logger LOG;

	@Inject
	@ConfigProperty(name = IParrotConfigProperties.P_DEBEZIUM_API_URL)
	String debeziumApiUrl;

	DebeziumService dbzService;

	public List<String> listConnectors() {
		try {
			return getDebeziumService().listConnectors().execute().body();
		} catch (IOException e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public ResponseBody deleteConnector(String connectorName) {
		try {
			Response<ResponseBody> response = getDebeziumService().deleteConnector(connectorName).execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorCreationError(connectorName, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public ResponseBody restartConnector(String connectorName) {
		try {
			Response<ResponseBody> response = getDebeziumService().restartConnector(connectorName).execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorRestartError(connectorName, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public ResponseBody pauseConnector(String connectorName) {
		try {
			Response<ResponseBody> response = getDebeziumService().pauseConnector(connectorName).execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorPauseError(connectorName, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public ResponseBody resumeConnector(String connectorName) {
		try {
			Response<ResponseBody> response = getDebeziumService().resumeConnector(connectorName).execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorResumeError(connectorName, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public MySqlConnector addConnector(MySqlConnector connector) {
		try {
			Response<MySqlConnector> response = getDebeziumService().addConnector(connector).execute();
			return (MySqlConnector) addConnector(response, connector);
		} catch (IOException e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public MongoDbConnector addConnector(MongoDbConnector connector) {
		try {
			Response<MongoDbConnector> response = getDebeziumService().addConnector(connector).execute();
			return (MongoDbConnector) addConnector(response, connector);
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public PostgreSqlConnector addConnector(PostgreSqlConnector connector) {
		try {
			Response<PostgreSqlConnector> response = getDebeziumService().addConnector(connector).execute();
			return (PostgreSqlConnector) addConnector(response, connector);
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	public Connector getConnectorStatus(String connectorName) {
		try {
			Response<Connector> response = getDebeziumService().getConnectorStatus(connectorName).execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorCreationError(connectorName, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	Connector addConnector(Response response, Connector connector) {
		try {
			if (response.isSuccessful()) {
				return (Connector) response.body();
			} else {
				Error error = JsonUtils.byteArrayToObject(response.errorBody().bytes(), Error.class);
				throw new ParrotException(message.debeziumConnectorCreationError(connector.name, error.message));
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
	}

	DebeziumService getDebeziumService() {
		try {
			if (dbzService == null) {
				HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
				logging.setLevel(Level.BODY);
				OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
				httpClient.addInterceptor(logging);
				Retrofit retrofit = new Retrofit.Builder().baseUrl(debeziumApiUrl).client(httpClient.build())
						.addConverterFactory(JacksonConverterFactory.create()).build();
				dbzService = retrofit.create(DebeziumService.class);
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
		return dbzService;
	}
}
