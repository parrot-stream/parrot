package io.parrot.processor.sink;

import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.exception.ParrotException;

public class SinkBuilder {

	public static IParrotSink Build(ParrotProcessorApi pProcessor) {
		try {
			return (IParrotSink) Class.forName(pProcessor.getSink().getSinkClass()).getConstructor(ParrotProcessorApi.class)
					.newInstance(pProcessor);
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}
	}

}
