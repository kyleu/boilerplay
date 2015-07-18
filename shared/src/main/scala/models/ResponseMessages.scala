package models

sealed trait ResponseMessage
trait ReversibleResponseMessage extends ResponseMessage

case class ServerError(reason: String, content: String) extends ResponseMessage
case class Pong(timestamp: Long) extends ResponseMessage
case class VersionResponse(version: String) extends ResponseMessage
case object SendDebugInfo extends ResponseMessage
case class Disconnected(reason: String) extends ResponseMessage

case class MessageSet(messages: Seq[ResponseMessage]) extends ReversibleResponseMessage
