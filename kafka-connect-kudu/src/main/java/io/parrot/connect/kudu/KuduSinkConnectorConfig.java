package io.parrot.connect.kudu;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

public class KuduSinkConnectorConfig extends AbstractConfig {

    enum CfgKeys {
        ;
        static final String KUDU_MASTER = "kudu.master";
        static final String KUDU_NUM_BUCKETS = "kudu.num.buckets";
    }

    enum CfgTips {
        ;
        static final String KUDU_MASTER = "Hostname an Port of the Kudu Master (es. kudu_server:7051)";
        static final String KUDU_NUM_BUCKETS = "Number of Kudu Buckets";
    }

    static final ConfigDef configDef = new ConfigDef().define(CfgKeys.KUDU_MASTER, ConfigDef.Type.STRING,
            ConfigDef.NO_DEFAULT_VALUE, ConfigDef.Importance.HIGH, CfgTips.KUDU_MASTER).define(CfgKeys.KUDU_NUM_BUCKETS,
                    ConfigDef.Type.INT, 1, ConfigDef.Importance.HIGH, CfgTips.KUDU_NUM_BUCKETS);

    public static ConfigDef conf() {
        return configDef;
    }

    public String getKuduMaster() {
        return getString(CfgKeys.KUDU_MASTER);
    }

    public int getKuduNumBuckets() {
        return getInt(CfgKeys.KUDU_NUM_BUCKETS);
    }

    public KuduSinkConnectorConfig(ConfigDef definition, Map<?, ?> originals) {
        super(definition, originals);
    }

    public KuduSinkConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }
}
