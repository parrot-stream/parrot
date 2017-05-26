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
package io.parrot.zookeeper;

import static io.parrot.utils.JsonUtils.jsonToObject;
import static io.parrot.utils.JsonUtils.readFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.cdi.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import io.parrot.ParrotBaseTest;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.zookeeper.path.AboutPath;
import io.parrot.zookeeper.path.ParrotProcessorConfigPath;

@RunWith(CamelCdiRunner.class)
public class ITParrotZkClientTest extends ParrotBaseTest {

	@Inject
	ParrotZkClient zkClient;

	@Inject
	Logger LOG;

	@Ignore
	@Test
	@Order(0)
	public void getProcessors() {
		try {

			List<ParrotProcessorConfigPath> processors = zkClient.getProcessors();
			assertEquals(0, processors.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	// @Ignore
	@Test
	@Order(1)
	public void testCreateAbout() {
		try {
			List<String> mantainers = new ArrayList<String>();
			mantainers.add("Matteo Capitanio <matteo.capitanio@gmail.com>");
			zkClient.createAbout(new AboutPath("Apache Parrot", "0.1.0", mantainers));
			AboutPath aboutPath = zkClient.getAbout();
			assertNotNull(aboutPath);
			assertEquals("Apache Parrot", aboutPath.getProduct());
			assertEquals("0.1.0", aboutPath.getVersion());
			assertEquals("Matteo Capitanio <matteo.capitanio@gmail.com>", mantainers.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	@Order(2)
	public void addProcessorConfigPostgresqlToHive() {
		try {
			String json = readFromFile(JSON_PATH_PROCESSOR_CONFIG_POSTGRESQL_TO_HBASE);
			ParrotProcessorApi processor = jsonToObject(json, ParrotProcessorApi.class);

			// Get the newly created Processor
			zkClient.addProcessor(processor);

			ParrotProcessorConfigPath processorZkPath = zkClient.getProcessor(processor.getId());
			processor = processorZkPath.getProcessorApi();

			// Assertions
			assertEquals(PROCESSOR_ID_1, processor.getId());
			assertEquals(SOURCE_BOOTRSTRAP_SERVERS, processor.getSource().getBootstrapServers());
			assertEquals(SOURCE_LOGICAL_NAME, processor.getSource().getLogicalName());
			assertEquals(SOURCE_TABLE_WHITELIST, processor.getSource().getTableWhitelist());
			assertEquals(SINK_ID_1, processor.getSink().getId());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	// @Ignore
	@Test
	@Order(4)
	public void deleteProcessorConfigPostgresqlToHive() {
		try {
			// Add a new Processor
			String json = readFromFile(JSON_PATH_PROCESSOR_CONFIG_POSTGRESQL_TO_HBASE);
			ParrotProcessorApi processor = jsonToObject(json, ParrotProcessorApi.class);

			zkClient.deleteProcessor(processor.getId());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}