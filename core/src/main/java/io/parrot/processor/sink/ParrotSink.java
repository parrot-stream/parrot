package io.parrot.processor.sink;

import javax.enterprise.inject.spi.CDI;

import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotSinkApi;

public abstract class ParrotSink implements Processor, IParrotSink {

    protected Logger LOG;
    protected ApplicationMessages message;

    ParrotProcessorApi processor;

    Component sinkComponent;
    String componentName;

    SinkConfiguration configuration;

    public ParrotSink(ParrotProcessorApi pProcessor) {
        super();
        processor = pProcessor;
        LOG = LoggerFactory.getLogger(getClass());
        message = CDI.current().select(ApplicationMessages.class).get();
    }

    @Override
    @Handler
    public void process(Exchange exchange) {

    }

    @Override
    public ParrotSinkApi getSinkConfig() {
        return processor.getSink();
    }

    @Override
    public Component getComponent() {
        return sinkComponent;
    }

    public void setSinkComponent(Component sinkComponent) {
        this.sinkComponent = sinkComponent;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public SinkConfiguration getConfiguration() {
        return configuration;
    }

    public ParrotProcessorApi getProcessor() {
        return processor;
    }

    public void setProcessor(ParrotProcessorApi processor) {
        this.processor = processor;
    }

    protected void setConfiguration(SinkConfiguration configuration) {
        this.configuration = configuration;
    }

}