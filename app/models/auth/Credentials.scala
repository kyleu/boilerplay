package models.auth

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.user.SystemUser
import util.JsonSerializers._

final case class Credentials(user: SystemUser, remoteAddress: String = util.NullUtils.str, tags: Map[String, String] = Map.empty)

object Credentials {
  implicit val jsonEncoder: Encoder[Credentials] = deriveEncoder
  implicit val jsonDecoder: Decoder[Credentials] = deriveDecoder

  val system = Credentials(SystemUser.system, "localhost")

  def fromInsecureRequest(request: UserAwareRequest[AuthEnv, _]) = Credentials(request.identity.getOrElse(SystemUser.api), request.remoteAddress)
  def fromRequest(request: SecuredRequest[AuthEnv, _]) = Credentials(request.identity, request.remoteAddress)
}
