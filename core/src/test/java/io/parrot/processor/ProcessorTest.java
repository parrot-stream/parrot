package io.parrot.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.BeforeClass;

import io.parrot.ParrotBaseTest;

public class ProcessorTest extends ParrotBaseTest {

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
}
