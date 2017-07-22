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
package io.parrot.kafka.connect.kudu;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.kafka.connect.kudu.sink.KuduSinkConnector;

public class Version {

  static Logger log = LoggerFactory.getLogger(KuduSinkConnector.class);

  private static String version = "unknown";

  static {
    try {
      Properties props = new Properties();
      props.load(Version.class.getResourceAsStream("/kafka-connect-kudu-version.properties"));
      version = props.getProperty("version", version).trim();
    } catch (Exception e) {
      log.warn("Error while loading version:", e);
    }
  }

  public static String getVersion() {
    return version;
  }
}
