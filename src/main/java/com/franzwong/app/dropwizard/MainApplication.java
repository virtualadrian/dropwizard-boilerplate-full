package com.franzwong.app.dropwizard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franzwong.app.dropwizard.health.Ping;
import com.franzwong.app.dropwizard.resource.TaskResource;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MainApplication extends Application<MainConfiguration> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
	
	public static void main(String[] args) throws Exception {
		new MainApplication().run(args);
	}

	@Override
	public void run(MainConfiguration config, Environment env) throws Exception {
		env.healthChecks().register("ping", new Ping());
		
		env.jersey().register(new LoggingFilter(java.util.logging.Logger.getLogger(getClass().getName()), true));
		
		setupObjectMapper(env);
		
		List<Module> modules = createModules(config, env);
		Injector injector = Guice.createInjector(modules);
		
		registerResources(injector, env);
		
		env.servlets().addFilter("PersistFilter", injector.getInstance(PersistFilter.class)).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}

	private List<Module> createModules(MainConfiguration config, Environment env) {
		AbstractModule module = new AbstractModule() {
			@Override
			protected void configure() {
				ObjectMapper mapper = env.getObjectMapper();
				bind(ObjectMapper.class).toInstance(mapper);
			}
		};
		
		List<Module> modules = new ArrayList<>();
		modules.add(module);
		modules.add(new JpaPersistModule("manager"));
		return modules;
	}
	
	private void registerResources(Injector injector, Environment env) {
		env.jersey().register(injector.getInstance(TaskResource.class));
	}
	
	private void setupObjectMapper(Environment env) {
		ObjectMapper mapper = env.getObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
}
