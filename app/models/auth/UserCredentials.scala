package models.auth

import com.kyleu.projectile.services.Credentials
import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.user.SystemUser
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.util.NullUtils

final case class UserCredentials(user: SystemUser, remoteAddress: String = NullUtils.str, tags: Map[String, String] = Map.empty) extends Credentials

object UserCredentials {
  implicit val jsonEncoder: Encoder[UserCredentials] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserCredentials] = deriveDecoder

  val system = UserCredentials(SystemUser.system, "localhost")

  def fromInsecureRequest(request: UserAwareRequest[AuthEnv, _]) = UserCredentials(request.identity.getOrElse(SystemUser.api), request.remoteAddress)
  def fromRequest(request: SecuredRequest[AuthEnv, _]) = UserCredentials(request.identity, request.remoteAddress)
}
