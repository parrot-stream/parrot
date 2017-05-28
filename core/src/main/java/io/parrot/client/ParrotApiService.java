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
package io.parrot.client;

import java.util.List;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.exception.ParrotApiException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface ParrotApiService {

	/**
	 * Get the list of Processors
	 * 
	 * @return The list of Processors
	 */
	@Headers("Content-Type: application/json")
	@GET("processors")
	Call<List<String>> getProcessors();

	/**
	 * @param pId
	 *            A Processor ID
	 * @return The Processor
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@GET("processors/{id}")
	Call<ParrotProcessorApi> getProcessor(@Path("id") String pId);

	/**
	 * @param pProcessor
	 *            A Processor to add
	 * @return The added Processor
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@POST("processors")
	Call<ParrotProcessorApi> addProcessor(@Body ParrotProcessorApi pProcessor);

	/**
	 * @param pId
	 *            A Processor ID
	 * @return The Processor cluster
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@GET("processors/{id}/cluster")
	Call<List<ParrotProcessorNodeApi>> getProcessorCluster(@Path("id") String pId);

	/**
	 * @param pId
	 *            A Processor ID to start
	 * @return The started Processor
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@POST("processors/{id}/start")
	Call<ParrotProcessorApi> startProcessor(@Path("id") String pId);

	/**
	 * @param pId
	 *            A Processor ID to stop
	 * @return The stopped Processor
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@POST("processors/{id}/stop")
	Call<ParrotProcessorApi> stopProcessor(@Path("id") String pId);

	/**
	 * @param pId
	 *            A Processor ID to restart
	 * @return The restarted Processor
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@POST("processors/{id}/restart")
	Call<ParrotProcessorApi> restartProcessor(@Path("id") String pId);

	/**
	 * @param pId
	 *            A Processor ID to delete
	 * @return
	 * @throws ParrotApiException
	 */
	@Headers("Content-Type: application/json")
	@DELETE("processors/{id}")
	Call<ResponseBody> deleteProcessor(@Path("id") String pId);

}
