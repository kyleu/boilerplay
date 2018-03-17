package util

import io.circe.generic.extras.Configuration
import io.circe.java8.time._
import io.circe.parser._
import io.circe.syntax._
import models.user.UserPreferences
import models.{RequestMessage, ResponseMessage}

object JsonSerializers {
  object Circe {
    import io.circe.generic.extras.decoding.ConfiguredDecoder
    import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
    import shapeless.Lazy

    type Decoder[A] = io.circe.Decoder[A]
    type Encoder[A] = io.circe.Encoder[A]

    implicit def encodeLocalDateTime = io.circe.java8.time.encodeLocalDateTimeDefault
    implicit def encodeLocalDate = io.circe.java8.time.encodeLocalDateDefault
    implicit def encodeLocalTime = io.circe.java8.time.encodeLocalTimeDefault
    implicit def decodeLocalDateTime = io.circe.java8.time.decodeLocalDateTimeDefault
    implicit def decodeLocalDate = io.circe.java8.time.decodeLocalDateDefault
    implicit def decodeLocalTime = io.circe.java8.time.decodeLocalTimeDefault

    implicit val config: Configuration = Configuration.default.withDefaults
    def deriveDecoder[A](implicit decode: Lazy[ConfiguredDecoder[A]]) = io.circe.generic.extras.semiauto.deriveDecoder[A]
    def deriveEncoder[A](implicit decode: Lazy[ConfiguredObjectEncoder[A]]) = io.circe.generic.extras.semiauto.deriveEncoder[A]
  }

  def toJson(s: String) = parse(s) match {
    case Right(json) => json
    case Left(failure) => throw failure
  }

  val emptyObject = toJson("{}")

  def readPreferences(s: String) = decode[UserPreferences](s) match {
    case Right(x) => x
    case Left(_) => UserPreferences.empty
  }
  def writePreferences(p: UserPreferences, indent: Boolean = true) = if (indent) { p.asJson.spaces2 } else { p.asJson.noSpaces }

  def readRequestMessage(s: String) = decode[RequestMessage](s) match {
    case Right(x) => x
    case Left(err) => throw err
  }
  def writeRequestMessage(rm: RequestMessage) = rm.asJson.spaces2

  def readResponseMessage(s: String) = decode[ResponseMessage](s) match {
    case Right(x) => x
    case Left(err) => throw err
  }
  def writeResponseMessage(rm: ResponseMessage) = rm.asJson.spaces2
}
