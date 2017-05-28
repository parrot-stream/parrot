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

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface ApplicationMessages {

	@MessageTemplate("{parrot.service.processor.creation.error}")
	String parrotProcessorCreationError(String pIdProcessor, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.get.error}")
	String parrotProcessorGetError(String pIdProcessor, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.get.cluster.error}")
	String parrotProcessorGetStatusError(String pIdProcessor, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.get.list.error}")
	String parrotProcessorGetListError(String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.delete.error}")
	String parrotProcessorDeleteError(String pId, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.update.error}")
	String parrotProcessorUpdateError(String pId, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.start.error}")
	String parrotProcessorStartError(String pId, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.stop.error}")
	String parrotProcessorStopError(String pId, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.restart.error}")
	String parrotProcessorRestartError(String pId, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.add.info}")
	String parrotProcessorAddInfo(String pId);

	@MessageTemplate("{parrot.service.processor.update.info}")
	String parrotProcessorUpdateInfo(String pId);

	@MessageTemplate("{parrot.service.processor.delete.info}")
	String parrotProcessorDeleteInfo(String pId);

	@MessageTemplate("{parrot.service.processor.start.info}")
	String parrotProcessorStartInfo(String pId);

	@MessageTemplate("{parrot.service.processor.stop.info}")
	String parrotProcessorStopInfo(String pId);

	@MessageTemplate("{parrot.service.processor.restart.info}")
	String parrotProcessorRestartInfo(String pId);

	@MessageTemplate("{parrot.service.processor.node.start.info}")
	String parrotProcessorNodeStartInfo(String pId, String pNode);

	@MessageTemplate("{parrot.service.processor.node.start.error}")
	String parrotProcessorNodeStartError(String pId, String pNode, String pErrorMessage);

	@MessageTemplate("{parrot.service.processor.node.stop.info}")
	String parrotProcessorNodeStopInfo(String pId, String pNode);

	@MessageTemplate("{parrot.service.processor.node.stop.error}")
	String parrotProcessorNodeStopError(String pId, String pNode, String pErrorMessage);
	
}