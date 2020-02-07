# quarkus-native-issue
This project demonstrates an issue with classes missing from native builds using with Quarkus and GraalVM.
## Setup
- Compiling the native targets requires GraalVM with the `native-image` tool installed [per the normal Quarkus guidelines](https://quarkus.io/guides/building-native-image).
- The project uses Maven (version 3.6.3 was used).
- I replicated the issue with both the Java 8 and Java 11 versions of [GraalVM Community Edition 19.3.1](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-19.3.1) on macOS 10.14.6.
## Working Scenario (using JAR)
### Java 11
```
$ mvn clean package
$ java -jar target/quarkus-native-issue-runner.jar
```
### Java 8
```
$ mvn -Pjava8 clean package
$ java -jar target/quarkus-native-issue-runner.jar
```
Note this warning during build, which seems benign:
```
[WARNING] [io.quarkus.deployment.steps.ReflectiveHierarchyStep] Unable to properly register the hierarchy of the following classes for reflection as they are not in the Jandex index:
	- javax.inject.Provider
	- javax.ws.rs.client.Client
	- javax.ws.rs.client.WebTarget
	- javax.ws.rs.core.StreamingOutput
	- javax.ws.rs.core.UriBuilder
	- org.slf4j.Logger
Consider adding them to the index either by creating a Jandex index for your dependency via the Maven plugin, an empty META-INF/beans.xml or quarkus.index-dependency properties.");.
```
Once quarkus is running, point a web browser to http://localhost:8080/hello and you should get the following JSON:
```
{"source":"https://quarkus.io","greetings":["Hello, Quarkus!"]}
```
Use `CTRL-C` (or equivalent) to stop Quarkus and the complete console output should look something like this:
```
2020-02-06 23:01:00,872 INFO  [io.quarkus] (main) quarkus-native-issue 1.0.0-SNAPSHOT (running on Quarkus 1.2.0.Final) started in 0.872s. Listening on: http://0.0.0.0:8080
2020-02-06 23:01:00,886 INFO  [io.quarkus] (main) Profile prod activated. 
2020-02-06 23:01:00,887 INFO  [io.quarkus] (main) Installed features: [cdi, rest-client, resteasy, resteasy-jsonb, smallrye-health]
2020-02-06 23:01:08,001 INFO  [io.git.ema.qua.iss.Greeter] (executor-thread-1) fetching greetings
2020-02-06 23:01:08,204 INFO  [io.git.ema.qua.iss.LoggingFilter] (executor-thread-1) >> HEAD https://quarkus.io
2020-02-06 23:01:08,403 INFO  [io.git.ema.qua.iss.LoggingFilter] (executor-thread-1) << 200 OK
2020-02-06 23:01:14,863 INFO  [io.quarkus] (main) quarkus-native-issue stopped in 0.030s
```
## Failing Scenario (using native)
### Java 11
```
$ mvn -Pnative clean package
$ ./target/quarkus-native-issue-runner
```
### Java 8
```
$ mvn -Pjava8,native clean package
$ ./target/quarkus-native-issue-runner
```
Again, note this warning during build:
```
[WARNING] [io.quarkus.deployment.steps.ReflectiveHierarchyStep] Unable to properly register the hierarchy of the following classes for reflection as they are not in the Jandex index:
	- javax.inject.Provider
	- javax.ws.rs.client.Client
	- javax.ws.rs.client.WebTarget
	- javax.ws.rs.core.StreamingOutput
	- javax.ws.rs.core.UriBuilder
	- org.slf4j.Logger
Consider adding them to the index either by creating a Jandex index for your dependency via the Maven plugin, an empty META-INF/beans.xml or quarkus.index-dependency properties.");.
```
Once quarkus is running, point a web browser to http://localhost:8080/hello and you should get the default _Internal Server Error_ page.
Use `CTRL-C` (or equivalent) to stop Quarkus and the complete console output should look something like this (stack-traces replaced with `...` for brevity):
```
2020-02-06 23:09:57,943 INFO  [io.quarkus] (main) quarkus-native-issue 1.0.0-SNAPSHOT (running on Quarkus 1.2.0.Final) started in 0.011s. Listening on: http://0.0.0.0:8080
2020-02-06 23:09:57,943 INFO  [io.quarkus] (main) Profile prod activated. 
2020-02-06 23:09:57,943 INFO  [io.quarkus] (main) Installed features: [cdi, rest-client, resteasy, resteasy-jsonb, smallrye-health]
2020-02-06 23:10:03,619 INFO  [io.git.ema.qua.iss.Greeter] (executor-thread-1) fetching greetings
2020-02-06 23:10:03,619 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.IIOImageProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.IIOImageProvider
...

2020-02-06 23:10:03,621 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.sse.SseEventProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.sse.SseEventProvider
...

2020-02-06 23:10:03,622 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.DefaultTextPlain from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.DefaultTextPlain
...

2020-02-06 23:10:03,624 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider
...

2020-02-06 23:10:03,626 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.DefaultNumberWriter from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.DefaultNumberWriter
...

2020-02-06 23:10:03,627 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.SourceProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.SourceProvider
...

2020-02-06 23:10:03,627 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.DocumentProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.DocumentProvider
...

2020-02-06 23:10:03,629 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.DefaultBooleanWriter from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.DefaultBooleanWriter
...

2020-02-06 23:10:03,630 WARN  [org.jbo.res.res.i18n] (executor-thread-1) RESTEASY002120: ClassNotFoundException: Unable to load builtin provider org.jboss.resteasy.plugins.providers.JaxrsFormProvider from resource:META-INF/services/javax.ws.rs.ext.Providers: java.lang.ClassNotFoundException: org.jboss.resteasy.plugins.providers.JaxrsFormProvider
...

2020-02-06 23:10:03,631 ERROR [io.qua.ver.htt.run.QuarkusErrorHandler] (executor-thread-1) HTTP Request to /hello failed, error id: 855020c8-e29b-45e1-a2ec-2eaa7c3bfcbc-1: org.jboss.resteasy.spi.UnhandledException: javax.json.stream.JsonGenerationException: Generating incomplete JSON
...

2020-02-06 23:10:43,328 INFO  [io.quarkus] (main) quarkus-native-issue stopped in 0.002s
```
