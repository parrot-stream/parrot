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

import java.net.URL;
import java.util.Properties;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.util.PropertyFileUtils;

/**
 * Default properties are store in file Default.properties, in the META-INF
 * directory.
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
