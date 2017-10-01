# Websocket

The main index page includes the `client` Scala.js project, and opens a websocket. 
Every few seconds, it will send a `Ping` message, await a `Pong` response, and calculate the latency.
To add your own messages, add a case class to `RequestMessages.scala`.
