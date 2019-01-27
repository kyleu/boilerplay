package models

import com.google.inject.{AbstractModule, Provides}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.user.SystemUser
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.metrics.MetricsConfig
import com.kyleu.projectile.util.tracing.{OpenTracingService, TraceData, TracingService}
import com.kyleu.projectile.web.util.ErrorHandler
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import net.codingwell.scalaguice.ScalaModule
import play.api.mvc.{Flash, Session}
import services.note.ModelNoteService
import services.user.{OAuth2InfoService, PasswordInfoService, SystemUserSearchService}
import util.Version

class ProjectileModule extends AbstractModule with ScalaModule {
  override def configure() = {
    bind[NoteService].to(classOf[ModelNoteService])

    bind[IdentityService[SystemUser]].to(classOf[SystemUserSearchService])
    bind[DelegableAuthInfoDAO[PasswordInfo]].to(classOf[PasswordInfoService])
    bind[DelegableAuthInfoDAO[OAuth2Info]].to(classOf[OAuth2InfoService])
  }

  @Provides
  def provideApplicationActions: Application.Actions = Application.Actions(
    projectName = Version.projectName,
    failedRedirect = controllers.auth.routes.AuthenticationController.signInForm()
  )

  @Provides
  def providesErrorActions() = new ErrorHandler.Actions {
    override def badRequest(path: String, error: String)(implicit session: Session, flash: Flash, td: TraceData) = {
      views.html.error.badRequest(path, error)
    }
    override def serverError(error: String, ex: Option[Throwable])(implicit session: Session, flash: Flash, td: TraceData) = {
      views.html.error.serverError(error, ex)
    }
    override def notFound(path: String)(implicit session: Session, flash: Flash, td: TraceData) = {
      views.html.error.notFound(path)
    }
  }

  @Provides
  def provideTracingService(cnf: MetricsConfig): TracingService = new OpenTracingService(cnf)
}
