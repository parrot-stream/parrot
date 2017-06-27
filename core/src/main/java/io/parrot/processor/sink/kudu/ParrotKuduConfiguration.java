package io.parrot.processor.sink.kudu;

import io.parrot.api.model.ParrotSinkConfigurationApi;
import io.parrot.processor.sink.impala.ParrotImpalaConfiguration;
import io.parrot.utils.ParrotLogFormatter;

public class ParrotKuduConfiguration extends ParrotImpalaConfiguration {

    public static final String CONF_KUDU_MASTER = "kudu.master";
    public static final String CONF_KUDU_NUM_BUCKETS = "kudu.num.buckets";

    public ParrotKuduConfiguration(ParrotSinkConfigurationApi pConfigurations) {
        super(pConfigurations);
    }

    public String getKuduMaster() {
        return getConfigurationValue(CONF_KUDU_MASTER, "localhost:7051");
    }

    public int getNumBuckets() {
        return Integer.parseInt(getConfigurationValue(CONF_KUDU_NUM_BUCKETS, "2"));
    }

    public String toString() {
        return ParrotLogFormatter.formatLogNoHeader(CONF_KUDU_MASTER, getKuduMaster(), CONF_KUDU_NUM_BUCKETS,
                getNumBuckets(), CONF_IMPALA_EXTERNAL_DATABASE, getImpalaDatabase(), CONF_IMPALA_EXTERNAL_HOST,
                getImpalaHost(), CONF_IMPALA_EXTERNAL_PORT, getImpalaPort());
    }
}
