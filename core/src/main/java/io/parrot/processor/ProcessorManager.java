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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.api.model.ParrotProcessorNodeApi.StatusEnum;
import io.parrot.config.IParrotConfigProperties;
import io.parrot.context.IDefaults;
import io.parrot.context.ParrotContext;
import io.parrot.exception.GenericApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.exception.ParrotException;
import io.parrot.utils.ParrotHelper;
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
    ApplicationMessages message;

    @Inject
    @ConfigProperty(name = IParrotConfigProperties.P_PARROT_NODE, defaultValue = IDefaults.DEF_PARROT_NODE)
    String parrotNode;

    Map<String, ParrotProcessor> processors;

    public Collection<ParrotProcessor> startUpProcessors() {
        List<ParrotProcessorConfigPath> processorPaths = null;
        try {
            processorPaths = zkClient.getProcessors();
        } catch (Exception e) {
            throw new ParrotException(message.parrotProcessorGetListError(e.getMessage()));
        }
        processors = new HashMap<String, ParrotProcessor>();
        LOG.info(message.parrotProcessorStartupInfo());
        String errorMessage = "";
        for (ParrotProcessorConfigPath processorConfig : processorPaths) {
            LOG.info(message.parrotProcessorCreateInfo(processorConfig.getProcessorApi().getId(), parrotNode));
            try {
                ParrotProcessor processor = addProcessor(processorConfig.getProcessorApi(), parrotNode);
                processor.checkConfiguration();
            } catch (ParrotException pe) {
                errorMessage = message.parrotProcessorAddError(processorConfig.getProcessorApi().getId(), parrotNode,
                        pe.getMessage());
                LOG.error(errorMessage);
            } finally {
                updateProcessorNode(processorConfig.getProcessorApi(), parrotNode, errorMessage);
            }
        }
        return processors.values();
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
        try {
            if (parrotNode.equalsIgnoreCase(pProcessorNode.getNode())) {
                LOG.info(message.parrotProcessorStopInfo(pProcessorNode.getId(), pProcessorNode.getNode()));
                stopProcessor(pProcessorNode.getId());
            }
            return pProcessorNode;
        } catch (Exception e) {
            throw new ParrotException(
                    message.parrotProcessorStopError(pProcessorNode.getId(), pProcessorNode.getNode(), e.getMessage()));
        }
    }

    public ParrotProcessorNodeApi deleteProcessorNode(ParrotProcessorNodeApi pProcessorNode) {
        try {
            if (parrotNode.equalsIgnoreCase(pProcessorNode.getNode())) {
                LOG.info(message.parrotProcessorDeleteInfo(pProcessorNode.getId(), pProcessorNode.getNode()));
                ctx.removeRoute(pProcessorNode.getId());
            }
            return pProcessorNode;
        } catch (Exception e) {
            throw new ParrotException(message.parrotProcessorDeleteError(pProcessorNode.getId(),
                    pProcessorNode.getNode(), e.getMessage()));
        }
    }

    public ParrotProcessorNodeApi startProcessorNode(ParrotProcessorNodeApi pProcessorNode) {
        try {
            if (StatusEnum.STARTED == pProcessorNode.getStatus()
                    && parrotNode.equalsIgnoreCase(pProcessorNode.getNode())) {
                LOG.info(message.parrotProcessorStartInfo(pProcessorNode.getId(), pProcessorNode.getNode()));
                startProcessor(pProcessorNode.getId());
            }
            return pProcessorNode;
        } catch (Exception e) {
            throw new ParrotException(message.parrotProcessorStartError(pProcessorNode.getId(),
                    pProcessorNode.getNode(), e.getMessage()));
        }
    }

    public ParrotProcessorApi addProcessorNode(ParrotProcessorApi pProcessor, String pNode) {
        String errorMessage = null;
        try {
            if (parrotNode.equalsIgnoreCase(pNode)) {
                LOG.info(message.parrotProcessorAddInfo(pProcessor.getId(), pNode));
                ParrotProcessor processor = addProcessor(pProcessor, pNode);
                processor.checkConfiguration();
                ctx.addRoutes(processor);
                ctx.addComponent(processor.getSink().getComponentName(), processor.getSink().getComponent());
            }
            return pProcessor;
        } catch (ParrotException pe) {
            pe.printStackTrace();
            errorMessage = message.parrotProcessorAddError(pProcessor.getId(), pNode, pe.getMessage());
            throw pe;
        } catch (Exception e) {
            errorMessage = message.parrotProcessorAddError(pProcessor.getId(), pNode, e.getMessage());
            throw new ParrotException(errorMessage);
        } finally {
            updateProcessorNode(pProcessor, pNode, errorMessage);
        }
    }

    public ParrotProcessorNodeApi updateProcessorNode(ParrotProcessorApi pProcessor, String pNode,
            String pErrorMessage) {
        ParrotProcessorNodeApi processorNode = new ParrotProcessorNodeApi();
        try {
            processorNode.setId(pProcessor.getId());
            processorNode.setStatus(pErrorMessage != null ? StatusEnum.FAILED
                    : (pProcessor.getStatus() == io.parrot.api.model.ParrotProcessorApi.StatusEnum.ENABLED
                            ? StatusEnum.STARTED : StatusEnum.STOPPED));
            processorNode.setNode(pNode);
            processorNode.setError(pErrorMessage);
            zkClient.updateProcessorNode(processorNode);
        } catch (ParrotApiException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParrotException(message.parrotProcessorUpdateError(pProcessor.getId(), pNode, e.getMessage()));
        }
        return processorNode;
    }

    ParrotProcessor addProcessor(ParrotProcessorApi pProcessor, String pNode) {
        ParrotProcessor processor = new ParrotProcessor(pProcessor, pNode);
        processors.put(pProcessor.getId(), processor);
        return processor;
    }

    /**
     * Processor Operations
     * 
     * @throws Exception
     */
    public void stopProcessor(String pId) throws Exception {
        if (processors.containsKey(pId)) {
            ctx.stopRoute(pId);
            ParrotProcessor processor = processors.get(pId);
            processor.shutdown();
        }
    }

    public void startProcessor(String pId) throws Exception {
        if (processors.containsKey(pId)) {
            ctx.startRoute(pId);
            ParrotProcessor processor = processors.get(pId);
            processor.startup();
        }
    }

    public void deleteProcessor(String pId) throws Exception {
        stopProcessor(pId);
        if (processors.containsKey(pId)) {
            ctx.removeRoute(pId);
            ctx.removeComponent(processors.get(pId).getSink().getComponentName());
            processors.remove(pId);
        }
    }

}