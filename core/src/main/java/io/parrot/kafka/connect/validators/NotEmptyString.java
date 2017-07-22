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
package io.parrot.kafka.connect.validators;

import org.apache.kafka.common.config.ConfigDef.Validator;
import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotEmptyString implements Validator {

  Logger logger = LoggerFactory.getLogger(NotEmptyString.class);


  @Override
  public void ensureValid(String name, Object value) {
    logger.debug("Validating {} with value {}", name, value);
    String s = (String) value;
    if (s == null || s.trim().length() == 0) {
      throw new ConfigException(name, value, "String must not be empty");
    }
  }

}
