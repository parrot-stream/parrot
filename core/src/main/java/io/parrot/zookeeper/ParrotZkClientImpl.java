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
import javax.ws.rs.core.Response.Status;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.zookeeper.CreateMode;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.config.IParrotConfigProperties;
import io.parrot.exception.BadRequestApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.exception.ParrotException;
import io.parrot.exception.ParrotZkException;
import io.parrot.utils.JsonUtils;
import io.parrot.zookeeper.path.AboutPath;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;
import io.parrot.zookeeper.path.ParrotProcessorNodePath;

@Default
@ApplicationScoped
public class ParrotZkClientImpl implements ParrotZkClient {

    @Inject
    ParrotZkEventListener parrotZkEventListener;

    @Inject
    ApplicationMessages message;

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
    public ParrotProcessorConfigPath addProcessor(ParrotProcessorApi pProcessor)
            throws BadRequestApiException, ParrotZkException {
        ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(
                ParrotProcessorConfigPath.ZK_PATH + "/" + pProcessor.getId(), pProcessor);
        try {
            if (exists(processorConfigPath.getPath())) {
                throw new ParrotApiException(Status.CONFLICT,
                        message.parrotZooKeeperProcessorAlreadyExists(pProcessor.getId()));
            }
            getFramework().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).inBackground()
                    .forPath(processorConfigPath.getPath(), JsonUtils.objectToJson(pProcessor).getBytes());
        } catch (ParrotApiException pe) {
            throw pe;
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
    public ParrotProcessorNodePath addProcessorNode(ParrotProcessorNodeApi pProcessorNode) throws ParrotZkException {
        ParrotProcessorNodePath processorNodePath = new ParrotProcessorNodePath(
                ParrotProcessorNodePath.ZK_PATH + "/" + pProcessorNode.getId() + "/" + pProcessorNode.getNode(),
                pProcessorNode);
        try {
            if (exists(processorNodePath.getPath())) {
                updateProcessorNode(pProcessorNode);
            } else {
                getFramework().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground()
                        .forPath(processorNodePath.getPath(), JsonUtils.objectToJson(pProcessorNode).getBytes());
            }
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
        return processorNodePath;
    }

    @Override
    public ParrotProcessorNodePath updateProcessorNode(ParrotProcessorNodeApi pProcessorNode) throws ParrotZkException {
        ParrotProcessorNodePath processorNodePath = new ParrotProcessorNodePath(
                ParrotProcessorNodePath.ZK_PATH + "/" + pProcessorNode.getId() + "/" + pProcessorNode.getNode(),
                pProcessorNode);
        try {
            if (!exists(processorNodePath.getPath())) {
                addProcessorNode(pProcessorNode);
            } else {
                getFramework().setData().inBackground().forPath(processorNodePath.getPath(),
                        JsonUtils.objectToJson(pProcessorNode).getBytes());
            }
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
        return processorNodePath;
    }

    @Override
    public void deleteProcessorNode(String pId, String pNode) throws ParrotZkException {
        ParrotProcessorNodePath processorClusterPath = new ParrotProcessorNodePath(
                ParrotProcessorNodePath.ZK_PATH + "/" + pId + "/" + pNode);
        try {
            getFramework().delete().forPath(processorClusterPath.getPath());
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
    }

    @Override
    public List<ParrotProcessorNodePath> getProcessorCluster(String pId) throws ParrotZkException {
        List<ParrotProcessorNodePath> processorCluster = new ArrayList<ParrotProcessorNodePath>();
        try {
            if (exists(ParrotProcessorConfigPath.ZK_PATH + "/" + pId)) {
                List<String> nodes = getFramework().getChildren().forPath(ParrotProcessorNodePath.ZK_PATH + "/" + pId);
                if (nodes != null) {
                    for (String node : nodes) {
                        processorCluster.add(getProcessorNode(pId, node));
                    }
                }
            }
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
        return processorCluster;
    }

    @Override
    public ParrotProcessorNodePath getProcessorNode(String pId, String pNode) throws ParrotZkException {
        ParrotProcessorNodePath processorNodePath = new ParrotProcessorNodePath(
                ParrotProcessorNodePath.ZK_PATH + "/" + pId + "/" + pNode);
        try {
            if (exists(processorNodePath.getPath())) {
                ParrotProcessorNodeApi processorStatus = JsonUtils.byteArrayToObject(
                        getFramework().getData().forPath(processorNodePath.getPath()), ParrotProcessorNodeApi.class);
                processorNodePath.setProcessorNodeApi(processorStatus);
            } else {
                throw new ParrotApiException(Status.NOT_FOUND,
                        message.parrotZooKeeperProcessorNodeNotExists(pId, pNode));
            }
        } catch (ParrotApiException pae) {
            throw pae;
        } catch (ParrotZkException pe) {
            throw pe;
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
        return processorNodePath;
    }

    @Override
    public ParrotProcessorConfigPath updateProcessor(ParrotProcessorApi pProcessor)
            throws ParrotApiException, ParrotZkException {
        ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(
                ParrotProcessorConfigPath.ZK_PATH + "/" + pProcessor.getId(), pProcessor);
        try {
            if (exists(processorConfigPath.getPath())) {
                getFramework().setData().inBackground().forPath(processorConfigPath.getPath(),
                        JsonUtils.objectToJson(pProcessor).getBytes());
            } else {
                throw new ParrotApiException(Status.NOT_FOUND,
                        message.parrotZooKeeperProcessorNotExists(pProcessor.getId()));
            }
        } catch (ParrotApiException pae) {
            throw pae;
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
    public ParrotProcessorConfigPath getProcessor(String pIdProcessor) throws ParrotZkException {
        return getProcessorByPath(ParrotProcessorConfigPath.ZK_PATH + "/" + pIdProcessor);
    }

    @Override
    public ParrotProcessorConfigPath getProcessorByPath(String pPath) throws ParrotZkException {
        ParrotProcessorConfigPath processorConfigPath = new ParrotProcessorConfigPath(pPath);
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
    public ParrotProcessorNodePath getProcessorNodeByPath(String pPath) throws ParrotZkException {
        ParrotProcessorNodePath processorClusterPath = new ParrotProcessorNodePath(pPath);
        try {
            ParrotProcessorNodeApi processorNode = JsonUtils.byteArrayToObject(
                    getFramework().getData().forPath(processorClusterPath.getPath()), ParrotProcessorNodeApi.class);
            processorClusterPath.setProcessorNodeApi(processorNode);
        } catch (Throwable t) {
            throw new ParrotZkException(t);
        }
        return processorClusterPath;
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
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().namespace(ZK_NAMESPACE)
                .connectString(zookeeperHosts).retryPolicy(retryPolicy);
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
