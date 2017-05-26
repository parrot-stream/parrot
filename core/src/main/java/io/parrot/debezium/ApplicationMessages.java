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
package io.parrot.debezium;

import org.apache.deltaspike.core.api.message.MessageBundle;
import org.apache.deltaspike.core.api.message.MessageTemplate;

@MessageBundle
public interface ApplicationMessages {

	@MessageTemplate("{debezium.connector.creation.error}")
	String debeziumConnectorCreationError(String connectorName, String errorMessage);

	@MessageTemplate("{debezium.connector.deletion.error}")
	String debeziumConnectorDeletionError(String connectorName, String errorMessage);

	@MessageTemplate("{debezium.connector.status.error}")
	String debeziumConnectorStatusError(String connectorName, String errorMessage);

	@MessageTemplate("{debezium.connector.restart.error}")
	String debeziumConnectorRestartError(String connectorName, String errorMessage);

	@MessageTemplate("{debezium.connector.pause.error}")
	String debeziumConnectorPauseError(String connectorName, String errorMessage);

	@MessageTemplate("{debezium.connector.resume.error}")
	String debeziumConnectorResumeError(String connectorName, String errorMessage);
}