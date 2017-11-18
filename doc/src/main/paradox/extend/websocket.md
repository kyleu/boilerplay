# Websocket

The main index page includes the `client` Scala.js project, and opens a websocket. 
Every few seconds, it will send a `Ping` message, await a `Pong` response, and updates the display's statistics.

![Websocket stats](websocketStats.png)

To add your own messages, add a case class to `RequestMessages.scala` or `ResponseMessages.scala`. 
No other steps are needed, see [Serialization](serialization.md) for more details. 
