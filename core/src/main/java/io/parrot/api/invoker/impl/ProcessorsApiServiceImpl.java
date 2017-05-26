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
package io.parrot.api.invoker.impl;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.parrot.api.ProcessorsApi;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.service.ProcessorServices;

public class ProcessorsApiServiceImpl implements ProcessorsApi {

	@Inject
	ProcessorServices processorServices;

	public ProcessorsApiServiceImpl() {
		processorServices = CDI.current().select(ProcessorServices.class).get();
	}

	@Override
	public Response updateProcessor(String pId, ParrotProcessorApi pProcessor, SecurityContext pSecurityContext) {
		return Response.ok().entity(processorServices.updateProcessor(pProcessor)).build();
	}

	@Override
	public Response createProcessor(ParrotProcessorApi pProcessor, SecurityContext pSecurityContext) {		;
		return Response.ok().entity(processorServices.addProcessor(pProcessor)).build();
	}

	@Override
	public Response deleteProcessor(String pId, SecurityContext pSecurityContext) {
		processorServices.deleteProcessor(pId);
		return Response.ok().build();
	}

	@Override
	public Response getProcessors(SecurityContext pSecurityContext) {
		return Response.ok().entity(processorServices.getProcessors()).build();
	}

	@Override
	public Response getProcessor(String pId, SecurityContext pSecurityContext) {
		return Response.ok().entity(processorServices.getProcessor(pId)).build();
	}
}
