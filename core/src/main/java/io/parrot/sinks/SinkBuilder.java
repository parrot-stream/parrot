package io.parrot.sinks;

import io.parrot.api.model.ParrotSinkApi;
import io.parrot.exception.ParrotException;

public class SinkBuilder {

	public static ParrotSink Build(ParrotSinkApi pSink) {
		try {
			return (ParrotSink) Class.forName(pSink.getSinkClass()).newInstance();
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}
	}

}
