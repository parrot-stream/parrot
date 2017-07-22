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

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.ConfigKey;
import org.apache.kafka.common.config.ConfigDef.Width;

import io.parrot.kafka.connect.jdbc.hive.HiveConfig;
import io.parrot.kafka.connect.jdbc.impala.ImpalaConfig;

public class KuduSinkConnectorConfig extends AbstractConfig {

  ImpalaConfig impalaConfig;
  HiveConfig hiveConfig;
  
  interface CfgKeys {
    String KUDU_MASTER = "kudu.master";
    String KUDU_NUM_BUCKETS = "kudu.num.buckets";
  }

  interface CfgDisplays {
    String KUDU_MASTER = "Kudu Master";
    String KUDU_NUM_BUCKETS = "Kudu number of buckets";
  }

  interface CfgTips {
    String KUDU_MASTER = "Hostname and Port of the Kudu Master (es. kudu_server:7051)";
    String KUDU_NUM_BUCKETS = "Kudu number of Buckets";
  }

  public KuduSinkConnectorConfig(Map<String, String> configProperties) {
    super(CONFIG, configProperties);
    impalaConfig = new ImpalaConfig(configProperties);
    hiveConfig = new HiveConfig(configProperties);
  }
  
  static final ConfigDef CONFIG = initConfigDef();
  
  
  static ConfigDef initConfigDef() {
    ConfigDef configDef = new ConfigDef();
    addConfigs(configDef, ImpalaConfig.CONFIG);
    addConfigs(configDef, HiveConfig.CONFIG);
    addConnectorConfigs(configDef);    
    return configDef;
  }
  
  static void addConfigs(ConfigDef configDef, ConfigDef configDefToAdd) {
    for (ConfigKey k: configDefToAdd.configKeys().values()) {
      configDef.define(k);
    }
  }
  
  static void addConnectorConfigs(ConfigDef configDef) {
    String group = "Connector";
    int order = 0;
    configDef.define(
        CfgKeys.KUDU_MASTER,
        ConfigDef.Type.STRING,
        ConfigDef.NO_DEFAULT_VALUE,
        ConfigDef.Importance.HIGH,
        CfgTips.KUDU_MASTER,
        group,
        ++order,
        Width.LONG,
        CfgDisplays.KUDU_MASTER
    ).define(
        CfgKeys.KUDU_NUM_BUCKETS,
        ConfigDef.Type.INT,
        2,
        ConfigDef.Importance.HIGH,
        CfgTips.KUDU_NUM_BUCKETS,
        group,
        ++order,
        Width.SHORT,
        CfgDisplays.KUDU_NUM_BUCKETS);
  }

  

  public String getKuduMaster() {
    return getString(CfgKeys.KUDU_MASTER);
  }

  public int getKuduNumBuckets() {
    return getInt(CfgKeys.KUDU_NUM_BUCKETS);
  }

  public ImpalaConfig impalaConfig() {
    return impalaConfig;
  }

  public HiveConfig hiveConfig() {
    return hiveConfig;
  }

}