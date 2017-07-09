package io.parrot.connect.kudu;

import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.kudu.client.KuduClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.util.ParrotHelper;

public class KuduSinkTask extends SinkTask {

    static Logger LOG = LoggerFactory.getLogger(KuduSinkTask.class);

    KuduClient kuduClient;
    
    @Override
    public String version() {
        return ParrotHelper.getVersion();
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecord) {
       LOG.info(sinkRecord.toString());
    }

    @Override
    public void start(Map<String, String> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
