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

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface ApplicationMessages {

    @MessageTemplate("{parrot.processor.get.list.error}")
    String parrotProcessorGetListError(String pErrorMessage);

    @MessageTemplate("{parrot.processor.startup.info}")
    String parrotProcessorStartupInfo();

    @MessageTemplate("{parrot.processor.create.info}")
    String parrotProcessorCreateInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.created.info}")
    String parrotProcessorCreatedInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.create.error}")
    String parrotProcessorCreateError(String pId, String pErrorMessage);

    @MessageTemplate("{parrot.processor.start.info}")
    String parrotProcessorStartInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.start.error}")
    String parrotProcessorStartError(String pId, String pNode, String pErrorMessage);

    @MessageTemplate("{parrot.processor.stop.info}")
    String parrotProcessorStopInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.stop.error}")
    String parrotProcessorStopError(String pId, String pNode, String pErrorMessage);

    @MessageTemplate("{parrot.processor.delete.info}")
    String parrotProcessorDeleteInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.delete.error}")
    String parrotProcessorDeleteError(String pId, String pNode, String pErrorMessage);

    @MessageTemplate("{parrot.processor.add.info}")
    String parrotProcessorAddInfo(String pId, String pNode);

    @MessageTemplate("{parrot.processor.add.error}")
    String parrotProcessorAddError(String pId, String pNode, String pErrorMessage);

    @MessageTemplate("{parrot.processor.update.error}")
    String parrotProcessorUpdateError(String pId, String pNode, String pErrorMessage);

    @MessageTemplate("{parrot.processor.source.notopics.error}")
    String parrotProcessorSourceNoTopicsError(String pId);

}