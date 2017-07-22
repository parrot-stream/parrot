/*-
 * ============================LICENSE_START============================
 * Parrot
 * ---------------------------------------------------------------------
 * Copyright (C) 2017 Parrot
 * ---------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================LICENSE_END=============================
 */
package io.parrot.kafka.connect.transforms;

import java.util.Map;

import org.apache.kafka.common.cache.Cache;
import org.apache.kafka.common.cache.LRUCache;
import org.apache.kafka.common.cache.SynchronizedCache;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.Requirements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.data.Envelope;

public class DebeziumTransform<R extends ConnectRecord<R>> implements Transformation<R> {

  static Logger logger = LoggerFactory.getLogger(DebeziumTransform.class);

  DebeziumTransformConfig config;

  Cache<Schema, Schema> envelopeSchemaUpdateCache;
  Schema transformedEnvelopeSchema;

  @Override
  public void configure(Map<String, ?> configs) {
    config = new DebeziumTransformConfig(configs);
    envelopeSchemaUpdateCache = new SynchronizedCache<>(new LRUCache<Schema, Schema>(16));
  }

  @Override
  public R apply(R record) {
    Schema newEnvelopeSchema = transformEnvelopeSchema(record.valueSchema());
    return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(),
        newEnvelopeSchema,
        transformEnvelope(newEnvelopeSchema, Requirements.requireStruct(record.value(), "Updating envelope")),
        record.timestamp());
  }

  @Override
  public ConfigDef config() {
    return DebeziumTransformConfig.CONFIG;
  }

  @Override
  public void close() {
  }

  Schema transformEnvelopeSchema(Schema oldEnvelopeSchema) {
    if (transformedEnvelopeSchema == null) {
      transformedEnvelopeSchema = oldEnvelopeSchema.field(Envelope.FieldName.AFTER).schema();
    }
    return transformedEnvelopeSchema;
  }

  Struct transformEnvelope(Schema newEnvelopeSchema, Struct oldEnvelope) {
    final Struct newEnvelope = new Struct(newEnvelopeSchema);
    Field afterField = oldEnvelope.schema().field(Envelope.FieldName.AFTER);
    Struct afterValue = Requirements.requireStruct(oldEnvelope.get(afterField), "Extracting after value");
    for (Field field : afterValue.schema().fields()) {
      newEnvelope.put(field.name(), afterValue.get(field));
    }
    return newEnvelope;
  }

}
