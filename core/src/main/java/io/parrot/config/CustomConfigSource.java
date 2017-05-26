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
