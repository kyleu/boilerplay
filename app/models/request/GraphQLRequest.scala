package models.request

import java.time.LocalDateTime

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import io.circe.{Decoder, Encoder}

object GraphQLRequest {
  implicit val jsonEncoder: Encoder[GraphQLRequest] = deriveEncoder
  implicit val jsonDecoder: Decoder[GraphQLRequest] = deriveDecoder
}

case class GraphQLRequest(
    name: String,
    query: String = "query NewQuery {\n  \n}",
    variables: Map[String, String] = Map.empty,
    source: String = "adhoc",
    author: String = "Unknown",
    created: LocalDateTime = util.DateUtils.now
) {
  val variablesJson = {
    import io.circe.syntax._
    variables.asJson
  }
  val bodySize = query.length
}
