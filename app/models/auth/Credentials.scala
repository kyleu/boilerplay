package models.auth

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import models.user.User

case class Credentials(user: User, remoteAddress: String, tags: Map[String, String] = Map.empty)

object Credentials {
  implicit val jsonEncoder: Encoder[Credentials] = deriveEncoder
  implicit val jsonDecoder: Decoder[Credentials] = deriveDecoder

  val system = Credentials(User.system, "localhost")

  def fromRequest(request: SecuredRequest[AuthEnv, _]) = Credentials(request.identity, request.remoteAddress)
}
