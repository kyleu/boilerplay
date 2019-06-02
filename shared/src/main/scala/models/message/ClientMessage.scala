package models.message

import com.kyleu.projectile.util.BinarySerializers._
import com.kyleu.projectile.util.JsonSerializers._

sealed trait ClientMessage

object ClientMessage {
  implicit val jsonEncoder: Encoder[ClientMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[ClientMessage] = deriveDecoder
  implicit val pickler: Pickler[ClientMessage] = generatePickler[ClientMessage]

  final case class Ping(ts: Long) extends ClientMessage
}
