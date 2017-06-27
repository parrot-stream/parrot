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
package io.parrot.processor.source;

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface ApplicationMessages {

    @MessageTemplate("{parrot.processor.source.debezium.key.payload.info}")
    String parrotProcessorSourceDebeziumKeyPayloadInfo();

    @MessageTemplate("{parrot.processor.source.debezium.value.payload.info}")
    String parrotProcessorSourceDebeziumValuePayloadInfo();

    @MessageTemplate("{parrot.processor.source.debezium.key.schema.info}")
    String parrotProcessorSourceDebeziumKeySchemaInfo();

    @MessageTemplate("{parrot.processor.source.debezium.value.schema.info}")
    String parrotProcessorSourceDebeziumValueSchemaInfo();

    @MessageTemplate("{parrot.processor.source.parrot.key.info}")
    String parrotProcessorSourceParrotKeyInfo();

    @MessageTemplate("{parrot.processor.source.parrot.value.info}")
    String parrotProcessorSourceParrotValueInfo();

    @MessageTemplate("{parrot.processor.source.withid.key.info}")
    String parrotProcessorSourceWithIdKeyInfo(String pIdProcessor);

    @MessageTemplate("{parrot.processor.source.withid.value.info}")
    String parrotProcessorSourceWithIdValueInfo(String pIdProcessor);

    @MessageTemplate("{parrot.processor.source.zookeeper.error}")
    String parrotProcessorSourceZooKeeperError(String pIdProcessor, String pErrorMessage);

    @MessageTemplate("{parrot.processor.source.kafka.error}")
    String parrotProcessorSourceKafkaError(String pIdProcessor, String pErrorMessage);
}