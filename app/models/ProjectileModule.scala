package models

import com.google.inject.{AbstractModule, Provides}
import com.kyleu.projectile.graphql.GraphQLSchema
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.models.config.{NavHtml, NavUrls, UiConfig, UserSettings}
import com.kyleu.projectile.models.user.Role
import com.kyleu.projectile.services.database.{ApplicationDatabase, JdbcDatabase}
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.JsonSerializers.extract
import com.kyleu.projectile.util.metrics.MetricsConfig
import com.kyleu.projectile.util.tracing.{OpenTracingService, TracingService}
import com.kyleu.projectile.web.util.{ErrorHandler, GravatarUrl}
import io.circe.JsonObject
import models.graphql.Schema
import models.template.UserMenu
import net.codingwell.scalaguice.ScalaModule
import services.note.ModelNoteService
import services.settings.SettingsService
import util.Version

class ProjectileModule extends AbstractModule with ScalaModule {
  override def configure() = {
    bind[NoteService].to(classOf[ModelNoteService])
  }

  @Provides
  def providesTracingService(cnf: MetricsConfig): TracingService = new OpenTracingService(cnf)

  @Provides
  def providesJdbcDatabase(): JdbcDatabase = ApplicationDatabase

  @Provides
  def providesApplicationActions = Application.Actions(
    projectName = Version.projectName,
    configForUser = (su, admin, crumbs) => su match {
      case None => UiConfig.empty.copy(projectName = Version.projectName, menu = UserMenu.guestMenu)
      case Some(u) =>
        val menu = if (admin) { UserMenu.adminMenu(u) } else { UserMenu.standardMenu(u) }

        val settings = extract[JsonObject](u.settings)
        val theme = settings("theme").map(extract[String]).getOrElse("dark")
        val user = UserSettings(name = u.username, theme = theme, avatarUrl = Some(GravatarUrl(u.email)))

        val html = NavHtml(views.html.components.headerRightMenu(user.name, user.avatarUrl.getOrElse("")))

        val breadcrumbs = UserMenu.breadcrumbs(menu, crumbs)

        UiConfig(projectName = Version.projectName, menu = menu, urls = NavUrls(), html = html, user = user, breadcrumbs = breadcrumbs)
    }
  )

  @Provides
  def providesErrorActions() = new ErrorHandler.Actions()

  @Provides
  def providesAuthActions(settings: SettingsService) = new AuthActions(projectName = Version.projectName) {
    override def allowRegistration = settings.allowRegistration
    override def defaultRole = Role.Admin
  }

  @Provides
  def providesGraphQLSchema(): GraphQLSchema = Schema
}
