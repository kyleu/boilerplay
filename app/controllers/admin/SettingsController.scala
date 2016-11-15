package controllers.admin

import controllers.BaseController
import models.settings.SettingKey
import services.settings.SettingsService
import utils.Application
import utils.web.FormUtils

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def settings = withAdminSession("admin-settings") { implicit request =>
    Future.successful(Ok(views.html.admin.settings(request.identity)))
  }

  def saveSettings = withAdminSession("admin-settings-save") { implicit request =>
    val form = FormUtils.getForm(request)
    form.foreach { x =>
      SettingKey.withNameOption(x._1) match {
        case Some(settingKey) => SettingsService.set(settingKey, x._2)
        case None => log.warn(s"Attempt to save invalid setting [${x._1}].")
      }
    }
    Future.successful(Redirect(controllers.routes.HomeController.home()))
  }
}
