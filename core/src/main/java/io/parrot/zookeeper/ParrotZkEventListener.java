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
package io.parrot.zookeeper;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.config.IParrotConfigProperties;
import io.parrot.context.ParrotContext;
import io.parrot.processor.ProcessorManager;
import io.parrot.utils.ParrotLogFormatter;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;
import io.parrot.zookeeper.path.ParrotProcessorNodePath;

@Dependent
public class ParrotZkEventListener implements CuratorListener {

	@Inject
	Logger LOG;

	@Inject
	ParrotZkClient zkClient;

	@Inject
	ParrotContext ctx;

	@Inject
	ProcessorManager processorManager;

	@Inject
	@ConfigProperty(name = IParrotConfigProperties.P_PARROT_NODE)
	String parrotNode;

	/**
	 * Manages the following ZooKeeper events:
	 * 
	 * <ul>
	 * <li><b>CREATE</b>: creates a new Processor Camel route and starts it</li>
	 * <li><b>SET_DATA</b>: stops the Processor Camel route, recreates it with
	 * the updated configuration and starts it</li>
	 * <li><b>DELETE</b>: stops the Processor Camel route and deletes it</li>
	 * </ul>
	 * 
	 * filtering on the <b>/parrot/processor/config</b> path.
	 * 
	 * This is useful to s and remove Came route in case of a DELETE event, or
	 * to create and start a new Camel route in case of a CREATE event.
	 */
	@Override
	public void eventReceived(CuratorFramework pFramework, CuratorEvent pEvent) throws Exception {

		String path = pEvent.getPath();
		CuratorEventType type = pEvent.getType();
		LOG.info(ParrotLogFormatter.formatLog(
				"ZooKeeper Event Type: " + type + "\nZooKeeper Path: " + (path != null ? path : "ND"), "Name",
				pEvent.getName(), "Data", pEvent.getData() != null ? new String(pEvent.getData(), "UTF-8") : null));
		ParrotProcessorApi processorApi = null;
		switch (type) {
		case DELETE:
			String[] tokens = path.split("/");
			if (path.startsWith(ParrotProcessorConfigPath.ZK_PATH)) {
				String id = tokens[tokens.length - 1];
				ParrotProcessorNodeApi processorNode = zkClient.getProcessorNode(id, parrotNode)
						.getProcessorNodeApi();

				processorManager.deleteZkProcessorNode(processorNode);
			} else if (path.startsWith(ParrotProcessorNodePath.ZK_PATH)) {
				ParrotProcessorNodeApi processorNode = zkClient.getProcessorNodeByPath(path)
						.getProcessorNodeApi();
				processorManager.stopProcessorNode(processorNode);
				processorManager.deleteProcessorNode(processorNode);
			}
			break;
		case CREATE:
			/**
			 * If the CREATE event is on the path of the Parrot Processor's
			 * Configuration
			 */
			if (path.startsWith(ParrotProcessorConfigPath.ZK_PATH)) {
				processorApi = zkClient.getProcessorByPath(pEvent.getPath()).getProcessorApi();
				processorManager.addProcessorNode(processorApi, parrotNode);
			}
			/**
			 * else if the CREATE event is on the path of a Parrot Processor's
			 * Node
			 */
			break;
		case SET_DATA:
			/**
			 * If the SET_DATA event is on the path of the Parrot Processor's
			 * Configuration
			 */
			if (path.startsWith(ParrotProcessorConfigPath.ZK_PATH)) {
				processorApi = zkClient.getProcessorByPath(pEvent.getPath()).getProcessorApi();
				ParrotProcessorNodeApi processorNode = zkClient.getProcessorNode(processorApi.getId(), parrotNode)
						.getProcessorNodeApi();
				processorManager.stopProcessorNode(processorNode);
				processorManager.deleteProcessorNode(processorNode);
				processorManager.addProcessorNode(processorApi, parrotNode);
			}
			/**
			 * else if the SET_DATA event is on the path of a Parrot Processor's
			 * Node
			 */
			else if (path.startsWith(ParrotProcessorNodePath.ZK_PATH)) {
				ParrotProcessorNodeApi processorNode = zkClient.getProcessorNodeByPath(pEvent.getPath())
						.getProcessorNodeApi();
				processorManager.startProcessorNode(processorNode);
			}
			break;
		default:
			break;
		}
	}

}
