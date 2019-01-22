package controllers.admin.system

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application

import scala.concurrent.ExecutionContext.Implicits.global
import models.settings.SettingKeyType
import com.kyleu.projectile.web.util.ControllerUtils
import services.settings.SettingsService

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsController @javax.inject.Inject() (override val app: Application, settingsService: SettingsService) extends AuthController("settings") {
  def settings = withSession("settings.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.settingValues(request.identity, settingsService)))
  }

  def saveSettings = withSession("settings.save", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    form.foreach { x =>
      SettingKeyType.withValueOpt(x._1) match {
        case Some(settingKey) => settingsService.set(settingKey, x._2)
        case None => log.warn(s"Attempt to save invalid setting [${x._1}].")
      }
    }
    Future.successful(Redirect(controllers.routes.HomeController.home()))
  }
}
