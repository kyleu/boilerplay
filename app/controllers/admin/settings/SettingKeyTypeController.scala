/* Generated File */
package controllers.admin.settings

import com.kyleu.projectile.controllers.{AuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.util.JsonSerializers._
import models.settings.SettingKeyType
import play.twirl.api.Html
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class SettingKeyTypeController @javax.inject.Inject() (override val app: Application)(implicit ec: ExecutionContext) extends AuthController("settingKeyType") {
  def list = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(render {
      case Accepts.Html() => Ok(com.kyleu.projectile.views.html.admin.layout.listPage(
        title = "SettingKeyType",
        cfg = app.cfg(u = Some(request.identity), admin = true, "settings", "setting_key"),
        vals = SettingKeyType.values.map(v => Html(v.toString))
      ))
      case Accepts.Json() => Ok(SettingKeyType.values.asJson)
      case ServiceController.acceptsCsv() => Ok(SettingKeyType.values.mkString(", ")).as("text/csv")
    })
  }
}
