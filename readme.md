# Boilerplay

Using the latest technology in the Scala ecosystem, Boilerplay is a pure Scala reactive web application built on Play 2.6, Scala.js, Silhouette 5, Akka, PostgreSQL 9.5+, and Sangria/GraphQL.
It provides a good starting point for whatever you want to build.


## Documentation

https://kyleu.github.io/boilerplay


## Features

* Local sign-in, profile, and change password support.
* Role based security, with normal and admin roles.
* Full admin suite for user management, reporting, and real-time session observation.
* GraphQL schema and query interface, with shared queries and mutations.
* OpenAPI/Swagger definitions for all routes, along with a packaged UI.
* Scala source code, shared between the client and server via Scala.js.
* JDBC queries, Slick definitions, and an instrumented database access layer. 
* Websocket-driven actor support, with monitoring and tracing.
* Binary and JSON serialization, provided by circe and Boopickle.


## Contributing

The project is built on SBT, and can be opened by IntelliJ directly. Plugins are included for Eclipse and Sublime Text.


## License

The code is licensed under [CC0-1.0](license). 

You can basically do whatever you want with the code, no attribution required. Make it your own! 
