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
import org.slf4j.Logger;

import io.parrot.utils.ParrotLogFormatter;

@Dependent
public class ParrotZkEventListener implements CuratorListener {

	@Inject
	Logger LOG;

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

		/**
		 * TODO: GESTIRE EVENTI
		 */
		if (pEvent != null) {
			CuratorEventType type = pEvent.getType();
			String path = pEvent.getPath();
			LOG.info(ParrotLogFormatter.formatLog(
					"Zookeper Event Type: " + type + "\nZookeeper Path: " + (path != null ? path : "ND"), "Name",
					pEvent.getName(), "Data", pEvent.getData() != null ? new String(pEvent.getData(), "UTF-8") : null));

			// LOG.info("\n\n" + event.getPath() + "\n\n");
		}
	}

}
