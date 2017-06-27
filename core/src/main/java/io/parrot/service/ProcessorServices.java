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
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import io.parrot.api.model.ErrorApi;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorApi.StatusEnum;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.exception.BadRequestApiException;
import io.parrot.exception.GenericApiException;
import io.parrot.exception.ParrotApiException;
import io.parrot.exception.ParrotZkException;
import io.parrot.processor.ProcessorManager;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotHelper;
import io.parrot.utils.ParrotLogFormatter;
import io.parrot.zookeeper.ParrotZkClient;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;
import io.parrot.zookeeper.path.ParrotProcessorNodePath;

/**
 * This is the Parrot API Services implementation. This class is used in the
 * Swagger generated class ({@link io.parrot.api.ProcessorsApiServiceImpl}.
 */
@Stateless
public class ProcessorServices {

    @Inject
    Logger LOG;

    @Inject
    ParrotZkClient zkClient;

    @Inject
    ProcessorManager processorManager;

    @Inject
    ApplicationMessages message;

    public ParrotProcessorApi getProcessor(String pId) throws ParrotApiException {
        ParrotProcessorApi processor;
        try {
            processor = zkClient.getProcessor(pId).getProcessorApi();
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorGetError(pId, e.getMessage()));
        }
        return processor;
    }

    public ParrotProcessorNodeApi getProcessorNode(String pId, String pNode) throws ParrotApiException {
        ParrotProcessorNodeApi processorNode;
        try {
            processorNode = zkClient.getProcessorNode(pId, pNode).getProcessorNodeApi();
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorGetStatusError(pId, e.getMessage()));
        }
        return processorNode;
    }

    public List<ParrotProcessorNodeApi> getProcessorCluster(String pId) throws ParrotApiException {
        try {
            List<ParrotProcessorNodeApi> processorCluster = new ArrayList<ParrotProcessorNodeApi>();
            List<ParrotProcessorNodePath> processorNodePaths = zkClient.getProcessorCluster(pId);
            for (ParrotProcessorNodePath processorNodePath : processorNodePaths) {
                processorCluster.add(processorNodePath.getProcessorNodeApi());
            }
            return processorCluster;
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorGetStatusError(pId, e.getMessage()));
        }
    }

    public List<ParrotProcessorApi> getProcessors() throws ParrotApiException {
        try {
            List<ParrotProcessorApi> processors = new ArrayList<ParrotProcessorApi>();
            List<ParrotProcessorConfigPath> processorPaths = zkClient.getProcessors();
            for (ParrotProcessorConfigPath processorPath : processorPaths) {
                processors.add(processorPath.getProcessorApi());
            }
            return processors;
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorGetListError(e.getMessage()));
        }
    }

    public ParrotProcessorApi addProcessor(ParrotProcessorApi pProcessor) throws ParrotApiException {
        LOG.info(ParrotLogFormatter.formatLog(message.parrotProcessorAddInfo(pProcessor.getId()),
                JsonUtils.objectToJson(pProcessor, true)));
        try {
            ParrotHelper.validateSink(pProcessor.getSink());
            zkClient.addProcessor(pProcessor);
        } catch (ParrotZkException e) {
            throw new ParrotApiException(Response.Status.INTERNAL_SERVER_ERROR, message.parrotProcessorCreationError(pProcessor.getId(), e.getMessage()));
        }
        return pProcessor;
    }

    public void deleteProcessor(String pId) throws ParrotApiException {
        LOG.info(message.parrotProcessorDeleteInfo(pId));
        try {
            zkClient.deleteProcessor(pId);
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorDeleteError(pId, e.getMessage()));
        }
    }

    public ParrotProcessorApi updateProcessor(ParrotProcessorApi pProcessor) throws ParrotApiException {
        LOG.info(message.parrotProcessorUpdateInfo(pProcessor.getId()));
        try {
            return zkClient.updateProcessor(pProcessor).getProcessorApi();
        } catch (ParrotZkException e) {
            throw new GenericApiException(message.parrotProcessorUpdateError(pProcessor.getId(), e.getMessage()));
        }
    }

    public ParrotProcessorApi startProcessor(String pId) throws ParrotApiException {
        LOG.info(message.parrotProcessorStartInfo(pId));
        try {
            ParrotProcessorApi processor = zkClient.getProcessor(pId).getProcessorApi();
            processor.setStatus(StatusEnum.ENABLED);
            zkClient.updateProcessor(processor);
            return processor;
        } catch (Exception e) {
            throw new GenericApiException(message.parrotProcessorStartError(pId, e.getMessage()));
        }
    }

    public ParrotProcessorApi stopProcessor(String pId) throws ParrotApiException {
        LOG.info(message.parrotProcessorStopInfo(pId));
        try {
            ParrotProcessorApi processor = zkClient.getProcessor(pId).getProcessorApi();
            processor.setStatus(StatusEnum.DISABLED);
            zkClient.updateProcessor(processor);
            return processor;
        } catch (Exception e) {
            throw new GenericApiException(message.parrotProcessorStopError(pId, e.getMessage()));
        }
    }

    public ParrotProcessorApi restartProcessor(String pId) throws ParrotApiException {
        LOG.info(message.parrotProcessorRestartInfo(pId));
        try {
            ParrotProcessorApi processor = stopProcessor(pId);
            processor = startProcessor(pId);
            return processor;
        } catch (Exception e) {
            throw new GenericApiException(message.parrotProcessorRestartError(pId, e.getMessage()));
        }
    }

    public ParrotProcessorNodeApi startProcessorNode(String pId, String pNode) throws ParrotApiException {
        try {
            ParrotProcessorApi processor = zkClient.getProcessor(pId).getProcessorApi();
            ParrotProcessorNodeApi processorNode = zkClient.getProcessorNode(pId, pNode).getProcessorNodeApi();
            if (processor.getStatus() == StatusEnum.ENABLED) {
                LOG.info(message.parrotProcessorNodeStartInfo(pId, pNode));
                processorNode.setStatus(ParrotProcessorNodeApi.StatusEnum.STARTED);
                zkClient.updateProcessorNode(processorNode);
            } else {
                String errorMessage = message.parrotProcessorNodeStartError(pId, pNode,
                        "processor is in STOPPED status!");
                LOG.warn(errorMessage);
                throw new ParrotApiException(Response.Status.BAD_REQUEST, errorMessage);
            }
            return processorNode;
        } catch (Exception e) {
            throw new GenericApiException(message.parrotProcessorNodeStartError(pId, pNode, e.getMessage()));
        }
    }

    public ParrotProcessorNodeApi stopProcessorNode(String pId, String pNode) throws ParrotApiException {
        try {
            ParrotProcessorNodeApi processorNode = zkClient.getProcessorNode(pId, pNode).getProcessorNodeApi();
            if (processorNode.getStatus() == ParrotProcessorNodeApi.StatusEnum.STARTED) {
                LOG.info(message.parrotProcessorNodeStopInfo(pId, pNode));
                processorNode.setStatus(ParrotProcessorNodeApi.StatusEnum.STOPPED);
                zkClient.updateProcessorNode(processorNode);
            } else {
                String errorMessage = message.parrotProcessorNodeStartError(pId, pNode,
                        "processor node is already STOPPED!");
                LOG.warn(errorMessage);
                throw new ParrotApiException(Response.Status.BAD_REQUEST, errorMessage);
            }
            return processorNode;
        } catch (Exception e) {
            throw new GenericApiException(message.parrotProcessorNodeStartError(pId, pNode, e.getMessage()));
        }
    }
}
