package models

import java.time.LocalDateTime

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage

case class Ping(timestamp: LocalDateTime) extends RequestMessage
case object GetVersion extends RequestMessage
