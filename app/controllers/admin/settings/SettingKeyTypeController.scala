/* Generated File */
package controllers.admin.settings

import com.kyleu.projectile.controllers.{AuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.util.JsonSerializers._
import models.settings.SettingKeyType
import play.twirl.api.Html
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

@javax.inject.Singleton
class SettingKeyTypeController @javax.inject.Inject() (override val app: Application) extends AuthController("settingKeyType") {

  def list = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.layout.listPage(request.identity, "SettingKeyType", "explore", SettingKeyType.values.map(v => Html(v.toString))))
      case Accepts.Json() => Ok(SettingKeyType.values.asJson)
      case ServiceController.acceptsCsv() => Ok(SettingKeyType.values.mkString(", ")).as("text/csv")
    })
  }
}
