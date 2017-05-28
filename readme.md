# Boilerplay

Using the latest technology in the Scala ecosystem, Boilerplay is a pure Scala reactive web application built on Play 2.5, ScalaJS, Silhouette 4, Akka, Sangria, and postgres-async.
It provides a good starting point for whatever you want to build.

## Features

* Local sign-in, profile, and change password support.
* Role based security, with normal and admin roles.
* Full admin suite for user management, reporting, and real-time session observation.
* GraphQL schema and query interface, with shared queries and mutations.
* Scala source that is shared between the client and server via Scala.js.
* Websocket-driven actor support, with monitoring and tracing.


## Technology

The Play application communicates over a WebSocket to a pool of Akka actors managing connections.
Serialization is handled by Play Json, and all database communication runs via postgres-async. Scala.js compiles the
shared code and provides an in-browser component. 

The index page opens a websocket connection for bidirectional communication, handled via Play and Akka.

A GraphQL schema is provided, accessible in the administration section.

## Running the app

First, either create a Postgres role and database named "boilerplay", or change the application.conf to use your existing database.

```sql
CREATE ROLE boilerplay WITH LOGIN PASSWORD 'boilerplay';
CREATE DATABASE boilerplay;
GRANT ALL PRIVILEGES ON DATABASE boilerplay TO boilerplay;
```

Alternatively, start Postgres in a Docker container using the script provided:
```shell
$ ./start_postgres.sh
```

You'll either need Node.js available as "node" on the path, or change project/Server.scala's EngineType to Rhino.

Then you'll need to change the Silhouette sections in application.conf to use your authentication settings.

Now, finally,
```shell
$ sbt
> run
$ open http://127.0.0.1:9000
```

As the application starts, it will create database tables and seed data.

The first account to sign up is created as an Admin, all subsequent users will have a normal user role.


## Projects

* `server` Main web application.
* `sharedJvm` Core Scala logic and rules definitions, for JVM projects.
* `sharedJs` Shared classes, compiled to Scala.js JavaScript.
* `client` Barebones Scala.js app.


## Metrics

All meaningful operations are tracked through Scala Metrics, and are exposed through JMX, or via a servlet available on port 9001.
Reporting to Graphite can be enabled through application.conf, and reports to 127.0.0.1:2003 by default.
Metrics exposes all actors, queries, logs, requests, and jvm info.


## Make it your own

To start your own project, you can change every instance of the word "boilerplay" to your project's id, and 
change every instance of "Boilerplay" to your project's name. Don't worry, there's only a few. 


## Contributing

All Scala code is formatted by Scalariform, and passes all checks from Scalastyle and Scapegoat. No Scala file is longer than 100 lines, no line
longer than 140 characters, and all warnings are treated as errors. Tests are part of the main source tree so they can be run from the browser.

The project is built on SBT, and can be opened by IntelliJ directly.


## Technology

Boilerplay relies on a whole lot of tremendous open source projects. Here's a few of them.

* [Scala](http://www.scala-lang.org)
* [Scala.js](https://www.scala-js.org)
* [Play Framework](https://www.playframework.com)
* [Akka](http://akka.io)
* [Sangria](http://sangria-graphql.org)
* [Materialize CSS](http://materializecss.com)
* [Enumeratum](https://github.com/lloydmeta/enumeratum)
* [circe](https://circe.github.io/circe)
* [Scalatags](https://github.com/lihaoyi/scalatags)
* [Postgres-async](https://github.com/mauricio/postgresql-async)
* [Silhouette](https://www.silhouette.rocks)
* [Dropwizard Metrics](http://metrics.dropwizard.io)
* [Netty](http://netty.io)
* [ScalaCrypt](https://github.com/Richard-W/scalacrypt)
* [Font Awesome](http://fontawesome.io)
* [JQuery](https://jquery.com/)


## License

The code is licensed under [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
