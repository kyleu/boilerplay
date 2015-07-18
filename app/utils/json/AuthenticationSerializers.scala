package utils.json

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import play.api.libs.json._

object AuthenticationSerializers {
  implicit val loginInfoWrites = Json.writes[LoginInfo]
  implicit val profileWrites = Json.writes[CommonSocialProfile]
}
