package io.parrot.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;
import org.apache.deltaspike.core.util.PropertyFileUtils;

public class PriorityConfigSourceProvider implements ConfigSourceProvider {

	private static final String DEFAULT_PROPERTY_FILE = "META-INF/Default.properties";

	private List<ConfigSource> configSources = new ArrayList<ConfigSource>();

	public PriorityConfigSourceProvider() {
		configSources.addAll(defaultConfigSources());
		configSources.add(customConfigSource());
	}

	@Override
	public List<ConfigSource> getConfigSources() {
		return configSources;
	}

	private static List<PriorityConfigSource> defaultConfigSources() {
		List<PriorityConfigSource> configSources = new ArrayList<PriorityConfigSource>();
		try {
			Enumeration<URL> propertyFileUrls = PropertyFileUtils.resolvePropertyFiles(DEFAULT_PROPERTY_FILE);
			while (propertyFileUrls.hasMoreElements()) {
				URL propertyFileUrl = propertyFileUrls.nextElement();
				configSources.add(new DefaultConfigSource(propertyFileUrl));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error loading Default property files", e);
		}
		return configSources;
	}

	private ConfigSource customConfigSource() {
		return new CustomConfigSource();
	}
}
