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
package io.parrot.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;

public class ParrotHelper {

  public static String readAsciiHeader(String resourceName) {
    InputStream is = ParrotHelper.class.getClassLoader().getResourceAsStream(resourceName);
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    try {
      while ((length = is.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      return result.toString("UTF-8");
    } catch (Exception e) {
      return "";
    }
  }

  public static String formatBigDecimal(Schema fieldSchema, Object value) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(Integer.parseInt(fieldSchema.parameters().get(Decimal.SCALE_FIELD)));
    df.setMinimumFractionDigits(df.getMaximumFractionDigits());
    df.setGroupingUsed(false);
    return df.format(value);
  }

}
