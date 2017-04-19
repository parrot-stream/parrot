package io.parrot.camel.routes;


import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

@ContextName("parrot-context")
public class EventInKafkaDequeuerRoute extends RouteBuilder {

	@EndpointInject(uri = "kafka:{{parrot.kafka.hostname}}:{{parrot.kafka.port}}?clientId=parrot.dequeue&consumerId=parrot.dequeue&groupId=parrot&topic=parrot&autoOffsetReset=earliest&consumersCount=1&consumerStreams=1")
	private Endpoint subscribeTopicsInputEndpoint;

	@Override
	public void configure() {

		/**
		 * Definisce la route come:
		 * 
		 * <ul>
		 * <li>legge dai topic Kafka di sottoscrizione</li>
		 * <li>memorizza l'uuid nell'idempotent table (policy skip)</li>
		 * <li></li>
		 * <ul>
		 */
		from(subscribeTopicsInputEndpoint).to("mock:test");
	}

}