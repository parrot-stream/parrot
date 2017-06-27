package io.parrot.processor.sink;

import io.parrot.api.model.ParrotSinkConfigurationApi;

public class SinkConfiguration {

	ParrotSinkConfigurationApi configurations;

	public SinkConfiguration(ParrotSinkConfigurationApi pConfigurations) {
		configurations = pConfigurations;
	}

	ParrotSinkConfigurationApi getConfigurations() {
		return configurations;
	}

	protected String getConfigurationValue(String pName, String pDefault) {
		if (!getConfigurations().containsKey(pName)) {
			return pDefault;
		}
		return getConfigurations().get(pName);
	}

}