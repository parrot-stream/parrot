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
package io.parrot.kafka.connect.transforms;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

public class DebeziumTransformConfig extends AbstractConfig {

  interface CfgKeys {
  }

  interface CfgDisplays {
  }

  interface CfgTips {
  }

  static ConfigDef baseConfigDef() {
    final ConfigDef configDef = new ConfigDef();
    addConfigs(configDef);
    return configDef;
  }

  static void addConfigs(ConfigDef configDef) {
    final String group = "Transforms";
    int order = 0;

  }

  static final ConfigDef CONFIG = baseConfigDef();

  public DebeziumTransformConfig(Map<String, ?> configProperties) {
    super(CONFIG, configProperties);
  }
  
}