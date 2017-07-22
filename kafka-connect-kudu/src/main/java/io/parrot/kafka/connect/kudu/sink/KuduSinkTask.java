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

import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.kudu.client.KuduClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.kafka.connect.kudu.Version;

public class KuduSinkTask extends SinkTask {

  static Logger log = LoggerFactory.getLogger(KuduSinkTask.class);

  KuduClient kuduClient;
  KuduSinkImpl kuduSinkImpl;
  KuduSinkConnectorConfig config;

  @Override
  public String version() {
    return Version.getVersion();
  }

  @Override
  public void put(Collection<SinkRecord> sinkRecord) {
    log.info("Sink: " + sinkRecord.toString());
    getKuduSinkImpl().upsert(sinkRecord);
  }

  @Override
  public void start(Map<String, String> configProperties) {
    log.info("Starting task...");
    config = new KuduSinkConnectorConfig(configProperties);
    
  }

  @Override
  public void stop() {
    log.info("Stopping task...");
  }

  KuduSinkImpl getKuduSinkImpl() {
    if (kuduSinkImpl == null) {
      kuduSinkImpl = new KuduSinkImpl(config);
    }
    return kuduSinkImpl;
  }

}
