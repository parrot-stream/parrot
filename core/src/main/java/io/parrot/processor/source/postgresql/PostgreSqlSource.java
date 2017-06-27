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
package io.parrot.processor.source.postgresql;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.data.ParrotField;
import io.parrot.data.reader.ChangeEventKeyReader;
import io.parrot.data.reader.ChangeEventValueReader;
import io.parrot.data.reader.postgesql.PostgreSqlSourceType;
import io.parrot.processor.ParrotHeaderType;
import io.parrot.processor.ParrotMeta;
import io.parrot.processor.source.ParrotMetaType;
import io.parrot.processor.source.ParrotSource;
import io.parrot.utils.KafkaUtils;

@Named("Source-PostgreSql")
public class PostgreSqlSource extends ParrotSource {

    public PostgreSqlSource(ParrotProcessorApi pProcessor) {
        super(pProcessor);
    }

    @Override
    @Handler
    public void process(Exchange exchange) {
        super.process(exchange);

        SchemaAndValue key = (SchemaAndValue) exchange.getIn()
                .getHeader(ParrotHeaderType.PARROT_CHANGE_EVENT_KEY.name());
        SchemaAndValue value = (SchemaAndValue) exchange.getIn()
                .getHeader(ParrotHeaderType.PARROT_CHANGE_EVENT_VALUE.name());
        ChangeEventKeyReader keyReader = new ChangeEventKeyReader(key);
        ChangeEventValueReader valueReader = new ChangeEventValueReader(value);

        /**
         * Fills the PostgreSql specific part of Parrot Metadata
         */

        ParrotMeta parrotMeta = (ParrotMeta) exchange.getIn().getHeader(ParrotHeaderType.PARROT_META.name());
        Long tsUsec = valueReader.getSourceValue().getInt64(PostgreSqlSourceType.TS_USEC.name().toLowerCase());
        parrotMeta.addMeta(ParrotMetaType.PARROT_SOURCE_TIMESTAMP,
                new ParrotField(ParrotMetaType.PARROT_SOURCE_TIMESTAMP.name(), Long.valueOf(Math.round(tsUsec / 1000d)),
                        SchemaBuilder.int64().name(ParrotMetaType.PARROT_PROCESSOR_TIMESTAMP.name()), true));

        exchange.getIn().setBody(exchange.getIn().getHeader(ParrotHeaderType.PARROT_DATA.name()));
    }

    @Override
    public List<String> getTopics() {
        List<String> topics = KafkaUtils.getAllTopics(processor.getSource());
        /**
         * TODO: Implementare filtro
         */
        return topics;
    }

}