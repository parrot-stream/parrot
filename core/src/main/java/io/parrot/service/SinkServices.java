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
package io.parrot.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.parrot.api.model.ParrotSinkApi;
import io.parrot.exception.GenericApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.zookeeper.ParrotZkClient;

@Stateless
public class SinkServices {

	@Inject
	Logger LOG;

	@Inject
	ParrotZkClient zkClient;

	/**
	 * @param pSink
	 *            A Sink to add
	 * @return The created Sink
	 * @throws GenericApiException
	 */
	public ParrotSinkApi addSink(ParrotSinkApi pSink) throws ParrotApiException {
		LOG.info("ADDING SINK: " + pSink.getId());

		return pSink;
	}

	/**
	 * @param pSink
	 *            pId A Sink ID to delete
	 */
	public void deleteSink(String pId) throws ParrotApiException {
		LOG.info("DELETING SINK: " + pId);

	}
}
