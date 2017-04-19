package io.parrot.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.deltaspike.core.spi.config.ConfigSource;

public abstract class PriorityConfigSource implements ConfigSource {

	public static final int LOWEST_PRIORITY = 0;

	public static final int HIGHEST_PRIORITY = 1000;

	private final Properties properties;

	private int priority;
	
	protected PriorityConfigSource(Properties properties, int priority) {
		this.properties = properties;
		this.priority = priority;
	}

	@Override
	public int getOrdinal() {
		return priority;
	}

	@Override
	public String getPropertyValue(String key) {
		return properties.getProperty(key);
	}

	@Override
	public Map<String, String> getProperties() {
		Map<String, String> result = new HashMap<String, String>();
		for (String propertyName : properties.stringPropertyNames()) {
			result.put(propertyName, properties.getProperty(propertyName));
		}
		return result;
	}

	@Override
	public boolean isScannable() {
		return true;
	}
	
}
