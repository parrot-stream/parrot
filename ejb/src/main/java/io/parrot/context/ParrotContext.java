package io.parrot.context;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.cdi.CdiCamelContext;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.properties.DefaultPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.properties.PropertiesParser;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;

import io.parrot.exception.ParrotException;
import io.parrot.utils.ParrotLogFormatter;

@ApplicationScoped
@Startup
@Default
@ContextName("parrot-context")
public class ParrotContext extends CdiCamelContext {

	@Inject
	private Logger LOG;

	@Inject
	@ConfigProperty(name = IParrotConfigProperties.P_SOURCE_DATABASES, defaultValue = "ND")
	private String parrotSourceDatabases;

	@PostConstruct
	void init() {

		LOG.info(ParrotLogFormatter.formatLog("Parrot Configuration", "Camel Context Name", getName(),
				"Source Databases", parrotSourceDatabases.split(",")));

		try {
			setAutoStartup(true);
		} catch (Exception e) {
			throw new ParrotException(e.getMessage());
		}
	}

	/*
	 * Definisce il "PropertiesComponent" bean che Camel utilizza per fare il
	 * lookup delle Properties
	 */
	@Produces
	@ApplicationScoped
	@Named("properties")
	public PropertiesComponent properties(PropertiesParser parser) {
		PropertiesComponent component = new PropertiesComponent();
		/*
		 * Per dire a Camel di utilizzare DeltaSpike come configurazione per
		 * Camel CDI
		 */
		component.setPropertiesParser(parser);
		return component;
	}

	/*
	 * PropertiesParser che DeltaSpike usa per risolvere le properties.
	 */
	static class DeltaSpikeParser extends DefaultPropertiesParser {
		@Override
		public String parseProperty(String key, String value, Properties properties) {
			return ConfigResolver.getPropertyValue(key);
		}
	}

	@PreDestroy
	void cleanUp() {
	}

	public String getParrotSchemas() {
		return parrotSourceDatabases;
	}

}