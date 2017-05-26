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
package io.parrot.source;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;

import io.parrot.debezium.event.data.ChangeEventData;
import io.parrot.utils.JsonUtils;

public class PostgreSqlSource implements ParrotSource {

	@Inject
	Logger LOG;
	
	@Override
	@Handler
	public void process(Exchange exchange) {
		String jsonKey = (String) exchange.getIn().getHeader(KafkaConstants.KEY);
		String jsonValue = (String) exchange.getIn().getBody();
		System.out.println("\n\nKEY: " + jsonKey+ "\n\n");
		System.out.println("\n\nVALUE: " + jsonValue+ "\n\n");
		ChangeEventData key = JsonUtils.jsonToObject(jsonKey, ChangeEventData.class);
		ChangeEventData value = JsonUtils.jsonToObject(jsonValue, ChangeEventData.class);
		LOG.debug(JsonUtils.prettyPrint(jsonKey));
		LOG.debug(JsonUtils.prettyPrint(jsonValue));
		// PostgreSqlChangeEventReader reader = new
		// PostgreSqlChangeEventReader();
	}

}