package util

import java.time.{LocalDate, LocalDateTime, LocalTime}

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.decoding.ConfiguredDecoder
import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
import io.circe.java8.time._
import shapeless.Lazy

object JsonSerializers {
  type Decoder[A] = io.circe.Decoder[A]
  type Encoder[A] = io.circe.Encoder[A]

  implicit def encodeLocalDateTime: Encoder[LocalDateTime] = io.circe.java8.time.encodeLocalDateTimeDefault
  implicit def encodeLocalDate: Encoder[LocalDate] = io.circe.java8.time.encodeLocalDateDefault
  implicit def encodeLocalTime: Encoder[LocalTime] = io.circe.java8.time.encodeLocalTimeDefault
  implicit def decodeLocalDateTime: Decoder[LocalDateTime] = io.circe.java8.time.decodeLocalDateTimeDefault
  implicit def decodeLocalDate: Decoder[LocalDate] = io.circe.java8.time.decodeLocalDateDefault
  implicit def decodeLocalTime: Decoder[LocalTime] = io.circe.java8.time.decodeLocalTimeDefault

  implicit val config: Configuration = Configuration.default.withDefaults
  def deriveDecoder[A](implicit decode: Lazy[ConfiguredDecoder[A]]) = io.circe.generic.extras.semiauto.deriveDecoder[A]
  def deriveEncoder[A](implicit decode: Lazy[ConfiguredObjectEncoder[A]]) = io.circe.generic.extras.semiauto.deriveEncoder[A]
}
