package io.parrot.processor;

import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorApi.StatusEnum;
import io.parrot.exception.ParrotException;
import io.parrot.processor.sink.IParrotSink;
import io.parrot.processor.sink.SinkBuilder;
import io.parrot.processor.source.IParrotSource;
import io.parrot.processor.source.SourceBuilder;
import io.parrot.utils.JsonUtils;
import io.parrot.utils.ParrotLogFormatter;

public class ParrotProcessor extends RouteBuilder {

    Logger LOG;

    ParrotProcessorApi processor;
    String node;

    IParrotSource source;
    IParrotSink sink;

    public ParrotProcessor(ParrotProcessorApi pProcessor, String pNode) {
        LOG = LoggerFactory.getLogger(ParrotProcessor.class);
        processor = pProcessor;
        node = pNode;
        try {
            source = SourceBuilder.Build(processor);
            sink = SinkBuilder.Build(processor);
        } catch (ParrotException pe) {
            throw pe;
        }
    }

    @Override
    public void configure() {
        try {
            /**
             * Creates the source topic list using the information provided by
             * the Processor's Source.
             */
            List<String> topicsList = source.getTopics();
            if (topicsList.isEmpty()) {
                topicsList.add("parrot.empty.topic");
            }
            String topics = String.join(",", topicsList);
            LOG.info("Registering topics '" + topics + "' for Parrot Processor '" + processor.getId() + "'.");

            /**
             * The Kafka Consumer Client ID is composed by the:
             * 
             * <ul>
             * <li>Processor's ID</li>
             * <li>Node's ID</li>
             * </ul>
             */
            String clientProcessorID = node + "#" + processor.getId();

            /**
             * The Kafka Group ID is the Processor's ID
             */
            String kafkaGroupID = processor.getId();

            /**
             * If there are topics to process then creates the route
             */
            if (!topics.isEmpty()) {
                String kafkaEndpoint = "kafka:" + topics + "?brokers="
                        + getProcessor().getSource().getBootstrapServers() + "&clientId=" + clientProcessorID
                        + "&groupId=" + kafkaGroupID + "&autoOffsetReset=earliest&consumersCount=1&consumerStreams=1";

                boolean autoStartup = StatusEnum.ENABLED == processor.getStatus();

                from(kafkaEndpoint).id(getProcessor().getId()).autoStartup(autoStartup)
                        .convertBodyTo(SchemaAndValue.class).id("JsonToChangeEventConverter#" + processor.getId())
                        /*
                         * .idempotentConsumer(body().method("")).
                         * messageIdRepository(new KafkaIdempotentRepository(
                         * "parrot.idempotent.repository",
                         * getProcessor().getSource().getBootstrapServers()))
                         */
                        .filter(header(ParrotHeaderType.PARROT_IS_VALID.name())).bean(source)
                        .id("Source-Kafka#" + processor.getId()).bean(sink)
                        .id(sink.getSinkConfig().getId() + "#" + processor.getId())
                        .toD("${header.PARROT_SINK_TARGET_ENDPOINT}")
                        .id(sink.getSinkConfig().getId() + "-Endpoint#" + processor.getId());

                LOG.debug(ParrotLogFormatter.formatLog(
                        "Created Parrot Processor '" + processor.getId() + "' on Node '" + node + "'!", "Source Topics",
                        topics, "Source Config", "\n" + JsonUtils.objectToJson(source.getSourceConfig(), true),
                        "Sink Config", "\n" + JsonUtils.objectToJson(sink.getSinkConfig(), true)));
            } else {
                throw new ParrotException("There are no topics for Processor '" + processor.getId()
                        + "'. Check the Source configuration.");
            }
        } catch (ParrotException pe) {
            throw pe;
        } catch (Throwable e) {
            throw new ParrotException(e.getMessage());
        }
    }

    public void startup() {
        sink.startup();
    }

    public void shutdown() {
        sink.shutdown();
    }

    public ParrotProcessorApi getProcessor() {
        return processor;
    }

    public IParrotSink getSink() {
        return sink;
    }

    public void checkConfiguration() {
        source.checkConfiguration();
        sink.checkConfiguration();
    }

}
