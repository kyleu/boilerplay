package models.message

import com.kyleu.projectile.util.BinarySerializers._
import com.kyleu.projectile.util.JsonSerializers._

sealed trait ServerMessage

object ServerMessage {
  implicit val jsonEncoder: Encoder[ServerMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[ServerMessage] = deriveDecoder
  implicit val pickler: Pickler[ServerMessage] = generatePickler[ServerMessage]

  final case class Pong(ts: Long, serverTime: Long) extends ServerMessage
}
