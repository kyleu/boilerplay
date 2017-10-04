## Technology

The Play application communicates over a WebSocket to a pool of Akka actors managing connections.
Serialization is handled by Play Json, and all database communication runs via jdbc. Scala.js compiles the
shared code and provides an in-browser component. 

The index page opens a websocket connection for bidirectional communication, handled via Play and Akka.

A GraphQL schema is provided, accessible in the administration section.

Boilerplay relies on a whole lot of tremendous open source projects. Here's a few of them.

* [Scala](http://www.scala-lang.org)
* [Scala.js](https://www.scala-js.org)
* [Play Framework](https://www.playframework.com)
* [Akka](http://akka.io)
* [Sangria](http://sangria-graphql.org)
* [Graphiql](https://github.com/graphql/graphiql)
* [GraphQL Voyager](https://github.com/APIs-guru/graphql-voyager)
* [Zipkin](http://zipkin.io)
* [Materialize CSS](http://materializecss.com)
* [Enumeratum](https://github.com/lloydmeta/enumeratum)
* [circe](https://circe.github.io/circe)
* [Silhouette](https://www.silhouette.rocks)
* [Dropwizard Metrics](http://metrics.dropwizard.io)
* [Netty](http://netty.io)
* [ScalaCrypt](https://github.com/Richard-W/scalacrypt)
* [Font Awesome](http://fontawesome.io)
* [JQuery](https://jquery.com/)
