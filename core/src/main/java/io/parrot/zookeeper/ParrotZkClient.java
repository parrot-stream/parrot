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

import java.util.List;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.exception.ParrotZkException;
import io.parrot.zookeeper.path.AboutPath;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;
import io.parrot.zookeeper.path.ParrotProcessorNodePath;

public interface ParrotZkClient {

	public String ZK_NAMESPACE = "parrot";

	public AboutPath createAbout(AboutPath pAbout) throws ParrotZkException;

	public void deleteAbout() throws ParrotZkException;

	public ParrotProcessorConfigPath addProcessor(ParrotProcessorApi pProcessor) throws ParrotZkException;

	public ParrotProcessorConfigPath updateProcessor(ParrotProcessorApi pProcessor) throws ParrotZkException;

	public ParrotProcessorConfigPath getProcessor(String pId) throws ParrotZkException;

	public ParrotProcessorConfigPath getProcessorByPath(String pPath) throws ParrotZkException;

	public void deleteProcessor(String pId) throws ParrotZkException;

	public ParrotProcessorNodePath addProcessorNode(ParrotProcessorNodeApi pProcessorNodeM)
			throws ParrotZkException;

	public ParrotProcessorNodePath updateProcessorNode(ParrotProcessorNodeApi pProcessorNode)
			throws ParrotZkException;

	public ParrotProcessorNodePath getProcessorNodeByPath(String pPath) throws ParrotZkException;

	public void deleteProcessorNode(String pId, String pNode) throws ParrotZkException;

	public ParrotProcessorNodePath getProcessorNode(String pId, String pNode) throws ParrotZkException;

	public List<ParrotProcessorNodePath> getProcessorCluster(String pId) throws ParrotZkException;

	public List<ParrotProcessorConfigPath> getProcessors() throws ParrotZkException;

	public AboutPath getAbout() throws ParrotZkException;

	public AboutPath updateAbout(AboutPath pAbout) throws ParrotZkException;

	public boolean exists(String pPath) throws ParrotZkException;

}