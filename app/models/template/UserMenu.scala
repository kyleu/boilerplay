package models.template

import com.kyleu.projectile.models.config.{BreadcrumbEntry, NavMenu}
import com.kyleu.projectile.models.user.SystemUser

object UserMenu {
  val guestMenu = Seq.empty[NavMenu]
  def standardMenu(u: SystemUser) = Seq.empty[NavMenu]
  def adminMenu(u: SystemUser) = staticMenu

  def breadcrumbs(menu: Seq[NavMenu], keys: Seq[String]): Seq[BreadcrumbEntry] = menu.find(m => keys.headOption.contains(m.key)) match {
    case Some(m) if keys.size == 1 => Seq(BreadcrumbEntry(m.key, m.title, m.url))
    case Some(m) => BreadcrumbEntry(m.key, m.title, m.url) +: breadcrumbs(m.children, keys.drop(1))
    case None => keys.toList match {
      case Nil => Nil
      case h :: tail => BreadcrumbEntry(h, h, None) +: breadcrumbs(Nil, tail)
    }
  }

  private[this] val staticGraphQL = {
    import com.kyleu.projectile.controllers.graphql
    NavMenu(key = "graphql", title = "GraphQL", url = Some(graphql.routes.GraphQLController.graphql().url), children = Seq(
      NavMenu(key = "ide", title = "IDE", url = Some(graphql.routes.GraphQLController.graphql().url), icon = Some("brightness_low")),
      NavMenu(key = "query", title = "Query Schema", url = Some(graphql.routes.SchemaController.voyager("Query").url), icon = Some("brightness_high")),
      NavMenu(key = "mutation", title = "Mutation Schema", url = Some(graphql.routes.SchemaController.voyager("Query").url), icon = Some("brightness_high"))
    ), flatSection = true)
  }

  private[this] val staticRest = {
    NavMenu(key = "rest", title = "Rest", url = Some(com.kyleu.projectile.controllers.rest.routes.OpenApiController.ui().url), children = Seq(
      NavMenu(key = "ide", title = "Swagger UI", url = Some(com.kyleu.projectile.controllers.rest.routes.OpenApiController.ui().url), icon = Some("dialpad"))
    ), flatSection = true)
  }

  private[this] val staticStatus = NavMenu(key = "status", title = "Status", children = Seq(
    NavMenu(key = "prefs", title = "System Preferences", url = Some(controllers.admin.system.routes.SettingsController.settings().url), icon = Some("flag")),
    NavMenu(key = "audits", title = "Audit Trail", url = Some(controllers.admin.audit.routes.AuditRowController.list().url), icon = Some("settings")),
    NavMenu(key = "metrics", title = "Metrics", url = Some(controllers.admin.system.routes.MetricsController.showMetrics().url), icon = Some("short_text")),
    NavMenu(key = "tasks", title = "Scheduled Tasks", url = Some(controllers.admin.system.routes.ScheduleController.list().url), icon = Some("access_time")),
    NavMenu(key = "sandbox", title = "Sandbox Methods", url = Some(controllers.admin.system.routes.SandboxController.list().url), icon = Some("widgets"))
  ), flatSection = true)

  private[this] val staticMenu = ComponentMenu.menu ++ Seq(staticGraphQL, staticRest, staticStatus)
}
