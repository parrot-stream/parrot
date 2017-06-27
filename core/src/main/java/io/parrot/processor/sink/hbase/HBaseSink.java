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
package io.parrot.processor.sink.hbase;

import java.io.IOException;
import java.sql.DriverManager;

import javax.inject.Named;

import org.apache.avro.Schema;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.apache.camel.component.hbase.HBaseComponent;
import org.apache.camel.component.hbase.HBaseConstants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.connect.json.JsonConverter;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.data.ParrotData;
import io.parrot.data.ParrotField;
import io.parrot.exception.ParrotException;
import io.parrot.processor.ParrotHeaderType;
import io.parrot.processor.ParrotMeta;
import io.parrot.processor.sink.hive.AbstractHiveSink;
import io.parrot.processor.source.ParrotMetaType;
import io.parrot.utils.ParrotHelper;
import io.parrot.utils.ParrotLogFormatter;

@Named("Sink-HBase")
public class HBaseSink extends AbstractHiveSink {

    Connection hbaseConnection;
    HBaseComponent hbaseComponent;
    ParrotHBaseConfiguration parrotHBaseConfiguration;
    JsonConverter jsonConverter;

    public HBaseSink(ParrotProcessorApi pProcessor) {
        super(pProcessor);
        try {
            LOG.info(message.parrotProcessorSinkCreateInfo(getSinkConfig().getId()));
            jsonConverter = new JsonConverter();

            /**
             * Creates the HBase Configuration
             */
            setConfiguration(new ParrotHBaseConfiguration(getSinkConfig().getConfigurations()));
            parrotHBaseConfiguration = (ParrotHBaseConfiguration) getConfiguration();
            setParrotHiveConfiguration(parrotHBaseConfiguration);

            /**
             * Creates the HBase Component
             */

            setSinkComponent(new HBaseComponent());
            hbaseComponent = (HBaseComponent) getComponent();
            hbaseComponent.setPoolMaxSize(1);
            hbaseComponent.setConfiguration(parrotHBaseConfiguration.getHbaseConfiguration());

            /**
             * Hive Connection
             */
            if (parrotHBaseConfiguration.isHiveEnabled()) {
                Class.forName("org.apache.hive.jdbc.HiveDriver");
                setJdbcConnection(DriverManager.getConnection(parrotHBaseConfiguration.getHiveUri(),
                        parrotHBaseConfiguration.getHiveUsername(), parrotHBaseConfiguration.getHivePassword()));
            }

            /**
             * Creates the Component Name
             */
            setComponentName("hbase." + getSinkConfig().getId());

            LOG.info(ParrotLogFormatter.formatLog(message.parrotProcessorSinkCreatedInfo(getSinkConfig().getId()),
                    parrotHBaseConfiguration.toString()));

        } catch (ParrotException pe) {
            throw pe;
        } catch (Exception e) {
            e.printStackTrace();
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

        ParrotMeta meta = (ParrotMeta) exchange.getIn().getHeader(ParrotHeaderType.PARROT_META.name());
        ParrotData parrotData = (ParrotData) m.getHeader(ParrotHeaderType.PARROT_DATA.name());

        m.setHeader(ParrotHeaderType.PARROT_SINK_TARGET_ENDPOINT.name(),
                getComponentName() + ":" + tableName + "?operation="
                        + (ParrotHelper.isDeleteChangeEventType(meta) ? HBaseConstants.DELETE : HBaseConstants.PUT)
                        + "&mappingStrategyName=body&synchronous=true");

        try {

            if (!existsHBaseTable(tableName)) {
                createHBaseTable(tableName);
            }
            boolean tableStructChanged = checkTableStructChanged(tableName, meta, parrotData);
            if (parrotHBaseConfiguration.isHiveEnabled()) {
                if (!existsDatabase(parrotHBaseConfiguration.getHiveDatabase())) {
                    createDatabase(parrotHBaseConfiguration.getHiveDatabase());
                }
                if (tableStructChanged || !existsTable(parrotHBaseConfiguration.getHiveDatabase(), tableName)) {
                    dropTable(parrotHBaseConfiguration.getHiveDatabase(), tableName);
                    createTable(tableName, meta, parrotData);
                }
            }
            // upsert(tableName, parrotData);

        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

    /**
     * PRIVATE
     * 
     * @throws IOException
     */
    boolean checkTableStructChanged(String tableName, ParrotMeta meta, ParrotData processingParrotData) {
        try {
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            // Retrieves the key Avro record
            Get get = new Get(Bytes.toBytes("struct"));
            Result rs = table.get(get);
            boolean structureChanged = false;
            if (rs.advance()) {
                Cell cell = rs.current();
                String columnName = Bytes.toString(
                        Bytes.copy(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                if (columnName.equals("key")) {
                    Schema currentKey = ParrotHelper.toAvroSchema(Bytes.toString(CellUtil.cloneValue(cell)));
                    if (!currentKey.equals(ParrotHelper.toAvroSchema(processingParrotData.key()))) {
                        structureChanged = true;
                    }
                }
                if (columnName.equals("value")) {
                    Schema currentValue = ParrotHelper.toAvroSchema(Bytes.toString(CellUtil.cloneValue(cell)));
                    if (!currentValue.equals(ParrotHelper.toAvroSchema(processingParrotData.value()))) {
                        structureChanged = true;
                    }
                }
            }
            Put put = new Put(Bytes.toBytes("struct"));
            put.addColumn(Bytes.toBytes(HBaseFamilyType.FAMILY_PARROT.toString()), Bytes.toBytes("key"),
                    Bytes.toBytes(ParrotHelper.toAvroSchema(processingParrotData.key()).toString()));
            put.addColumn(Bytes.toBytes(HBaseFamilyType.FAMILY_PARROT.toString()), Bytes.toBytes("value"),
                    Bytes.toBytes(ParrotHelper.toAvroSchema(processingParrotData.value()).toString()));
            table.put(put);
            return structureChanged;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParrotException(e.getMessage());
        }
    }

    void createHBaseTable(String tableName) {
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        tableDescriptor.addFamily(new HColumnDescriptor(HBaseFamilyType.FAMILY_SOURCE.toString()));
        tableDescriptor.addFamily(new HColumnDescriptor(HBaseFamilyType.FAMILY_PARROT.toString()));
        try {
            getAdmin().createTable(tableDescriptor);
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

    @Override
    protected void createTable(String tableName, ParrotMeta meta, ParrotData parrotData) {
        String columns = "";
        for (ParrotField f : parrotData.value().values()) {
            columns += f.field().toLowerCase() + " " + getHiveDataType(f) + ",";
        }
        for (ParrotMetaType m : meta.keySet()) {
            columns += m.name().toLowerCase() + " " + getHiveDataType(meta.get(m)) + ",";
        }
        columns = columns.substring(0, columns.length() - 1);
        String keys = "";
        for (ParrotField f : parrotData.key().values()) {
            keys += f.schema().type() + ",";
        }
        keys = keys.substring(0, keys.length() - 1);
        String sqlWithSerdeProperties = "";
        for (ParrotField f : parrotData.value().values()) {
            if (!parrotData.key().containsKey(f.field())) {
                sqlWithSerdeProperties += "," + HBaseFamilyType.FAMILY_SOURCE.toString() + ":" + f.field();
            }
        }
        for (ParrotMetaType m : meta.keySet()) {
            sqlWithSerdeProperties += "," + HBaseFamilyType.FAMILY_PARROT.toString() + ":" + m.name().toLowerCase();
        }
        String serdeProperties = "SERDEPROPERTIES ('hbase.columns.mapping' = ':key" + sqlWithSerdeProperties + "')";
        String storedBy = " STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' WITH " + serdeProperties
                + " TBLPROPERTIES ('hbase.table.name' = '" + tableName + "')";
        String createTableStatement = "CREATE EXTERNAL TABLE " + parrotHBaseConfiguration.getHiveDatabase() + "."
                + tableName.replaceAll("\\.", "_") + "(" + columns + ")" + storedBy;
        LOG.debug(ParrotLogFormatter.formatLog("CREATE STATEMENT", createTableStatement));
        executeUpdate(createTableStatement);
    }

    Connection getConnection() {
        if (hbaseConnection == null || hbaseConnection.isClosed()) {
            try {
                hbaseConnection = ConnectionFactory.createConnection(parrotHBaseConfiguration.getHbaseConfiguration());
            } catch (Exception e) {
                throw new ParrotException(e.getMessage());
            }
        }
        return hbaseConnection;
    }

    Admin getAdmin() {
        try {
            return getConnection().getAdmin();
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }

    }

    TableName[] getTableNames() {
        try {
            return getAdmin().listTableNames();
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

    boolean existsHBaseTable(String tableName) {
        TableName[] hbaseTables = getTableNames();
        for (TableName t : hbaseTables) {
            if (t.getNameAsString().equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkConfiguration() {
        try {
            ParrotHelper.checkConnection(parrotHBaseConfiguration.getHbaseZooKeeperQuorum());
        } catch (Exception e) {
            new ParrotException("Unable to connect to HBase ZooKeeper: " + e.getMessage());
        }
        try {
            if (parrotHBaseConfiguration.isHiveEnabled()) {
                ParrotHelper.checkConnection(
                        parrotHBaseConfiguration.getHiveHost() + ":" + parrotHBaseConfiguration.getHivePort());
            }
        } catch (Exception e) {
            new ParrotException("Unable to connect to Hive: " + e.getMessage());
        }
        try {
            HBaseAdmin.checkHBaseAvailable(parrotHBaseConfiguration.getHbaseConfiguration());
        } catch (Exception e) {
            throw new ParrotException("Unable to connect to HBase: " + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        try {
            hbaseComponent.stop();
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

    @Override
    public void startup() {
        try {
            hbaseComponent.start();
        } catch (Exception e) {
            throw new ParrotException(e.getMessage());
        }
    }

}