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

import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverters;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.processor.ParrotHeaderType;

@ApplicationScoped
public class JsonToSchemaAndValue implements TypeConverters {

    Logger LOG;
    JsonConverter jsonConverter;
    ApplicationMessages message;

    public JsonToSchemaAndValue() {
        LOG = LoggerFactory.getLogger(getClass());
        jsonConverter = new JsonConverter();
        jsonConverter.configure(new HashMap<String, Object>(), false);
        message = CDI.current().select(ApplicationMessages.class).get();
    }

    @Converter
    public SchemaAndValue toSchemaAndValue(String jsonValue, Exchange exchange) {
        SchemaAndValue schemaAndValue = null;
        try {
            schemaAndValue = jsonConverter.toConnectData("", jsonValue.getBytes());

            if (schemaAndValue.schema() != null) {
                exchange.getIn().setHeader(ParrotHeaderType.PARROT_IS_VALID.name(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schemaAndValue;
    }
}
