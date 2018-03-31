/* Generated File */
package controllers.admin.settings

import controllers.BaseController
import controllers.admin.ServiceController
import models.settings.SettingKey
import play.twirl.api.Html
import scala.concurrent.Future
import util.JsonSerializers._

@javax.inject.Singleton
class SettingKeyController @javax.inject.Inject() (override val app: models.Application) extends BaseController("settingKey") {
  import app.contexts.webContext

  def list = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.layout.listPage(request.identity, "SettingKey", "explore", SettingKey.values.map(x => Html(x.toString))))
      case Accepts.Json() => Ok(SettingKey.values.asJson)
      case ServiceController.acceptsCsv() => Ok(SettingKey.values.mkString(", ")).as("text/csv")
    })
  }
}
