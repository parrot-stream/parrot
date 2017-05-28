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
package io.parrot.zookeeper.path;

import io.parrot.api.model.ParrotProcessorNodeApi;

public class ParrotProcessorNodePath extends ParrotPath {

	public static final String ZK_PATH = "/processor/cluster";

	ParrotProcessorNodeApi processorNodeApi;

	public ParrotProcessorNodePath() {
	}

	public ParrotProcessorNodePath(String pPath) {
		path = pPath;
	}

	public ParrotProcessorNodePath(String pPath, ParrotProcessorNodeApi pProcessorNodeApi) {
		path = pPath;
		processorNodeApi = pProcessorNodeApi;
	}

	public ParrotProcessorNodePath(ParrotProcessorNodeApi pProcessorNodeApi) {
		processorNodeApi = pProcessorNodeApi;
	}

	public ParrotProcessorNodeApi getProcessorNodeApi() {
		return processorNodeApi;
	}

	public void setProcessorNodeApi(ParrotProcessorNodeApi pProcessorNodeApi) {
		processorNodeApi = pProcessorNodeApi;
	}

}