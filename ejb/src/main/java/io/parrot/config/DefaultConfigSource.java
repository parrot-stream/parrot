package io.parrot.config;

import java.net.URL;
import java.util.Properties;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.util.PropertyFileUtils;

/**
 * Default properties are store in file Default.properties, in the META-INF directory.
 * 
 * @author Matteo Capitanio (matteo.capitanio@gmail.com)
 *
 */

@Exclude
public class DefaultConfigSource extends PriorityConfigSource {

	private String fileName;

	protected DefaultConfigSource(URL properyFile) {
		super(loadProperties(properyFile), LOWEST_PRIORITY);
		this.fileName = properyFile.toExternalForm();
	}

	@Override
	public String getConfigName() {
		return fileName;
	}

	private static Properties loadProperties(URL properyFile) {
		return PropertyFileUtils.loadProperties(properyFile);
	}

}
