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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import io.parrot.context.ParrotContext;
import io.parrot.exception.ParrotException;
import io.parrot.zookeeper.ParrotZkClient;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

@Dependent
public class ProcessorsManager {

	@Inject
	Logger LOG;

	@Inject
	ParrotZkClient zkClient;

	@Inject
	ParrotContext ctx;

	@Inject
	ParrotProcessorBuilder processorBuilder;

	public List<ParrotProcessor> createProcessors() {
		List<ParrotProcessorConfigPath> processorPaths = null;

		try {
			processorPaths = zkClient.getProcessors();
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}

		List<ParrotProcessor> processors = new ArrayList<ParrotProcessor>();
		for (ParrotProcessorConfigPath processor : processorPaths) {
			LOG.info("Creating processor " + processor.getProcessorApi().getId() + "...");
			processors.add(processorBuilder.build(processor.getProcessorApi()));
		}
		return processors;
	}

}
