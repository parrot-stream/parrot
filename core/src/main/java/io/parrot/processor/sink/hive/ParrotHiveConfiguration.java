package io.parrot.processor.sink.hive;

import io.parrot.api.model.ParrotSinkConfigurationApi;
import io.parrot.processor.sink.SinkConfiguration;

public class ParrotHiveConfiguration extends SinkConfiguration {

    public static final String CONF_HIVE_EXTERNAL_ENABLED = "hive.external.enabled";
    public static final String CONF_HIVE_EXTERNAL_DATABASE = "hive.external.database";
    public static final String CONF_HIVE_EXTERNAL_HOST = "hive.external.host";
    public static final String CONF_HIVE_EXTERNAL_PORT = "hive.external.port";
    public static final String CONF_HIVE_USERNAME = "hive.username";
    public static final String CONF_HIVE_PASSWORD = "hive.password";

    public ParrotHiveConfiguration(ParrotSinkConfigurationApi pConfigurations) {
        super(pConfigurations);
    }

    public String getHiveDatabase() {
        return getConfigurationValue(CONF_HIVE_EXTERNAL_DATABASE, "default");
    }

    public String getHiveHost() {
        return getConfigurationValue(CONF_HIVE_EXTERNAL_HOST, "localhost");
    }

    public String getHivePort() {
        return getConfigurationValue(CONF_HIVE_EXTERNAL_PORT, "10000");
    }

    public String getHiveEnabled() {
        return getConfigurationValue(CONF_HIVE_EXTERNAL_ENABLED, "false");
    }

    public String getHiveUsername() {
        return getConfigurationValue(CONF_HIVE_USERNAME, "parrot");
    }

    public String getHivePassword() {
        return getConfigurationValue(CONF_HIVE_PASSWORD, "parrot");
    }

    public boolean isHiveEnabled() {
        return "true".equalsIgnoreCase(getHiveEnabled());
    }

    public String getHiveJdbcDriver() {
        return "org.apache.hive.jdbc.HiveDriver";
    }

    public String getHiveUri() {
        return "jdbc:hive2://" + getHiveHost() + ":" + getHivePort() + "/" + getHiveDatabase();
    }

}
