package io.parrot.processor.sink;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.data.ParrotData;
import io.parrot.exception.ParrotException;
import io.parrot.processor.ParrotMeta;
import io.parrot.utils.ParrotLogFormatter;

public abstract class AbstractJdbcSink extends ParrotSink {

    Connection connection;

    public AbstractJdbcSink(ParrotProcessorApi pProcessor) {
        super(pProcessor);
    }

    protected void dropTable(String databaseName, String tableName) {
        if (existsTable(databaseName, tableName)) {
            String dropTableStatement = "DROP TABLE " + databaseName + "." + tableName.replaceAll("\\.", "_");
            LOG.debug(ParrotLogFormatter.formatLog("DROP STATEMENT", dropTableStatement));
            executeUpdate(dropTableStatement);
        }
    }

    protected boolean existsTable(String databaseName, String tableName) {
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
            se.printStackTrace();
            throw new ParrotException(se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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

    protected ResultSet executeSelect(String statement) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getJdbcConnection().createStatement();
            rs = stmt.executeQuery(statement);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ParrotException(se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    protected void executeUpdate(String statement) {
        Statement stmt = null;
        try {
            stmt = getJdbcConnection().createStatement();
            stmt.executeUpdate(statement);
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ParrotException(se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    protected Connection getJdbcConnection() {
        return connection;
    }

    protected void setJdbcConnection(Connection pConnection) {
        connection = pConnection;
    }

    protected abstract boolean existsDatabase(String pDatabaseName);

    protected abstract void createDatabase(String pDatabaseName);

    protected abstract void createTable(String tableName, ParrotMeta meta, ParrotData parrotData);

}
