package models.result.filter

import util.JsonSerializers._

object Filter {
  implicit val jsonEncoder: Encoder[Filter] = deriveEncoder
  implicit val jsonDecoder: Decoder[Filter] = deriveDecoder
}

case class Filter(
    k: String = "?",
    o: FilterOp = Equal,
    v: Seq[String] = Nil
)
