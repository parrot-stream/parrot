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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.zookeeper.CreateMode;

import io.parrot.api.model.ParrotNodeApi;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorStatusApi;
import io.parrot.config.IParrotConfigProperties;
import io.parrot.exception.ParrotException;
import io.parrot.exception.ParrotZkException;
import io.parrot.utils.JsonUtils;
import io.parrot.zookeeper.path.AboutPath;
import io.parrot.zookeeper.path.ParrotProcessorClusterPath;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

@Default
@ApplicationScoped
public class ParrotZkClientImpl implements ParrotZkClient {

	@Inject
	ParrotZkEventListener parrotZkEventListener;

	CuratorFramework curatorFramework;
	PathChildrenCache pathChildrenCache;

	@Override
	public AboutPath createAbout(AboutPath pAbout) throws ParrotZkException {
		AboutPath about = pAbout;
		try {
			if (getFramework().checkExists().forPath(AboutPath.ZK_PATH) == null) {
				about.setPath(curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
						.forPath(AboutPath.ZK_PATH, JsonUtils.objectToJson(pAbout).getBytes()));
			} else {
				about = getAbout();
			}
		} catch (Exception t) {
			throw new ParrotZkException(t);
		}
		return about;
	}

	@Override
	public ParrotProcessorConfigPath addProcessor(ParrotProcessorApi pProcessor) throws ParrotZkException {
		ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(
				ParrotProcessorConfigPath.ZK_PATH + "/" + pProcessor.getId(), pProcessor);
		try {
			getFramework().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground()
					.forPath(processorConfigPath.getPath(), JsonUtils.objectToJson(pProcessor).getBytes());
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return processorConfigPath;
	}

	@Override
	public void deleteProcessor(String id) {
		try {
			getFramework().delete().inBackground().forPath(ParrotProcessorConfigPath.ZK_PATH + "/" + id);
		} catch (Throwable t) {
			throw new ParrotException(t);
		}
	}

	@Override
	public ParrotProcessorClusterPath addProcessorStatusInCluster(ParrotNodeApi pParrotNodeApi,
			ParrotProcessorStatusApi pProcessorStatus) throws ParrotZkException {
		ParrotProcessorClusterPath processorClusterPath = new ParrotProcessorClusterPath(
				ParrotProcessorClusterPath.ZK_PATH + "/" + pProcessorStatus.getId() + "/" + pParrotNodeApi.getId(),
				pProcessorStatus);
		try {
			getFramework().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
					.forPath(processorClusterPath.getPath(), JsonUtils.objectToJson(pProcessorStatus).getBytes());
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return processorClusterPath;
	}

	@Override
	public ParrotProcessorConfigPath updateProcessor(ParrotProcessorApi pProcessor) throws ParrotZkException {
		ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(
				ParrotProcessorConfigPath.ZK_PATH + "/" + pProcessor.getId(), pProcessor);
		try {
			getFramework().setData().inBackground().forPath(processorConfigPath.getPath(),
					JsonUtils.objectToJson(pProcessor).getBytes());
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return processorConfigPath;
	}

	@Override
	public AboutPath updateAbout(AboutPath pAbout) throws ParrotZkException {
		AboutPath about = pAbout;
		try {
			getFramework().setData().forPath(AboutPath.ZK_PATH, JsonUtils.objectToJson(pAbout).getBytes());
			about = getAbout();
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return about;
	}

	@Override
	public void deleteAbout() throws ParrotZkException {
		try {
			getFramework().delete().forPath(AboutPath.ZK_PATH);
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
	}

	@Override
	public boolean exists(String pPath) throws ParrotZkException {
		try {
			return getFramework().checkExists().forPath(pPath) != null;
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
	}

	@Override
	public ParrotProcessorConfigPath getProcessor(String id) throws ParrotZkException {
		ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(
				ParrotProcessorConfigPath.ZK_PATH + "/" + id);
		try {
			ParrotProcessorApi processor = JsonUtils.byteArrayToObject(
					getFramework().getData().forPath(processorConfigPath.getPath()), ParrotProcessorApi.class);
			processorConfigPath.setProcessorApi(processor);
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return processorConfigPath;
	}

	@Override
	public List<ParrotProcessorConfigPath> getProcessors() throws ParrotZkException {
		List<ParrotProcessorConfigPath> processorsPath = new ArrayList<ParrotProcessorConfigPath>();
		try {
			if (getFramework().checkExists().forPath(ParrotProcessorConfigPath.ZK_PATH) != null) {
				List<String> processorsIDs = getFramework().getChildren().forPath(ParrotProcessorConfigPath.ZK_PATH);
				if (processorsIDs != null) {
					for (String id : processorsIDs) {
						processorsPath.add(getProcessor(id));
					}
				}
			}
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return processorsPath;
	}

	@Override
	public AboutPath getAbout() throws ParrotZkException {
		AboutPath aboutPath = null;
		try {
			aboutPath = JsonUtils.byteArrayToObject(getFramework().getData().forPath(AboutPath.ZK_PATH),
					AboutPath.class);
			aboutPath.setPath(AboutPath.ZK_PATH);
		} catch (Throwable t) {
			throw new ParrotZkException(t);
		}
		return aboutPath;
	}

	CuratorFramework getFramework() {
		String zookeeperHosts = ConfigResolver.getPropertyValue(IParrotConfigProperties.P_ZOOKEEPER_HOSTS);
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(10000, 3);
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
				.namespace(ParrotZkClient.ZK_NAMESPACE).connectString(zookeeperHosts).retryPolicy(retryPolicy);
		try {
			if (curatorFramework == null) {
				curatorFramework = builder.build();
				curatorFramework.getCuratorListenable().addListener(parrotZkEventListener);
				curatorFramework.start();
			}
			return curatorFramework;
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}
	}

}
