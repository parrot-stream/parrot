/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.parrot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import io.parrot.debezium.event.ITDebeziumPostgreSqlChangeEventReaderTest;

@RunWith(Suite.class)
/*
 * @Suite.SuiteClasses({ DebeziumMySqlChangeEventReaderTest.class,
 * ITDebeziumPostgreSqlChangeEventReaderTest.class, ITParrotZkClientTest.class,
 * ITKafkaUtilsTest.class})
 */
// @Suite.SuiteClasses({ ITParrotZkClientTest.class })
// @Suite.SuiteClasses({ ITDebeziumClientTest.class })
@Suite.SuiteClasses({ ITDebeziumPostgreSqlChangeEventReaderTest.class })
// @Suite.SuiteClasses({ ITPostgreSqlProcessorTest.class })
public class ITParrotTestSuite {

	static Connection pgConnection;

	@BeforeClass
	public static void init() {
		String url = "jdbc:postgresql://localhost:5432/parrot";
		Properties props = new Properties();
		props.setProperty("user", "parrot");
		props.setProperty("password", "parrot");
		try {
			pgConnection = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertTestCase1() {
		try {
			PreparedStatement ps = pgConnection.prepareStatement(
					"INSERT INTO parrot.table_with_simple_pk(n_smallint, n_integer, n_bigint, n_real, n_double) VALUES(?,?,?,?,?)");
			ps.setInt(1, 10);
			ps.setInt(2, 1000);
			ps.setLong(3, new Long(999999999));
			// ps.setBigDecimal(4, new BigDecimal(1.99999999));
			// ps.setBigDecimal(5, new BigDecimal(2.11111111));
			ps.setFloat(4, new Float(9.1212121212121212121212));
			ps.setDouble(5, new Double(10.91919191919191));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}