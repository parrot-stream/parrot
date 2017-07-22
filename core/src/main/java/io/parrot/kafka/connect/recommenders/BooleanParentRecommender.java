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
package io.parrot.kafka.connect.recommenders;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.kafka.connect.validators.NotEmptyString;

public class BooleanParentRecommender implements ConfigDef.Recommender {

  Logger logger = LoggerFactory.getLogger(NotEmptyString.class);

  protected String parentConfigName;

  public BooleanParentRecommender(String parentConfigName) {
    this.parentConfigName = parentConfigName;
  }

  @Override
  public List<Object> validValues(String name, Map<String, Object> connectorConfigs) {
    return new LinkedList<>();
  }

  @Override
  public boolean visible(String name, Map<String, Object> connectorConfigs) {
    boolean isVisible = (Boolean) connectorConfigs.get(parentConfigName);
    logger.info("\n\nVISIBLE: {}", isVisible);
    return isVisible;
  }

}
