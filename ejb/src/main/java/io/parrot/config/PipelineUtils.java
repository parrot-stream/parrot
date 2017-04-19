package io.parrot.config;

public class PipelineUtils {

	public enum RunEnv {
		PRODUCTION, DEVELOPMENT, LOCAL_UNIT, LOCAL_INTEGRATION, PIPELINE_UNIT, PIPELINE_INTEGRATION;
	}

	public static final String POSTGRESQL_HOSTNAME = "postgresql.hostname";
	public static final String POSTGRESQL_PORT = "postgresql.port";
	public static final String POSTGRESQL_DBNAME = "postgresql.dbname";

	private static final String JUNIT_TEST = "JUNIT_TEST";

	public static boolean isJUnitTest() {
		return Boolean.parseBoolean(getPropertyValue(JUNIT_TEST));
	}

	public static String getPostgresHostname() {
		return getPropertyValue(POSTGRESQL_HOSTNAME);
	}

	public static String getPostgresPort() {
		return getPropertyValue(POSTGRESQL_PORT);
	}

	public static String getPostgresDbName() {
		return getPropertyValue(POSTGRESQL_DBNAME);
	}

	public static boolean isLocal() {
		return RunEnv.LOCAL_UNIT == getRunEnv() || RunEnv.LOCAL_INTEGRATION == getRunEnv();
	}

	public static boolean isPipeline() {
		return RunEnv.PIPELINE_UNIT == getRunEnv() || RunEnv.PIPELINE_INTEGRATION == getRunEnv();
	}

	public static boolean isUnitTest() {
		return RunEnv.LOCAL_UNIT == getRunEnv() || RunEnv.PIPELINE_UNIT == getRunEnv();
	}

	public static boolean isIntegrationTest() {
		switch (getRunEnv()) {
		case LOCAL_INTEGRATION:
		case PIPELINE_INTEGRATION:
			return true;
		default:
			return false;
		}
	}

	public static boolean isDevelopment() {
		return RunEnv.DEVELOPMENT == getRunEnv();
	}

	public static boolean isProduction() {
		return RunEnv.PRODUCTION == getRunEnv();
	}

	public static synchronized RunEnv getRunEnv() {
		for (RunEnv testType : RunEnv.values()) {
			String value = getPropertyValue(testType.name());
			if (value == null) {
				continue;
			}
			if (Boolean.parseBoolean(value)) {
				return testType;
			}
		}
		return RunEnv.PRODUCTION;
	}

	private static String getPropertyValue(String propertyName) {
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue == null || propertyValue.isEmpty()) {
			propertyValue = System.getenv(propertyName);
		}
		return propertyValue;
	}

	public static synchronized String getPropertiesFile() {
		switch (getRunEnv()) {
		case PIPELINE_UNIT:
			return "Properties.properties.pipeline-ut";
		case PIPELINE_INTEGRATION:
			return "Properties.properties.pipeline-int";
		case LOCAL_UNIT:
		case DEVELOPMENT:
			return "Properties.properties.local-ut";
		case LOCAL_INTEGRATION:
			return "Properties.properties.local-int";
		default:
			return "Properties.properties"; // production properties
		}
	}

}
