package models

import com.google.inject.{AbstractModule, Provides}
import com.kyleu.projectile.graphql.GraphQLSchema
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.models.user.Role
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.metrics.MetricsConfig
import com.kyleu.projectile.util.tracing.{OpenTracingService, TracingService}
import com.kyleu.projectile.web.util.ErrorHandler
import models.graphql.Schema
import net.codingwell.scalaguice.ScalaModule
import services.note.ModelNoteService
import services.settings.SettingsService
import util.Version

class ProjectileModule extends AbstractModule with ScalaModule {
  override def configure() = {
    bind[NoteService].to(classOf[ModelNoteService])
  }

  @Provides
  def provideTracingService(cnf: MetricsConfig): TracingService = new OpenTracingService(cnf)

  @Provides
  def provideApplicationActions = Application.Actions(projectName = Version.projectName)

  @Provides
  def providesErrorActions() = new ErrorHandler.Actions()

  @Provides
  def providesAuthActions(settings: SettingsService) = new AuthActions(projectName = Version.projectName) {
    override def allowRegistration = settings.allowRegistration
    override def defaultRole = Role.Admin
    override def adminMenu = views.html.admin.layout.menu.apply
  }

  @Provides
  def provideGraphQLSchema(): GraphQLSchema = Schema
}
