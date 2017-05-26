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

import io.parrot.api.model.ParrotNodeApi;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorStatusApi;
import io.parrot.exception.ParrotZkException;
import io.parrot.zookeeper.path.AboutPath;
import io.parrot.zookeeper.path.ParrotProcessorClusterPath;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

public interface ParrotZkClient {

	public String ZK_NAMESPACE = "parrot";

	public AboutPath createAbout(AboutPath about) throws ParrotZkException;

	public void deleteAbout() throws ParrotZkException;

	public ParrotProcessorConfigPath addProcessor(ParrotProcessorApi processor) throws ParrotZkException;

	public ParrotProcessorConfigPath updateProcessor(ParrotProcessorApi processor) throws ParrotZkException;

	public ParrotProcessorConfigPath getProcessor(String id) throws ParrotZkException;

	public void deleteProcessor(String id) throws ParrotZkException;

	public ParrotProcessorClusterPath addProcessorStatusInCluster(ParrotNodeApi parrotNodeApi,
			ParrotProcessorStatusApi processorStatus) throws ParrotZkException;

	public List<ParrotProcessorConfigPath> getProcessors() throws ParrotZkException;

	public AboutPath getAbout() throws ParrotZkException;

	public AboutPath updateAbout(AboutPath about) throws ParrotZkException;

	public boolean exists(String path) throws ParrotZkException;

}