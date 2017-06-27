package io.parrot.processor.source;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;

import javax.enterprise.inject.spi.CDI;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.data.SchemaUtil;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotSourceApi;
import io.parrot.data.ParrotData;
import io.parrot.data.ParrotField;
import io.parrot.data.reader.ChangeEventKeyReader;
import io.parrot.data.reader.ChangeEventValueReader;
import io.parrot.exception.ParrotException;
import io.parrot.processor.ParrotHeaderType;
import io.parrot.processor.ParrotMeta;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotHelper;
import io.parrot.utils.ParrotLogFormatter;

public abstract class ParrotSource implements Processor, IParrotSource {

    protected ParrotProcessorApi processor;

    Logger LOG;
    JsonConverter jsonConverter;
    ApplicationMessages message;

    public ParrotSource(ParrotProcessorApi pProcessor) {
        processor = pProcessor;
        LOG = LoggerFactory.getLogger(getClass());

        jsonConverter = new JsonConverter();
        jsonConverter.configure(new HashMap<String, Object>(), false);
        message = CDI.current().select(ApplicationMessages.class).get();
    }

    @Override
    @Handler
    public void process(Exchange exchange) {
        String jsonKey = (String) exchange.getIn().getHeader(KafkaConstants.KEY);
        SchemaAndValue key = jsonConverter.toConnectData("", jsonKey.getBytes());
        SchemaAndValue value = (SchemaAndValue) exchange.getIn().getBody();

        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceDebeziumKeySchemaInfo(),
                JsonUtils.prettyPrint(SchemaUtil.asString(key.schema()))));
        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceDebeziumKeyPayloadInfo(),
                JsonUtils.prettyPrint(SchemaUtil.asString(key.value()))));
        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceDebeziumValueSchemaInfo(),
                JsonUtils.prettyPrint(SchemaUtil.asString(value.schema()))));
        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceDebeziumValuePayloadInfo(),
                JsonUtils.prettyPrint(SchemaUtil.asString(value.value()))));

        ChangeEventKeyReader keyReader = new ChangeEventKeyReader(key);
        ChangeEventValueReader valueReader = new ChangeEventValueReader(value);

        /**
         * Fills the common part of Parrot Metadata
         */
        ParrotData parrotData = new ParrotData(keyReader.toParrotKey(), valueReader.toParrotValue());
        ParrotMeta parrotMeta = new ParrotMeta();

        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceParrotKeyInfo(),
                JsonUtils.prettyPrint(parrotData.key().toString())));
        LOG.debug(ParrotLogFormatter.formatLog(message.parrotProcessorSourceParrotValueInfo(),
                JsonUtils.prettyPrint(parrotData.value().toString())));

        parrotMeta.addMeta(ParrotMetaType.PARROT_CHANGE_TYPE,
                new ParrotField(ParrotMetaType.PARROT_CHANGE_TYPE.name(), valueReader.getOperationType().code(),
                        SchemaBuilder.string().name(ParrotMetaType.PARROT_CHANGE_TYPE.name())));
        parrotMeta.addMeta(ParrotMetaType.PARROT_SOURCE_NAME, new ParrotField(ParrotMetaType.PARROT_SOURCE_NAME.name(),
                valueReader.getSourceName(), SchemaBuilder.string().name(ParrotMetaType.PARROT_SOURCE_NAME.name())));
        parrotMeta.addMeta(ParrotMetaType.PARROT_DEBEZIUM_TIMESTAMP,
                new ParrotField(ParrotMetaType.PARROT_DEBEZIUM_TIMESTAMP.name(), valueReader.getDebeziumTimestamp(),
                        SchemaBuilder.int64().optional().name(ParrotMetaType.PARROT_DEBEZIUM_TIMESTAMP.name()), true));
        parrotMeta.addMeta(ParrotMetaType.PARROT_PROCESSOR_TIMESTAMP,
                new ParrotField(ParrotMetaType.PARROT_PROCESSOR_TIMESTAMP.name(), Instant.now().toEpochMilli(),
                        SchemaBuilder.int64().name(ParrotMetaType.PARROT_PROCESSOR_TIMESTAMP.name()), true));

        /**
         * Fills Headers
         */
        exchange.getIn().setHeader(ParrotHeaderType.PARROT_SOURCE_NAME.name(),
                ParrotHelper.getSourceName(value.schema().name()));
        exchange.getIn().setHeader(ParrotHeaderType.PARROT_META.name(), parrotMeta);
        exchange.getIn().setHeader(ParrotHeaderType.PARROT_CHANGE_EVENT_KEY.name(), key);
        exchange.getIn().setHeader(ParrotHeaderType.PARROT_CHANGE_EVENT_VALUE.name(), value);
        exchange.getIn().setHeader(ParrotHeaderType.PARROT_DATA.name(), parrotData);
    }

    @Override
    public ParrotSourceApi getSourceConfig() {
        return processor.getSource();
    }

    @Override
    public void checkConfiguration() {
        String[] kafkaHostsArray = getSourceConfig().getBootstrapServers().split(",");
        for (String kafkaHost : kafkaHostsArray) {
            try {
                ParrotHelper.checkConnection(kafkaHost);
            } catch (Exception e) {
                throw new ParrotException(message.parrotProcessorSourceKafkaError(processor.getId(), e.getMessage()));
            }
        }
    }
}