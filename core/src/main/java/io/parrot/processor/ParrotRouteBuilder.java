package io.parrot.processor;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.debezium.event.data.ChangeEventData;
import io.parrot.source.PostgreSqlSource;

public class ParrotRouteBuilder extends RouteBuilder {

	Logger LOG = LoggerFactory.getLogger(ParrotRouteBuilder.class);

	String configurationError;
	boolean configured = false;

	ParrotProcessorApi processor;

	public ParrotRouteBuilder(ParrotProcessorApi pProcessor) {
		processor = pProcessor;
	}

	@Override
	public void configure() {
		try {
			String kafkaEndpoint = "kafka:parrot_source.parrot.table_with_simple_pk?brokers="
					+ getProcessor().getSource().getBootstrapServers()
					+ "&clientId=parrot.dequeue&groupId=parrot1234&autoOffsetReset=earliest&consumersCount=1&consumerStreams=1";

			from(kafkaEndpoint).id(getProcessor().getId()).convertBodyTo(ChangeEventData.class)
					.idempotentConsumer(body().method(""))
					.messageIdRepository(new KafkaIdempotentRepository("parrot.idempotent.repository",
							getProcessor().getSource().getBootstrapServers()))
					.bean(PostgreSqlSource.class).to("stream:out");
			configured = true;
		} catch (Exception e) {
			configurationError = "Unable to create Processor's route '" + getProcessor().getId() + "': "
					+ e.getMessage();
			LOG.error(configurationError);

		}
	}

	public String getConfigurationError() {
		return configurationError;
	}

	public boolean isConfigured() {
		return configured;
	}

	public ParrotProcessorApi getProcessor() {
		return processor;
	}
}
