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
package io.parrot.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.deltaspike.core.api.config.ConfigResolver;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.config.IParrotConfigProperties;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import scala.collection.Iterator;

public class KafkaUtils {

	private static ZkUtils zkUtils;
	private static ZkClient zkClient;

	/**
	 * Get all Kafka CDC topics for all databases in the source DB list
	 * 
	 * @param processors
	 *            A list of Parrot Processors.
	 * @return A list of Kafka topics.
	 */
	public static List<String> getAllTopics(ParrotProcessorApi[] processors) {
		List<String> topics = new LinkedList<String>();
		for (ParrotProcessorApi processor : processors) {
			topics.addAll(getAllTopics(processor));
		}
		return topics;
	}

	public static void createTopic(String topicName, int numPartitions, int replicationFactor) {
		AdminUtils.createTopic(getZkUtils(), topicName, numPartitions, replicationFactor,
				AdminUtils.createTopic$default$5(), AdminUtils.createTopic$default$6());
	}

	/**
	 * @param processor
	 *            A Parrot Processor.
	 * @return A list of Kafka topics.
	 */
	public static List<String> getAllTopics(ParrotProcessorApi processor) {
		List<String> topics = new ArrayList<String>();
		try {
			scala.collection.immutable.List<String> allTopics = getZkUtils().getAllTopics().toList();
			Iterator<String> i = allTopics.iterator();
			while (i.hasNext()) {
				String topic = (String) i.next();
				if (topic.startsWith(processor.getSource().getLogicalName() + ".")) {
					topics.add(topic);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return topics;
	}

	private static ZkUtils getZkUtils() {

		if (zkUtils == null || zkUtils.zkConnection() == null || zkUtils.zkConnection().getZookeeperState() == null
				|| !zkUtils.zkConnection().getZookeeperState().isConnected()
				|| !zkUtils.zkConnection().getZookeeperState().isAlive()) {
			String zookeeperHosts = ConfigResolver.getPropertyValue(IParrotConfigProperties.P_ZOOKEEPER_HOSTS);
			ZkConnection zkConnection = new ZkConnection(zookeeperHosts);
			zkUtils = new ZkUtils(getZkClient(), zkConnection, false);
		}
		return zkUtils;
	}

	private static ZkClient getZkClient() {
		if (zkClient == null) {
			String zookeeperHosts = ConfigResolver.getPropertyValue(IParrotConfigProperties.P_ZOOKEEPER_HOSTS);
			int sessionTimeoutMs = 10 * 1000;
			int connectionTimeoutMs = 20 * 1000;
			zkClient = new ZkClient(zookeeperHosts, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);
		}
		return zkClient;
	}

}
