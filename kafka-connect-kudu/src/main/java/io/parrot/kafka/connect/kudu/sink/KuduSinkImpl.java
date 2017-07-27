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
package io.parrot.kafka.connect.kudu.sink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.util.Requirements;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.Upsert;

import io.parrot.kafka.connect.jdbc.impala.ImpalaClient;
import io.parrot.util.ParrotHelper;

public class KuduSinkImpl {

  KuduClient kuduClient;
  KuduSinkConnectorConfig config;
  ImpalaKuduClient impalaClient;
  
  public KuduSinkImpl(KuduSinkConnectorConfig config) {
    this.config = config;
  }

  public void upsert(Collection<SinkRecord> sinkRecord) {
    for (SinkRecord r : sinkRecord) {
      String tableName = getTableName(r.topic());
      try {
        if (!getKuduClient().tableExists(tableName)) {
          createTable(tableName, r.keySchema(), r.valueSchema());
        }
        // Impala Integration
        if (config().impalaConfig().isImpalaIntegrated()) {
          if (!getImpalaClient().existsDatabase()) {
            getImpalaClient().createDatabase();
          }
          if (!getImpalaClient().existsTable(config().impalaConfig().getImpalaDatabase(), tableName)) {
            getImpalaClient().createTable(tableName);
          }
        }        
        
        upsert(tableName, r.valueSchema(), Requirements.requireStruct(r.value(), "Upserting value to Kudu"));
      } catch (ConnectException ce) {
        throw ce;
      } catch (Exception e) {
        e.printStackTrace();
        throw new ConnectException(e.getMessage());
      }
    }

  }

  void createTable(String tableName, Schema keySchema, Schema valueSchema) {
    List<ColumnSchema> columns = new ArrayList<>();
    List<String> keyColumns = new ArrayList<String>();
    for (Field f : keySchema.fields()) {
      columns.add(new ColumnSchema.ColumnSchemaBuilder(f.name().toLowerCase(), getKuduDataType(f)).key(true).build());
      keyColumns.add(f.name());
    }
    for (Field f : valueSchema.fields()) {
      if (keySchema.field(f.name()) == null) {
        columns.add(new ColumnSchema.ColumnSchemaBuilder(f.name().toLowerCase(), getKuduDataType(f))
            .nullable(f.schema().isOptional()).build());
      }
    }
    CreateTableOptions options = new CreateTableOptions();
    options.addHashPartitions(keyColumns, config.getKuduNumBuckets());
    try {
      getKuduClient().createTable(tableName, new org.apache.kudu.Schema(columns), options);
    } catch (KuduException e) {
      throw new ConnectException(e.getMessage());
    }
  }

  void upsert(String tableName, Schema valueSchema, Struct value) {
    KuduSession session = getKuduClient().newSession();
    KuduTable table;
    try {
      table = getKuduClient().openTable(tableName);
      Upsert upsert = table.newUpsert();
      for (Field f : valueSchema.fields()) {
        upsertField(upsert, f, value.get(f));
      }
      session.apply(upsert);
    } catch (KuduException e) {
      throw new ConnectException(e.getMessage());
    }
  }

  Upsert upsertField(Upsert upsert, Field field, Object value) {
    if (value == null) {
      return upsert;
    }
    switch (field.schema().type()) {
      case INT16:
        upsert.getRow().addShort(field.name().toLowerCase(), (Short) value);
        return upsert;
      case INT32:
        upsert.getRow().addInt(field.name().toLowerCase(), (Integer) value);
        return upsert;
      case INT64:
        /*
         * if (pField.isTimestamp()) {
         * upsert.getRow().addString(field.name().toLowerCase(),
         * Instant.ofEpochMilli((Long) pField.value()).toString()); } else {
         */
        upsert.getRow().addLong(field.name().toLowerCase(), (Long) value);
        // }

        return upsert;
      case FLOAT32:
        upsert.getRow().addFloat(field.name().toLowerCase(), (Float) value);
        return upsert;
      case FLOAT64:
        upsert.getRow().addDouble(field.name().toLowerCase(), (Double) value);
        return upsert;
      case BYTES:
        upsert.getRow().addString(field.name().toLowerCase(), ParrotHelper.formatBigDecimal(field.schema(), value));
        return upsert;
      default:
        upsert.getRow().addString(field.name().toLowerCase(), (String) value);
        return upsert;
    }
  }

  org.apache.kudu.Type getKuduDataType(Field field) {
    switch (field.schema().type()) {
      case INT16:
        return org.apache.kudu.Type.INT16;
      case INT32:
        return org.apache.kudu.Type.INT32;
      case INT64:
        /**
         * UNIXTIME_MICROS not yet supported in Impala
         */
        // if (pField.isTimestamp()) {
        // return org.apache.kudu.Type.UNIXTIME_MICROS;
        // }
        return org.apache.kudu.Type.INT64;
      case FLOAT32:
        return org.apache.kudu.Type.FLOAT;
      case FLOAT64:
        return org.apache.kudu.Type.DOUBLE;
      case BYTES:
        return org.apache.kudu.Type.STRING;
      default:
        return org.apache.kudu.Type.STRING;
    }
  }

  String getTableName(String topicName) {
    return topicName;
  }

  KuduClient getKuduClient() {
    if (kuduClient == null) {
      kuduClient = new KuduClient.KuduClientBuilder(config.getKuduMaster()).build();
    }
    return kuduClient;
  }

  ImpalaClient getImpalaClient() {
    if (impalaClient == null) {
      impalaClient = new ImpalaKuduClient(config().impalaConfig());
    }
    return impalaClient;
  }

  public KuduSinkConnectorConfig config() {
    return config;
  }
}
