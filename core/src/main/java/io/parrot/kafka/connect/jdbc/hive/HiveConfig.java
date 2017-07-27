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
package io.parrot.kafka.connect.jdbc.hive;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Range;
import org.apache.kafka.common.config.ConfigDef.Width;

import io.parrot.kafka.connect.recommenders.BooleanParentRecommender;
import io.parrot.kafka.connect.validators.NotEmptyString;

public class HiveConfig extends AbstractConfig {

  static final ConfigDef.Recommender IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER = new BooleanParentRecommender(
      Keys.HIVE_INTEGRATION);
  
  static final NotEmptyString NOT_EMPTY_STRING_VALIDATOR = new NotEmptyString();

  interface Keys {
    String HIVE_INTEGRATION = "hive.integration";
    String HIVE_HOSTNAME = "hive.hostname";
    String HIVE_PORT = "hive.port";
    String HIVE_DATABASE = "hive.database";
    String HIVE_USERNAME = "hive.username";
    String HIVE_PASSWORD = "hive.password";
  }

  interface Displays {
    String HIVE_INTEGRATION = "Hive integration active?";
    String HIVE_HOSTNAME = "HiveServer2 hostname";
    String HIVE_PORT = "HiveServer2 port";
    String HIVE_DATABASE = "Hive database name";
    String HIVE_USERNAME = "Hive username";
    String HIVE_PASSWORD = "Hive password";
  }

  interface Docs {
    String HIVE_INTEGRATION = "Configuration indicating whether to integrate with Hive when running the connector";
    String HIVE_HOSTNAME = "Hostname of Hive server";
    String HIVE_PORT = "Port of a running HiveServer2 process";
    String HIVE_DATABASE = "Impala database name";
    String HIVE_USERNAME = "Impala username";
    String HIVE_PASSWORD = "Impala password";
  }

  public static final ConfigDef CONFIG = initConfigDef();

  static ConfigDef initConfigDef() {
    ConfigDef configDef = new ConfigDef();
    addConfigs(configDef);
    return configDef;
  }

  static void addConfigs(ConfigDef configDef) {
    String group = "Hive";
    int order = 0;
    configDef.define(
        Keys.HIVE_INTEGRATION,
        ConfigDef.Type.BOOLEAN,
        false,
        ConfigDef.Importance.HIGH,
        Docs.HIVE_INTEGRATION,
        group, ++order,
        Width.LONG,
        Displays.HIVE_INTEGRATION,
        Arrays.asList(HiveConfig.Keys.HIVE_HOSTNAME,
                      HiveConfig.Keys.HIVE_PORT,
                      HiveConfig.Keys.HIVE_DATABASE,
                      HiveConfig.Keys.HIVE_USERNAME,
                      HiveConfig.Keys.HIVE_PASSWORD)
    ).define(
        Keys.HIVE_HOSTNAME,
        ConfigDef.Type.STRING,
        "localhost",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.HIGH,
        Docs.HIVE_HOSTNAME,
        group,
        ++order,
        Width.LONG,
        Displays.HIVE_HOSTNAME,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
    ).define(
        Keys.HIVE_PORT,
        ConfigDef.Type.INT,
        10000,
        Range.between(1025, 65535),
        ConfigDef.Importance.HIGH,
        Docs.HIVE_PORT,
        group,
        ++order,
        Width.SHORT,
        Displays.HIVE_PORT,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(
        Keys.HIVE_DATABASE,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.HIGH,
        Docs.HIVE_DATABASE,
        group,
        ++order,
        Width.MEDIUM,
        Displays.HIVE_DATABASE,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(Keys.HIVE_USERNAME,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.MEDIUM,
        Docs.HIVE_USERNAME,
        group,
        ++order,
        Width.MEDIUM,
        Displays.HIVE_USERNAME,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(
        Keys.HIVE_PASSWORD,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.MEDIUM,
        Docs.HIVE_PASSWORD,
        group,
        ++order,
        Width.MEDIUM,
        Displays.HIVE_PASSWORD,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER);
  }

  public HiveConfig(Map<String, String> configProperties) {
    super(CONFIG, configProperties);
  }

  public Boolean isHiveIntegrated() {
    return getBoolean(Keys.HIVE_INTEGRATION);
  }

  public String getHiveHostname() {
    return getString(Keys.HIVE_HOSTNAME);
  }

  public Integer getHivePort() {
    return getInt(Keys.HIVE_PORT);
  }

  public String getHiveDatabase() {
    return getString(Keys.HIVE_DATABASE);
  }

  public String getHiveUserName() {
    return getString(Keys.HIVE_USERNAME);
  }

  public String getHivePassword() {
    return getString(Keys.HIVE_PASSWORD);
  }

}
