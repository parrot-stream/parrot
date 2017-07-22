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
package io.parrot.kafka.connect.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJdbcClient {

  static Logger logger = LoggerFactory.getLogger(AbstractJdbcClient.class);

  Connection connection;

  public void dropTable(String databaseName, String tableName) {
    if (existsTable(databaseName, tableName)) {
      String dropTableStatement = "DROP TABLE " + databaseName + "." + tableName.replaceAll("\\.", "_");
      executeUpdate(dropTableStatement);
    }
  }

  public boolean existsTable(String databaseName, String tableName) {
    String queryExists = "SHOW TABLES IN " + databaseName + " LIKE '" + tableName.replaceAll("\\.", "_") + "'";
    Statement stmt = null;
    ResultSet rs = null;
    boolean existsHiveTable = false;
    try {
      stmt = getJdbcConnection().createStatement();
      rs = stmt.executeQuery(queryExists);
      if (rs.next()) {
        existsHiveTable = true;
      } else {
        existsHiveTable = false;
      }
    } catch (SQLException se) {
      throw new ConnectException(se.getMessage());
    } catch (Exception e) {
      throw new ConnectException(e.getMessage());
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
      }
    }
    return existsHiveTable;
  }

  public ResultSet executeSelect(String statement) {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = getJdbcConnection().createStatement();
      rs = stmt.executeQuery(statement);
    } catch (SQLException se) {
      throw new ConnectException(se.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      throw new ConnectException(e.getMessage());
    }
    return rs;
  }

  public void executeUpdate(String statement) {
    Statement stmt = null;
    try {
      stmt = getJdbcConnection().createStatement();
      stmt.executeUpdate(statement);
    } catch (SQLException se) {
      throw new ConnectException(se.getMessage());
    } catch (Exception e) {
      throw new ConnectException(e.getMessage());
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
      }
    }
  }

  public Connection getJdbcConnection() {
    if (connection == null) {
      try {
        Class.forName(getJdbcDriver());
        connection = DriverManager.getConnection(getJdbcUri(), getUserName(), getPassword());
      } catch (SQLException e) {
        throw new ConnectException(e.getMessage());
      } catch (ClassNotFoundException e) {
        throw new ConnectException(e.getMessage());
      }
    }
    return connection;
  }

  public abstract String getDataType(Field field);
  
  public abstract boolean existsDatabase();

  public abstract void createDatabase();

  public abstract void createTable(String tableName);

  public abstract String getJdbcDriver();

  public abstract String getJdbcUri();

  public abstract String getUserName();

  public abstract String getPassword();
}
