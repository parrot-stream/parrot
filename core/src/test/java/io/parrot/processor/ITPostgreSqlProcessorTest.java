package io.parrot.processor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.client.ParrotApiClient;
import io.parrot.debezium.DebeziumApiClient;
import io.parrot.debezium.connectors.postgresql.PostgreSqlConnector;
import io.parrot.utils.JsonUtils;

@RunWith(CamelCdiRunner.class)
public class ITPostgreSqlProcessorTest extends ProcessorTest {

	@Inject
	DebeziumApiClient dbzClient;

	@Inject
	ParrotApiClient parrotClient;

	@Before
	public void initTest() {
		String jsonConnector = JsonUtils.readFromFile(JSON_PATH_CONNECTOR_POSTGTRESQL);
		String jsonProcessor = JsonUtils.readFromFile(JSON_PATH_PROCESSOR_CONFIG_POSTGRESQL_TO_HBASE);
		// Add the Debezium Connector
		try {
			dbzClient.getPostgreSqlConnector(POSTGTRESQL_CONNECTOR_NAME);
		} catch (Exception e) {
			dbzClient.addConnector(JsonUtils.jsonToObject(jsonConnector, PostgreSqlConnector.class));
		}
		// Add the Parrot Processor
		try {
			parrotClient.getProcessor(POSTGTRESQL2HBASE_PROCESSOR_NAME);
		} catch (Exception e) {
			parrotClient.addProcessor(JsonUtils.jsonToObject(jsonProcessor, ParrotProcessorApi.class));
		}
	}

	// @Ignore
	@Test
	public void testCreate() {
		// ITParrotTestSuite.insertTestCase1();
	}

	void insertTestCase1() {
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
