package io.github.emattheis.quarkus.issues;


import java.net.URI;
import java.util.stream.Stream;

import javax.json.stream.JsonGenerator;


public class Greetings
{
	private final URI source;

	private final Stream<String> greetings;

	public Greetings(URI source, Stream<String> greetings)
	{
		this.source = source;
		this.greetings = greetings;
	}

	public void writeTo(JsonGenerator json)
	{
		json.writeStartObject()
		    .write("source", source.toString())
		    .writeStartArray("greetings");

		greetings.forEach(json::write);

		json.writeEnd()
		    .writeEnd();
	}
}
