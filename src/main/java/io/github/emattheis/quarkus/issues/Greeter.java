package io.github.emattheis.quarkus.issues;


import java.io.IOException;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.microprofile.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Greeter
implements StreamingOutput
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final GreetingService service;

	private final UriBuilder serviceEndpointBuilder;

	public Greeter(GreetingService service, Config config)
	{
		this.service = service;
		serviceEndpointBuilder = UriBuilder.fromUri(config.getOptionalValue("serviceEndpoint", String.class).orElse("https://quarkus.io"));
	}

	@Override
	public void write(OutputStream out)
	throws IOException, WebApplicationException
	{
		log.info("sending HEAD request to quarkus.io");
		ClientBuilder.newClient().target("https://quarkus.io").request().head().close();
		
		try (JsonGenerator json = Json.createGenerator(out))
		{
			log.info("fetching greetings");
			new Greetings(serviceEndpointBuilder.build(), service.getGreetings()).writeTo(json);
		}
	}
}
