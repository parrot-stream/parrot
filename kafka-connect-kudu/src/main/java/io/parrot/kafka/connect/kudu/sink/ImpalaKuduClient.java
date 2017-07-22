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

import io.parrot.kafka.connect.jdbc.impala.ImpalaClient;
import io.parrot.kafka.connect.jdbc.impala.ImpalaConfig;

public class ImpalaKuduClient extends ImpalaClient {
  
  public ImpalaKuduClient(ImpalaConfig config) {
    super(config);
  }

  @Override
  public void createTable(String tableName) {
    String storedBy = " STORED AS KUDU TBLPROPERTIES ('kudu.table_name' = '" + tableName + "')";
    String createTableStatement = "CREATE EXTERNAL TABLE " + config().getImpalaDatabase() + "."
        + tableName.replaceAll("\\.", "_") + storedBy;
    executeUpdate(createTableStatement);
  }
  
}
