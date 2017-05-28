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
package io.parrot.processor;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.api.model.ParrotProcessorNodeApi.StatusEnum;
import io.parrot.config.IParrotConfigProperties;
import io.parrot.context.ParrotContext;
import io.parrot.exception.GenericApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.exception.ParrotException;
import io.parrot.exception.ParrotZkException;
import io.parrot.zookeeper.ParrotZkClient;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

@ApplicationScoped
public class ProcessorManager {

	@Inject
	Logger LOG;

	@Inject
	ParrotZkClient zkClient;

	@Inject
	ParrotContext ctx;

	@Inject
	ParrotProcessorBuilder processorBuilder;


	@Inject
	@ConfigProperty(name = IParrotConfigProperties.P_PARROT_NODE)
	String parrotNode;

	public List<ParrotProcessor> startUpProcessors() throws ParrotZkException {
		List<ParrotProcessorConfigPath> processorPaths = null;
		try {
			processorPaths = zkClient.getProcessors();
		} catch (Exception e) {
			throw new ParrotException("Unable to get Parrot Processors: " + e.getMessage());
		}

		List<ParrotProcessor> processors = new ArrayList<ParrotProcessor>();
		for (ParrotProcessorConfigPath processor : processorPaths) {
			LOG.info("Creating processor " + processor.getProcessorApi().getId() + "...");
			processors.add(processorBuilder.build(processor.getProcessorApi()));
			updateProcessorNode(processor.getProcessorApi().getId(), parrotNode,
					StatusEnum.valueOf(processor.getProcessorApi().getStatus().name()));
		}
		return processors;
	}

	/**
	 * Following methods operates on ZooKeeper's Node configuration
	 */
	public void deleteZkProcessorNode(ParrotProcessorNodeApi pProcessorNode) {
		try {
			zkClient.deleteProcessorNode(pProcessorNode.getId(), pProcessorNode.getNode());
		} catch (ParrotApiException pe) {
			throw pe;
		} catch (Exception e) {
			throw new GenericApiException(e.getMessage());
		}
	}

	/**
	 * Following methods operates on Processor's Node route
	 */
	public ParrotProcessorNodeApi stopProcessorNode(ParrotProcessorNodeApi pProcessorNode) {
		LOG.info("Stopping Processor '" + pProcessorNode.getId() + "' on node '" + pProcessorNode.getNode() + "'");
		try {
			ctx.stopRoute(pProcessorNode.getId());
			return pProcessorNode;
		} catch (Exception e) {
			throw new ParrotException("Unable to stop Parrot Processor's route: " + e.getMessage());
		}
	}

	public ParrotProcessorNodeApi deleteProcessorNode(ParrotProcessorNodeApi pProcessorNode) {
		LOG.info("Deleting Processor '" + pProcessorNode.getId() + "' on node '" + pProcessorNode.getNode() + "'");
		try {
			ctx.removeRoute(pProcessorNode.getId());
			return pProcessorNode;
		} catch (Exception e) {
			throw new ParrotException("Unable to delete Parrot Processor's route: " + e.getMessage());
		}
	}

	public ParrotProcessorNodeApi startProcessorNode(ParrotProcessorNodeApi pProcessorNode) {

		try {
			if (StatusEnum.STARTED == pProcessorNode.getStatus()
					&& parrotNode.equalsIgnoreCase(pProcessorNode.getNode())) {
				LOG.info("Starting Processor '" + pProcessorNode.getId() + "' on node '" + pProcessorNode.getNode()
						+ "'");
				ctx.startRoute(pProcessorNode.getId());
			}
			return pProcessorNode;
		} catch (Exception e) {
			throw new ParrotException(
					"Unable to start Parrot Processor's node '" + pProcessorNode.getId() + "': " + e.getMessage());
		}
	}

	public ParrotProcessorNodeApi addProcessorNode(ParrotProcessorApi pProcessor, String pNode) {
		LOG.info("Adding Processor '" + pProcessor.getId() + "' on node '" + pNode + "'");
		try {
			if (parrotNode.equals(pNode)) {
				updateProcessorNode(pProcessor.getId(), pNode, StatusEnum.valueOf(pProcessor.getStatus().name()));
				ctx.addRoutes(processorBuilder.build(pProcessor).getParrotRouteBuilder());
			}
			return zkClient.getProcessorNode(pProcessor.getId(), pNode).getProcessorNodeApi();
		} catch (Exception e) {
			throw new ParrotException("Unable to add Parrot Processor's route: " + e.getMessage());
		}
	}

	/**
	 * Private
	 */
	ParrotProcessorNodeApi updateProcessorNode(String pId, String pNode, ParrotProcessorNodeApi.StatusEnum pStatus) {
		ParrotProcessorNodeApi processorNode = new ParrotProcessorNodeApi();
		processorNode.setId(pId);
		processorNode.setStatus(pStatus);
		processorNode.setNode(pNode);
		try {
			zkClient.updateProcessorNode(processorNode);
		} catch (ParrotApiException pe) {
			throw pe;
		} catch (Exception e) {
			throw new ParrotException("Unable to update Parrot Processor status in cluster: " + e.getMessage());
		}
		return processorNode;
	}
}
