package io.parrot.source;

import io.parrot.api.model.ParrotSourceApi;
import io.parrot.exception.ParrotException;

public class SourceBuilder {

	public static ParrotSource Build(ParrotSourceApi pSource) {
		try {
			return (ParrotSource) Class.forName(pSource.getSourceClass()).newInstance();
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}
	}

}
