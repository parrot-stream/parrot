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
package io.parrot.kafka.connect.jdbc.hive;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hive.service.cli.Type;
import org.apache.kafka.connect.data.Field;

import io.parrot.kafka.connect.Parameters;
import io.parrot.kafka.connect.jdbc.AbstractJdbcClient;

public abstract class HiveClient extends AbstractJdbcClient {

  HiveConfig config;

  public HiveClient(HiveConfig config) {
    this.config = config;
  }

  @Override
  public String getDataType(Field field) {
    switch (field.schema().type()) {
      case INT8:
      case INT16:
        return Type.SMALLINT_TYPE.getName();
      case INT32:
        return Type.INT_TYPE.getName();
      case INT64:
        /*
         * if (field.isTimestamp()) { return Type.VARCHAR_TYPE.getName() +
         * "(24)"; }
         */
        return Type.BIGINT_TYPE.getName();
      case FLOAT32:
        return Type.FLOAT_TYPE.getName();
      case FLOAT64:
        return Type.DOUBLE_TYPE.getName();
      case BYTES:
        return Type.DECIMAL_TYPE.getName() + "(38,"
            + field.schema().parameters().get(Parameters.SCALE.name().toLowerCase()) + ")";
      default:
        return Type.VARCHAR_TYPE.getName() + "(65355)";
    }
  }

  @Override
  public boolean existsDatabase() {
    ResultSet rs = executeSelect("SHOW DATABASES");
    try {
      while (rs.next()) {
        if (config.getHiveDatabase().equalsIgnoreCase(rs.getString(1))) {
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
  public void createDatabase() {
    executeUpdate("CREATE DATABASE " + config.getHiveDatabase());
  }

  @Override
  public String getJdbcDriver() {
    return "org.apache.hive.jdbc.HiveDriver";
  }

  @Override
  public String getJdbcUri() {
    return "jdbc:hive2://" + config.getHiveHostname() + ":" + config.getHivePort() + "/" + config.getHiveDatabase();
  }

  @Override
  public String getUserName() {
    return config.getHiveUserName();
  }

  @Override
  public String getPassword() {
    return config.getHivePassword();
  }

  public HiveConfig config() {
    return config;
  }

}
