package io.parrot.processor.sink;

import org.apache.camel.Component;
import org.apache.camel.Processor;

import io.parrot.api.model.ParrotSinkApi;

public interface IParrotSink extends Processor {

	public ParrotSinkApi getSinkConfig();

	public Component getComponent();

	public String getComponentName();

	public void checkConfiguration();
	
	public SinkConfiguration getConfiguration();
	
	public void shutdown();
	public void startup();
}
