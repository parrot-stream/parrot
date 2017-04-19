package io.parrot.config;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.util.PropertyFileUtils;

/**
 * <ul>
 * <li>Properties.properties.local-ut</li>
 * <li>Properties.properties.local-int</li>
 * <li>Properties.properties.pipeline-ut</li>
 * <li>Properties.properties.pipeline-int</li>
 * </ul>
 *
 */
@Exclude
public class CustomConfigSource extends PriorityConfigSource {

	protected CustomConfigSource() {
		super(loadProperties(), LOWEST_PRIORITY + 10);
	}

	@Override
	public String getConfigName() {
		return customPropertyFile();
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();
		String propertyFileName = customPropertyFile();
		try {
			Enumeration<URL> propertyFiles = PropertyFileUtils.resolvePropertyFiles(propertyFileName);
			if (!propertyFiles.hasMoreElements()) {
				throw new IllegalStateException("Missing configuration property file " + propertyFileName);
			}
			while (propertyFiles.hasMoreElements()) {
				properties.putAll(PropertyFileUtils.loadProperties(propertyFiles.nextElement()));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error loading custom property file " + propertyFileName, e);
		}
		return properties;
	}

	private static String customPropertyFile() {
		return PipelineUtils.getPropertiesFile();
	}
}
