package controllers

import com.google.inject.Injector
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.models.config.NotificationService
import com.kyleu.projectile.services.audit.{AuditHelper, AuditService}
import com.kyleu.projectile.services.database.MigrateTask
import com.kyleu.projectile.util.tracing.TraceData
import services.audit.{AuditCallbacks, AuditLookup}
import util.Version

import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (
    override val app: Application,
    aud: AuditService,
    audLookup: AuditLookup,
    injector: Injector
)(implicit ec: ExecutionContext) extends AuthController("home") {
  MigrateTask.migrate(app.db.source)(TraceData.noop)
  NotificationService.setCallback(f = user => Nil)
  AuditHelper.init(appName = Version.projectName, service = aud)
  AuditCallbackProvider.init(new AuditCallbacks(injector, audLookup))

  def home() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.index(request.identity, app.cfg(Some(request.identity), admin = false), app.config.debug)))
  }

  def robots() = withSession("robots") { implicit request => implicit td =>
    Future.successful(Ok("User-agent: *\nDisallow: /"))
  }
}
