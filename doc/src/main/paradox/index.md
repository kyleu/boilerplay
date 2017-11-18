# Boilerplay

@@@ index

* [Getting Started](gettingStarted.md)
* [Explore](explore/index.md)
* [Extend](extend/index.md)
* [Monitor](monitor/index.md)
* [Build](build/index.md)
* [Technology](technology.md)
* [Troubleshooting](troubleshooting.md)

@@@

Using the latest technology in the Scala ecosystem, Boilerplay is a pure Scala reactive web application built on Play 2.6, ScalaJS, Silhouette 5, Akka 2.5, and Sangria.
It provides a good starting point for whatever you want to build.

Boilerplay relies on a whole lot of tremendous open source projects. [Here's a few of them](technology.md).


## Features

* [Admin](explore/admin.md) - Full admin suite for user management, reporting, and real-time session observation.
* [GraphQL](explore/graphql.md) - GraphQL schema and query interface, with shared queries and mutations.
* [Auditing](explore/auditing.md) - Models can be automatically audited, admins can search history, add notes, and view changes in real time.
* [Authentication](explore/authentication.md) - Local sign-in, profile, and change password support.
* [Security](explore/security.md) - Role based security, with normal and admin roles.
* [Websocket](extend/websocket.md) - Websocket-driven actor support, with monitoring and tracing.
* [Metrics](monitor/metrics.md) - Every database query, service call, and web request generates detailed metrics, published to Graphite or Prometheus.
* [Tracing](monitor/tracing.md) - Detailed Zipkin traces show every aspect of web requests, including detailed microservice tracking.


## License

The code is licensed under [CC0-1.0](https://raw.githubusercontent.com/KyleU/boilerplay/master/license). 

You can basically do whatever you want with the code, no attribution required. Make it your own! 
