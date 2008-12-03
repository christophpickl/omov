package at.phudy.service;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ISomeService.class).to(SomeService.class);
	}

}
