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
package io.parrot.kafka.connect.jdbc.impala;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Range;
import org.apache.kafka.common.config.ConfigDef.Width;

import io.parrot.kafka.connect.recommenders.BooleanParentRecommender;
import io.parrot.kafka.connect.validators.NotEmptyString;

public class ImpalaConfig extends AbstractConfig {

  static final ConfigDef.Recommender IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER = new BooleanParentRecommender(
      Keys.IMPALA_INTEGRATION);
  
  static final NotEmptyString NOT_EMPTY_STRING_VALIDATOR = new NotEmptyString();

  interface Keys {
    String IMPALA_INTEGRATION = "impala.integration";
    String IMPALA_HOSTNAME = "impala.hostname";
    String IMPALA_PORT = "impala.port";
    String IMPALA_DATABASE = "impala.database";
    String IMPALA_USERNAME = "impala.username";
    String IMPALA_PASSWORD = "impala.password";
  }

  interface Displays {
    String IMPALA_INTEGRATION = "Impala integration active?";
    String IMPALA_HOSTNAME = "Impala hostname";
    String IMPALA_PORT = "Impala port";
    String IMPALA_DATABASE = "Impala database name";
    String IMPALA_USERNAME = "Impala username";
    String IMPALA_PASSWORD = "Impala password";
  }

  interface Docs {
    String IMPALA_INTEGRATION = "Configuration indicating whether to integrate with Impala when running the connector";
    String IMPALA_HOSTNAME = "Hostname of a running hiveserver2-frontend process (server of impalad process)";
    String IMPALA_PORT = "Port of a running hiveserver2-frontend process";
    String IMPALA_DATABASE = "Impala database name";
    String IMPALA_USERNAME = "Impala username";
    String IMPALA_PASSWORD = "Impala password";
  }

  public static final ConfigDef CONFIG = initConfigDef();

  static ConfigDef initConfigDef() {
    ConfigDef configDef = new ConfigDef();
    addConfigs(configDef);
    return configDef;
  }

  static void addConfigs(ConfigDef configDef) {
    String group = "Impala";
    int order = 0;
    configDef.define(
        Keys.IMPALA_INTEGRATION,
        ConfigDef.Type.BOOLEAN,
        false,
        ConfigDef.Importance.HIGH,
        Docs.IMPALA_INTEGRATION,
        group, ++order,
        Width.LONG,
        Displays.IMPALA_INTEGRATION,
        Arrays.asList(ImpalaConfig.Keys.IMPALA_HOSTNAME,
                      ImpalaConfig.Keys.IMPALA_PORT,
                      ImpalaConfig.Keys.IMPALA_DATABASE,
                      ImpalaConfig.Keys.IMPALA_USERNAME,
                      ImpalaConfig.Keys.IMPALA_PASSWORD)
    ).define(
        Keys.IMPALA_HOSTNAME,
        ConfigDef.Type.STRING,
        "localhost",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.HIGH,
        Docs.IMPALA_HOSTNAME,
        group,
        ++order,
        Width.LONG,
        Displays.IMPALA_HOSTNAME,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
    ).define(
        Keys.IMPALA_PORT,
        ConfigDef.Type.INT,
        21050,
        Range.between(1025, 65535),
        ConfigDef.Importance.HIGH,
        Docs.IMPALA_PORT,
        group,
        ++order,
        Width.SHORT,
        Displays.IMPALA_PORT,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(
        Keys.IMPALA_DATABASE,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.HIGH,
        Docs.IMPALA_DATABASE,
        group,
        ++order,
        Width.MEDIUM,
        Displays.IMPALA_DATABASE,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(Keys.IMPALA_USERNAME,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.MEDIUM,
        Docs.IMPALA_USERNAME,
        group,
        ++order,
        Width.MEDIUM,
        Displays.IMPALA_USERNAME,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER
     ).define(
        Keys.IMPALA_PASSWORD,
        ConfigDef.Type.STRING,
        "parrot",
        NOT_EMPTY_STRING_VALIDATOR,
        ConfigDef.Importance.MEDIUM,
        Docs.IMPALA_PASSWORD,
        group,
        ++order,
        Width.MEDIUM,
        Displays.IMPALA_PASSWORD,
        IMPALA_INTEGRATION_DEPENDENTS_RECOMMENDER);
  }

  public ImpalaConfig(Map<String, String> configProperties) {
    super(CONFIG, configProperties);
  }

  public Boolean isImpalaIntegrated() {
    return getBoolean(Keys.IMPALA_INTEGRATION);
  }

  public String getImpalaHostname() {
    return getString(Keys.IMPALA_HOSTNAME);
  }

  public Integer getImpalaPort() {
    return getInt(Keys.IMPALA_PORT);
  }

  public String getImpalaDatabase() {
    return getString(Keys.IMPALA_DATABASE);
  }

  public String getImpalaUserName() {
    return getString(Keys.IMPALA_USERNAME);
  }

  public String getImpalaPassword() {
    return getString(Keys.IMPALA_PASSWORD);
  }

}
