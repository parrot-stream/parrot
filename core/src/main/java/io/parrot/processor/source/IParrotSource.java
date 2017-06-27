package io.parrot.processor.source;

import java.util.List;

import org.apache.camel.Processor;

import io.parrot.api.model.ParrotSourceApi;

public interface IParrotSource extends Processor {

	/**
	 * Gets the Kafka topics filtered according to the value of the given
	 * whitelist/blacklist.
	 * 
	 * @return A list of Kafka topics.
	 */
	public List<String> getTopics();

	/**
	 * Gets the Parrot Source configuration.
	 * 
	 * @return The Parrot Source configuration.
	 */
	public ParrotSourceApi getSourceConfig();
	
	/**
	 * Checks the Parrot Source configuration.
	 * 
	 * @return The Parrot Source configuration.
	 */
	public void checkConfiguration();

}
