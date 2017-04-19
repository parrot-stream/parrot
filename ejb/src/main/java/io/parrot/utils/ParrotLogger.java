package io.parrot.utils;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class ParrotLogger {

	@Produces
	@Default
	public Logger produceLogger(InjectionPoint point) {
		return LoggerFactory.getLogger(point.getMember().getDeclaringClass().getName());
	}

}
