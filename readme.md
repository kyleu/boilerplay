# Boilerplay

Using the latest technology in the Scala ecosystem, Boilerplay is a pure Scala reactive web application built on Play 2.6, ScalaJS, Silhouette 5, Akka, and Sangria.
It provides a good starting point for whatever you want to build.

## Documentation

https://kyleu.github.io/boilerplay


## Features

* Local sign-in, profile, and change password support.
* Role based security, with normal and admin roles.
* Full admin suite for user management, reporting, and real-time session observation.
* GraphQL schema and query interface, with shared queries and mutations.
* Scala source that is shared between the client and server via Scala.js.
* Websocket-driven actor support, with monitoring and tracing.


## Contributing

All Scala code is formatted by Scalariform, and passes all checks from Scalastyle and Scapegoat. No Scala file is longer than 100 lines, no line
longer than 140 characters, and all warnings are treated as errors. Tests are part of the main source tree so they can be run from the browser.

The project is built on SBT, and can be opened by IntelliJ directly.


## License

The code is licensed under [CC0-1.0](./license). 

You can basically do whatever you want with the code, no attribution required. Make it your own! 
