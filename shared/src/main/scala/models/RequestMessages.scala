package models

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage

case class Ping(timestamp: Long) extends RequestMessage
case object GetVersion extends RequestMessage
case class SetPreference(name: String, value: String) extends RequestMessage

case class DebugInfo(data: String) extends RequestMessage
