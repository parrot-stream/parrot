package io.parrot.connect.kudu;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.util.ParrotHelper;

public class KuduSinkConnector extends SinkConnector {

    static Logger LOG = LoggerFactory.getLogger(KuduSinkConnector.class);
    KuduSinkConnectorConfig config;

    @Override
    public ConfigDef config() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start(Map<String, String> parsedConfig) {
        config = new KuduSinkConnectorConfig(parsedConfig);
    }

    @Override
    public void stop() {
    }

    @Override
    public Class<? extends Task> taskClass() {
        return KuduSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        LOG.debug("Generating task configurations for {} tasks", maxTasks);
        return Collections.nCopies(maxTasks, config.originalsStrings());
    }

    @Override
    public String version() {
        return ParrotHelper.getVersion();
    }

}
