package io.github.emattheis.quarkus.issues;


import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.eclipse.microprofile.config.inject.ConfigProperty;


@ApplicationScoped
public class GreetingService
{
	@Inject
	Client client;

	@Inject
	@ConfigProperty(name = "serviceEndpoint", defaultValue = "https://quarkus.io")
	Provider<String> serviceEndpoint;

	private volatile WebTarget serviceTarget;

	@PostConstruct
	void configure()
	{
		serviceTarget = client.target(serviceEndpoint.get());
	}

	public Stream<String> getGreetings()
	{
		serviceTarget.request().head().close();

		return Stream.of("Hello, Quarkus!");
	}
}
