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
package io.parrot.processor.sink.kudu;

import java.sql.DriverManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.apache.camel.component.stream.StreamComponent;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.client.AlterTableOptions;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.Upsert;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.data.ParrotData;
import io.parrot.data.ParrotField;
import io.parrot.exception.ParrotException;
import io.parrot.processor.ParrotHeaderType;
import io.parrot.processor.ParrotMeta;
import io.parrot.processor.sink.impala.AbstractImpalaSink;
import io.parrot.processor.source.ParrotMetaType;
import io.parrot.utils.ParrotHelper;
import io.parrot.utils.ParrotLogFormatter;

@Named("Sink-Kudu")
public class KuduSink extends AbstractImpalaSink {

    KuduClient kuduClient;
    StreamComponent streamComponent;
    ParrotKuduConfiguration parrotKuduConfiguration;

    public KuduSink(ParrotProcessorApi pProcessor) {
        super(pProcessor);
        try {
            LOG.info(message.parrotProcessorSinkCreateInfo(getSinkConfig().getId()));

            /**
             * Creates the Kudu Configuration
             */
            setConfiguration(new ParrotKuduConfiguration(getSinkConfig().getConfigurations()));
            parrotKuduConfiguration = (ParrotKuduConfiguration) getConfiguration();
            setParrotImpalaConfiguration(parrotKuduConfiguration);

            /**
             * Creates the Kudu Component (we use a fake StreamComponent, the
             * component logic is implemented in the Sink)
             */
            setSinkComponent(new StreamComponent());
            streamComponent = (StreamComponent) getComponent();

            /**
             * Creates the Kudu Client
             */
            kuduClient = new KuduClient.KuduClientBuilder(parrotKuduConfiguration.getKuduMaster()).build();

            /**
             * Impala Connection
             */
            if (parrotKuduConfiguration.isImpalaEnabled()) {
                Class.forName(parrotKuduConfiguration.getImpalaJdbcDriver());
                setJdbcConnection(DriverManager.getConnection(parrotKuduConfiguration.getImpalaUri(),
                        parrotKuduConfiguration.getImpalaUsername(), parrotKuduConfiguration.getImpalaPassword()));
            }

            /**
             * Creates the Component Name
             */
            setComponentName("kudu" + getSinkConfig().getId());

            LOG.info(ParrotLogFormatter.formatLog(message.parrotProcessorSinkCreatedInfo(getSinkConfig().getId()),
                    parrotKuduConfiguration.toString()));

        } catch (ParrotException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParrotException(message.parrotProcessorSinkCreateError(getSinkConfig().getId(), e.getMessage()));
        }
    }

