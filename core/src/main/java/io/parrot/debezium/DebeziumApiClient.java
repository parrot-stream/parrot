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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;

import io.parrot.config.IParrotConfigProperties;
import io.parrot.debezium.connectors.Connector;
import io.parrot.debezium.connectors.mongodb.MongoDbConnector;
import io.parrot.debezium.connectors.mysql.MySqlConnector;
import io.parrot.debezium.connectors.postgresql.PostgreSqlConnector;
import io.parrot.exception.ParrotException;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@ApplicationScoped
public class DebeziumApiClient {

	@Inject
	Logger LOG;

	@Inject
	@ConfigProperty(name = IParrotConfigProperties.P_DEBEZIUM_API_URL)
	String debeziumApiUrl;

	DebeziumApiService dbzService;

	public List<String> listConnectors() {
		return ParrotHelper.execute(getDebeziumService().listConnectors());
	}

	public PostgreSqlConnector getPostgreSqlConnector(String pConnectorName) {
		return ParrotHelper.execute(getDebeziumService().getPostgreSqlConnector(pConnectorName));
	}

	public void deleteConnector(String pConnectorName) {
		ParrotHelper.execute(getDebeziumService().deleteConnector(pConnectorName));
	}

	public void restartConnector(String pConnectorName) {
		ParrotHelper.execute(getDebeziumService().restartConnector(pConnectorName));
	}

	public void pauseConnector(String pConnectorName) {
		ParrotHelper.execute(getDebeziumService().pauseConnector(pConnectorName));
	}

	public void resumeConnector(String pConnectorName) {
		ParrotHelper.execute(getDebeziumService().resumeConnector(pConnectorName));
	}

	public MySqlConnector addConnector(MySqlConnector pConnector) {
		return ParrotHelper.execute(getDebeziumService().addConnector(pConnector));
	}

	public MongoDbConnector addConnector(MongoDbConnector pConnector) {
		return ParrotHelper.execute(getDebeziumService().addConnector(pConnector));
	}

	public PostgreSqlConnector addConnector(PostgreSqlConnector pConnector) {
		return ParrotHelper.execute(getDebeziumService().addConnector(pConnector));
	}

	public Connector getConnectorStatus(String pConnectorName) {
		return ParrotHelper.execute(getDebeziumService().getConnectorStatus(pConnectorName));
	}

	/**
	 * Private
	 * 
	 * @return A Parrot service instance
	 */
	DebeziumApiService getDebeziumService() {
		try {
			if (dbzService == null) {
				HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
				logging.setLevel(Level.HEADERS);
				OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
				httpClient.addInterceptor(logging);
				Retrofit retrofit = new Retrofit.Builder().baseUrl(debeziumApiUrl).client(httpClient.build())
						.addConverterFactory(JacksonConverterFactory.create(JsonUtils.getDefaultMapper())).build();
				dbzService = retrofit.create(DebeziumApiService.class);
			}
		} catch (Exception e) {
			throw new ParrotException(e.getMessage(), e);
		}
		return dbzService;
	}
}
