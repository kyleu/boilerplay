package controllers.admin.system

import controllers.BaseController
import models.Application
import models.settings.SettingKey
import util.web.ControllerUtils

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsController @javax.inject.Inject() (override val app: Application) extends BaseController("settings") {
  import app.contexts.webContext

  def settings = withSession("settings.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.settings(request.identity, app.settingsService)))
  }

  def saveSettings = withSession("settings.save", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request)
    form.foreach { x =>
      SettingKey.withNameOption(x._1) match {
        case Some(settingKey) => app.settingsService.set(settingKey, x._2)
        case None => log.warn(s"Attempt to save invalid setting [${x._1}].")
      }
    }
    Future.successful(Redirect(controllers.routes.HomeController.home()))
  }
}