    @Override
    @Handler
    public void process(Exchange exchange) {
        super.process(exchange);
        Message m = exchange.getIn();

        /**
         * Add the dynamic endpoint, according to the HBase Sink type
         */
        String tableName = (String) m.getHeader(ParrotHeaderType.PARROT_SOURCE_NAME.name());
        m.setHeader(ParrotHeaderType.PARROT_SINK_TARGET_ENDPOINT.name(), getComponentName() + ":out");
        ParrotMeta meta = (ParrotMeta) exchange.getIn().getHeader(ParrotHeaderType.PARROT_META.name());
        ParrotData parrotData = (ParrotData) m.getHeader(ParrotHeaderType.PARROT_DATA.name());
        try {
            if (!kuduClient.tableExists(tableName)) {
                createKuduTable(tableName, meta, parrotData);
            }
            alterChangedTableStruct(tableName, meta, parrotData);
            if (parrotKuduConfiguration.isImpalaEnabled()) {
                if (!existsDatabase(parrotKuduConfiguration.getImpalaDatabase())) {
                    createDatabase(parrotKuduConfiguration.getImpalaDatabase());
                }
                if (!existsTable(parrotKuduConfiguration.getImpalaDatabase(), tableName)) {
                    createTable(tableName, meta, parrotData);
                }
            }
            upsert(tableName, meta, parrotData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParrotException(e.getMessage());
        }
    }

    /**
     * PRIVATE
     */
    void alterChangedTableStruct(String pTableName, ParrotMeta pMeta, ParrotData pParrotData) {
        try {
            KuduTable table = kuduClient.openTable(pTableName);
            AlterTableOptions alterTableOptions = new AlterTableOptions();
            for (ParrotField f : pParrotData.value().values()) {
                ColumnSchema columnSchema = null;
                try {
                    columnSchema = table.getSchema().getColumn(f.field());
                } catch (Exception e) {
                    ColumnSchema colSchema = new ColumnSchema.ColumnSchemaBuilder(f.field(), getKuduDataType(f))
                            .key(pParrotData.key().containsKey(f.field()))
                            .nullable(!pParrotData.key().containsKey(f.schema().name())).build();
                    alterTableOptions = alterTableOptions.addColumn(colSchema);
                }
                /**
                 * To change column type an update version of kudu is needed
                 */
                /*
                 * if (columnSchema != null) { if
                 * (!columnSchema.getType().equals(getKuduDataType(f.type))) {
                 * alterTableOptions = alterTableOptions. } }
                 */
            }
            kuduClient.alterTable(pTableName, alterTableOptions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParrotException(e.getMessage());
        }
    }

    void upsert(String pTableName, ParrotMeta pMeta, ParrotData pParrotData) {
        KuduSession session = kuduClient.newSession();
        KuduTable table;
        try {
            table = kuduClient.openTable(pTableName);
            Upsert upsert = table.newUpsert();
            for (ParrotField f : pParrotData.value().values()) {
                upsertField(upsert, f);
            }
            for (ParrotMetaType type : ParrotMetaType.values()) {
                upsertField(upsert, pMeta.get(type));
            }
            session.apply(upsert);
        } catch (KuduException e) {
            e.printStackTrace();
            throw new ParrotException(e.getMessage());
        }

    }

    void createKuduTable(String pTableName, ParrotMeta pMeta, ParrotData pParrotData) {
        ArrayList<ColumnSchema> columns = new ArrayList<>();
        List<String> keyColumns = new ArrayList<String>();
        for (ParrotField f : pParrotData.key().values()) {
            columns.add(new ColumnSchema.ColumnSchemaBuilder(f.field().toLowerCase(), getKuduDataType(f)).key(true)
                    .build());
            keyColumns.add(f.field());
        }
        for (ParrotField f : pParrotData.value().values()) {
            if (!pParrotData.key().containsKey(f.field())) {
                columns.add(new ColumnSchema.ColumnSchemaBuilder(f.field().toLowerCase(), getKuduDataType(f))
                        .nullable(f.schema().isOptional()).build());
            }
        }
        /**
         * Add Meta Data
         */
        for (ParrotMetaType type : ParrotMetaType.values()) {
            ParrotField metaField = pMeta.get(type);
            columns.add(
                    new ColumnSchema.ColumnSchemaBuilder(metaField.field().toLowerCase(), getKuduDataType(metaField))
                            .nullable(metaField.schema().isOptional()).build());
        }

        CreateTableOptions options = new CreateTableOptions();
        options.addHashPartitions(keyColumns, parrotKuduConfiguration.getNumBuckets());
        try {
            kuduClient.createTable(pTableName, new Schema(columns), options);
        } catch (KuduException e) {
            e.printStackTrace();
            throw new ParrotException(e.getMessage());
        }
    }

    @Override
    protected void createTable(String pTableName, ParrotMeta pParrotMeta, ParrotData pParrotData) {
        String storedBy = " STORED AS KUDU TBLPROPERTIES ('kudu.table_name' = '" + pTableName + "')";
        String createTableStatement = "CREATE EXTERNAL TABLE " + parrotKuduConfiguration.getImpalaDatabase() + "."
                + pTableName.replaceAll("\\.", "_") + storedBy;
        LOG.debug(ParrotLogFormatter.formatLog("CREATE STATEMENT", createTableStatement));
        executeUpdate(createTableStatement);
    }

    // Kudu
    org.apache.kudu.Type getKuduDataType(ParrotField pField) {
        switch (pField.schema().type()) {
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
           if (pField.isTimestamp()) {
                return org.apache.kudu.Type.STRING;
            }
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

    Upsert upsertField(Upsert pUpsert, ParrotField pField) {
        switch (pField.schema().type()) {
        case INT16:
            pUpsert.getRow().addShort(pField.field().toLowerCase(), (Short) pField.value());
            return pUpsert;
        case INT32:
            pUpsert.getRow().addInt(pField.field().toLowerCase(), (Integer) pField.value());
            return pUpsert;
        case INT64:
            if (pField.isTimestamp()) {
                pUpsert.getRow().addString(pField.field().toLowerCase(),
                        Instant.ofEpochMilli((Long) pField.value()).toString());
            } else {
                pUpsert.getRow().addLong(pField.field().toLowerCase(), (Long) pField.value());
            }
            return pUpsert;
        case FLOAT32:
            pUpsert.getRow().addFloat(pField.field().toLowerCase(), (Float) pField.value());
            return pUpsert;
        case FLOAT64:
            pUpsert.getRow().addDouble(pField.field().toLowerCase(), (Double) pField.value());
            return pUpsert;
        case BYTES:
            pUpsert.getRow().addString(pField.field().toLowerCase(), ParrotHelper.formatBigDecimal(pField));
            return pUpsert;
        default:
            pUpsert.getRow().addString(pField.field().toLowerCase(), (String) pField.value());
            return pUpsert;
        }
    }

    @Override
    public void checkConfiguration() {
        // TODO: Implementare check di connettività a Kudu
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void startup() {
        try {
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

}