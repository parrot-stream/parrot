package io.parrot.processor.sink.impala;

import io.parrot.api.model.ParrotSinkConfigurationApi;
import io.parrot.processor.sink.SinkConfiguration;

public class ParrotImpalaConfiguration extends SinkConfiguration {

	public static final String CONF_IMPALA_EXTERNAL_ENABLED = "impala.external.enabled";
	public static final String CONF_IMPALA_EXTERNAL_DATABASE = "impala.external.database";
	public static final String CONF_IMPALA_EXTERNAL_HOST = "impala.external.host";
	public static final String CONF_IMPALA_EXTERNAL_PORT = "impala.external.port";

	public static final String CONF_IMPALA_USERNAME = "impala.username";
	public static final String CONF_IMPALA_PASSWORD = "impala.password";

	public ParrotImpalaConfiguration(ParrotSinkConfigurationApi pConfigurations) {
		super(pConfigurations);
	}

	public String getImpalaDatabase() {
		return getConfigurationValue(CONF_IMPALA_EXTERNAL_DATABASE, "default");
	}

	public String getImpalaHost() {
		return getConfigurationValue(CONF_IMPALA_EXTERNAL_HOST, "localhost");
	}

	public String getImpalaPort() {
		return getConfigurationValue(CONF_IMPALA_EXTERNAL_PORT, "21050");
	}

	public String getImpalaEnabled() {
		return getConfigurationValue(CONF_IMPALA_EXTERNAL_ENABLED, "false");
	}

	public boolean isImpalaEnabled() {
		return "true".equalsIgnoreCase(getImpalaEnabled());
	}

	public String getImpalaUsername() {
		return getConfigurationValue(CONF_IMPALA_USERNAME, "parrot");
	}

	public String getImpalaPassword() {
		return getConfigurationValue(CONF_IMPALA_PASSWORD, "parrot");
	}

	public String getImpalaJdbcDriver() {
		return "org.apache.hive.jdbc.HiveDriver";
	}

	public String getImpalaUri() {
		return "jdbc:hive2://" + getImpalaHost() + ":" + getImpalaPort() + "/;auth=noSasl";
	}

}
