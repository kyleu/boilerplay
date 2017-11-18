#Monitor

@@@ index

* [Metrics](metrics.md)
* [Tracing](tracing.md)
* [Actors](actors.md)

@@@

Boilerplay exposes a *ton* of metrics to allow you to understand how your application performs.

Performance meters are provided by Scala Metrics, exposed through port 9001, and forwarded to Graphite or Prometheus. 
All web requests are traced via Zipkin, and Akka message flows can be easily visualized. 

@@toc { depth=2 }
