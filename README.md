# Boilerplay

Using the latest technology in the Scala ecosystem, Boilerplay is a reactive web application built on Play 2.4, ScalaJS, Silhouette, and postgres-async. 
It provides a good starting point for whatever you want to build.


## Features

* Social sign-in and local credentials support.
* Role based security, with normal and admin roles.
* Full admin suite for user management, reporting, and real-time session observation.
* Ad-hoc queries, nightly activity reports, and other pretty charts and graphs.
* Sophisticated asset pipeline using Require.js and UglifyJS for minified/uglifed/compressed resources.
* Scala source that is shared between the client and server.
* Websocket-driven actor support, with monitoring and tracing.


## Technology

The Play application communicates over a WebSocket to a pool of Akka actors managing connections. 
Serialization is handled by Play Json, and all database communication runs via postgres-async. Scala.js compiles the
shared code and provides an in-browser component. You can sign in with Facebook, Google, or Twitter thanks to Play Silhouette. 
Websocket communication is handled via Play and Akka.


## Running the app

First, either create a Postgres role and database named "boilerplay", or change the application.conf to use your existing database.

```sql
CREATE ROLE boilerplay WITH LOGIN PASSWORD 'password';
CREATE DATABASE boilerplay;
GRANT ALL PRIVILEGES ON DATABASE boilerplay TO boilerplay;
```

You'll either need Node.js available as "node" on the path, or change project/Server.scala's EngineType to Rhino.

Then you'll need to change the SMTP and Silhouette settings in application.conf to use your mail server and social apps.

For Twitter, visit https://apps.twitter.com/ and create a new app.

For Facebook, visit https://developers.facebook.com/products/ads/? and choose My Apps > Add a new app

For Google, visit https://console.developers.google.com/, create a project and credentials.

Now, finally,
```shell
$ sbt
> run
$ open http://127.0.0.1:9000
```

As the application starts, it will create database tables and seed data. If you see any SQL errors, restart (as the table creation has a race condition I'm working to fix).

Once you've signed up, visit http://127.0.0.1:9000/admin/enable to bootstrap your account as an admin. 


## Projects

* `server` Main web application.
* `sharedJvm` Core Scala logic and rules definitions, for JVM projects.
* `sharedJs` Shared classes, compiled to Scala.js JavaScript.
* `client` Barebones Scala.js app.


## Metrics

All meaningful operations are tracked through Scala Metrics, and are exposed through JMX, or via a servlet available on port 9001.
Reporting to Graphite can be enabled through application.conf, and reports to 127.0.0.1:2003 by default.
Metrics exposes all actors, queries, logs, requests, and jvm info.


## Contributing

All Scala code is formatted by Scalariform, and passes all checks from Scalastyle and Scapegoat. No Scala file is longer than 100 lines, no line 
longer than 140 characters, and all warnings are treated as errors. Tests are part of the main source tree so they can be run from the browser.

JavaScript is verified by Require.js and UglifyJS. Any Javascript errors or warnings will be treated as compile errors. 

The project is built on SBT, and can be opened by IntelliJ directly.


## License

The code is licensed under [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
