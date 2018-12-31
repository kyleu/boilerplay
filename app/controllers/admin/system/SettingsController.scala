package controllers.admin.system

import controllers.BaseController
import models.Application
import scala.concurrent.ExecutionContext.Implicits.global
import models.settings.SettingKeyType
import com.kyleu.projectile.util.web.ControllerUtils

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsController @javax.inject.Inject() (override val app: Application) extends BaseController("settings") {
  def settings = withSession("settings.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.settingValues(request.identity, app.coreServices.settings)))
  }

  def saveSettings = withSession("settings.save", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    form.foreach { x =>
      SettingKeyType.withValueOpt(x._1) match {
        case Some(settingKey) => app.coreServices.settings.set(settingKey, x._2)
        case None => log.warn(s"Attempt to save invalid setting [${x._1}].")
      }
    }
    Future.successful(Redirect(controllers.routes.HomeController.home()))
  }
}
