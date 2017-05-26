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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.exception.GenericApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.exception.ParrotZkException;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotLogFormatter;
import io.parrot.zookeeper.ParrotZkClient;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

@Stateless
public class ProcessorServices {

	@Inject
	Logger LOG;

	@Inject
	ParrotZkClient zkClient;

	/**
	 * @param pId
	 *            A Processor ID
	 * @return The Processor
	 * @throws ParrotApiException
	 */
	public ParrotProcessorApi getProcessor(String pId) throws ParrotApiException {
		LOG.info("GETTING PROCESSOR: " + pId, "");
		ParrotProcessorApi processor;
		try {
			processor = zkClient.getProcessor(pId).getProcessorApi();
		} catch (ParrotZkException e) {
			throw new GenericApiException("Unable to get the Processor '" + pId + "': " + e.getMessage());
		}
		return processor;
	}

	/**
	 * @return A Processor's list
	 * @throws ParrotApiException
	 */
	public List<ParrotProcessorApi> getProcessors() throws ParrotApiException {
		try {
			List<ParrotProcessorApi> processors = new ArrayList<ParrotProcessorApi>();
			List<ParrotProcessorConfigPath> processorPaths = zkClient.getProcessors();
			for (ParrotProcessorConfigPath processorPath : processorPaths) {
				processors.add(processorPath.getProcessorApi());
			}
			return processors;
		} catch (ParrotZkException e) {
			throw new GenericApiException("Unable to get the list of Parrot Processors: " + e.getMessage());
		}
	}

	/**
	 * @param pProcessor
	 *            A Processor to add
	 * @return The created Processor
	 * @throws ParrotApiException
	 */
	public ParrotProcessorApi addProcessor(ParrotProcessorApi pProcessor) throws ParrotApiException {
		LOG.info(ParrotLogFormatter.formatLog("ADDING PROCESSOR: " + pProcessor.getId(),
				JsonUtils.objectToJson(pProcessor, true)));
		try {
			zkClient.addProcessor(pProcessor);
		} catch (ParrotZkException e) {
			throw new GenericApiException(
					"Unable to create the Parrot Processor '" + pProcessor.getId() + "': " + e.getMessage());
		}
		return pProcessor;
	}

	/**
	 * @param pId
	 *            A Processor ID to delete
	 * @throws ParrotApiException
	 */
	public void deleteProcessor(String pId) throws ParrotApiException {
		LOG.info("DELETING PROCESSOR: " + pId, "");
		try {
			zkClient.deleteProcessor(pId);
		} catch (ParrotZkException e) {
			throw new GenericApiException("Unable to delete the Parrot Processor '" + pId + "': " + e.getMessage());
		}
	}

	/**
	 * @param pId
	 *            A Processor ID to update
	 * @return The updated Processor
	 * @throws ParrotApiException
	 */
	public ParrotProcessorApi updateProcessor(ParrotProcessorApi pProcessor) throws ParrotApiException {
		LOG.info("UPDATING PROCESSOR: " + pProcessor.getId(), "");
		try {
			return zkClient.updateProcessor(pProcessor).getProcessorApi();
		} catch (ParrotZkException e) {
			throw new GenericApiException(
					"Unable to update the Parrot Processor '" + pProcessor.getId() + "': " + e.getMessage());
		}
	}
}
