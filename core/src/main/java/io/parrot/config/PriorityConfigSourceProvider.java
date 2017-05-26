/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
