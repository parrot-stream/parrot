package io.parrot.processor.sink.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import io.parrot.api.model.ParrotSinkConfigurationApi;
import io.parrot.processor.sink.hive.ParrotHiveConfiguration;
import io.parrot.utils.ParrotLogFormatter;

public class ParrotHBaseConfiguration extends ParrotHiveConfiguration {

	public static final String CONF_HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";

	Configuration hbaseConfiguration;

	public ParrotHBaseConfiguration(ParrotSinkConfigurationApi pConfigurations) {
		super(pConfigurations);
		hbaseConfiguration = HBaseConfiguration.create();
		hbaseConfiguration.set(CONF_HBASE_ZOOKEEPER_QUORUM, getHbaseZooKeeperQuorum());
	}

	public String getHbaseZooKeeperQuorum() {
		return getConfigurationValue(CONF_HBASE_ZOOKEEPER_QUORUM, "localhost:2181");
	}

	public Configuration getHbaseConfiguration() {
		return hbaseConfiguration;
	}


	public String toString() {
		return ParrotLogFormatter.formatLogNoHeader(CONF_HBASE_ZOOKEEPER_QUORUM, getHbaseZooKeeperQuorum(),
				CONF_HIVE_EXTERNAL_DATABASE, getHiveDatabase(), CONF_HIVE_EXTERNAL_HOST, getHiveHost(),
				CONF_HIVE_EXTERNAL_PORT, getHivePort());
	}
}
