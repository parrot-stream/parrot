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
