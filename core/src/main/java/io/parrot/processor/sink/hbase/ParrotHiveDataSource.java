package io.parrot.processor.sink.hbase;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.hive.jdbc.HiveConnection;

public class ParrotHiveDataSource implements DataSource {

    String uri;
    Properties properties;

    public ParrotHiveDataSource(String pUri, Properties pProperties) {
        uri = pUri;
        properties = pProperties == null ? new Properties() : pProperties;
    }

    public Connection getConnection() throws SQLException {
        return getConnection("", "");
    }

    public Connection getConnection(String username, String password) throws SQLException {
        try {
            return new HiveConnection(uri, properties);
        } catch (Exception ex) {
            throw new SQLException("Error in getting HiveConnection", ex);
        }
    }

    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException("Method not supported");
    }

    public int getLoginTimeout() throws SQLException {
        throw new SQLException("Method not supported");
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Method not supported");
    }

    public void setLogWriter(PrintWriter arg0) throws SQLException {
        throw new SQLException("Method not supported");
    }

    public void setLoginTimeout(int arg0) throws SQLException {
        throw new SQLException("Method not supported");
    }

    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new SQLException("Method not supported");
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new SQLException("Method not supported");
    }

}
