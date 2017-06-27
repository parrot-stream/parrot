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
package io.parrot.processor.sink.impala;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Named;

import org.apache.hive.service.cli.Type;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.data.ParrotField;
import io.parrot.processor.sink.AbstractJdbcSink;

@Named("Sink-Impala")
public abstract class AbstractImpalaSink extends AbstractJdbcSink {
    ParrotImpalaConfiguration parrotImpalaConfiguration;

    public AbstractImpalaSink(ParrotProcessorApi pProcessor) {
        super(pProcessor);
    }

    protected String getImpalaDataType(ParrotField pField) {
        switch (pField.schema().type()) {
        case INT8:
        case INT16:
            return Type.SMALLINT_TYPE.getName();
        case INT32:
            return Type.INT_TYPE.getName();
        case INT64:
            return Type.BIGINT_TYPE.getName();
        case FLOAT32:
            return Type.FLOAT_TYPE.getName();
        case FLOAT64:
            return Type.DOUBLE_TYPE.getName();
        default:
            return Type.VARCHAR_TYPE.getName() + "(65355)";
        }
    }

    protected ParrotImpalaConfiguration getParrotImpalaConfiguration() {
        return parrotImpalaConfiguration;
    }

    protected void setParrotImpalaConfiguration(ParrotImpalaConfiguration pParrotImpalaConfiguration) {
        this.parrotImpalaConfiguration = pParrotImpalaConfiguration;
    }

    @Override
    protected boolean existsDatabase(String pDatabaseName) {
        ResultSet rs = executeSelect("SHOW DATABASES");
        try {
            while (rs.next()) {
                if (pDatabaseName.equalsIgnoreCase(rs.getString(1))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    @Override
    protected void createDatabase(String pDatabaseName) {
        executeUpdate("CREATE DATABASE " + pDatabaseName);
    }

    
}