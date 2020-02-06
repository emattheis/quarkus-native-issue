package io.github.emattheis.quarkus.issues;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.microprofile.config.Config;


@Path("/hello")
@ApplicationScoped
public class Hello
{
	@Inject
	GreetingService service;

	@Inject
	Config config;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public StreamingOutput sayHello()
	{
		return new Greeter(service, config);
	}
}
