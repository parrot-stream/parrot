/*-
 * ============================LICENSE_START============================
 * Parrot
 * ---------------------------------------------------------------------
 * Copyright (C) 2017 Parrot
 * ---------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================LICENSE_END=============================
 */
package io.parrot.kafka.connect.kudu.sink;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.kafka.connect.kudu.Version;
import io.parrot.util.ParrotHelper;

public class KuduSinkConnector extends SinkConnector {

  static Logger logger = LoggerFactory.getLogger(KuduSinkConnector.class);

  KuduSinkConnectorConfig config;

  @Override
  public ConfigDef config() {
    return KuduSinkConnectorConfig.CONFIG;
  }

  @Override
  public void start(Map<String, String> configProperties) {
    try {
      logger.info("\n\n" + ParrotHelper.readAsciiHeader("parrot-kudu-ascii.txt") + "\n\n");
      config = new KuduSinkConnectorConfig(configProperties);
    } catch (ConfigException e) {
      throw new ConnectException("Couldn't start Parrot KuduSinkConnector due to configuration error", e);
    }
  }

  @Override
  public void stop() {
  }

  @Override
  public Class<? extends Task> taskClass() {
    return KuduSinkTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    logger.debug("Generating task configurations for {} tasks", maxTasks);
    return Collections.nCopies(maxTasks, config.originalsStrings());
  }

  @Override
  public String version() {
    return Version.getVersion();
  }

}
