package models.template

import com.kyleu.projectile.models.config.{MenuProvider, NavMenu}
import com.kyleu.projectile.models.user.SystemUser

object UserMenu extends MenuProvider {
  private[this] lazy val graphQL = {
    import com.kyleu.projectile.controllers.graphql.routes.{GraphQLController, SchemaController}
    NavMenu(key = "graphql", title = "GraphQL", url = Some(GraphQLController.graphql().url), children = Seq(
      NavMenu(key = "ide", title = "IDE", url = Some(GraphQLController.graphql().url), icon = Some("brightness_low")),
      NavMenu(key = "query", title = "Query Schema", url = Some(SchemaController.voyager("Query").url), icon = Some("brightness_high")),
      NavMenu(key = "mutation", title = "Mutation Schema", url = Some(SchemaController.voyager("Query").url), icon = Some("brightness_high"))
    ), flatSection = true)
  }

  private[this] lazy val system = NavMenu(key = "system", title = "System", children = Seq(
    NavMenu(key = "ide", title = "Swagger UI", url = Some(com.kyleu.projectile.controllers.rest.routes.OpenApiController.ui().url), icon = Some("dialpad")),
    NavMenu(key = "notes", title = "Notes", url = Some(com.kyleu.projectile.controllers.admin.note.routes.NoteController.list().url), icon = Some("folder_open")),
    NavMenu(key = "audit", title = "Audits", url = Some(com.kyleu.projectile.controllers.admin.audit.routes.AuditController.list().url), icon = Some("memory")),
    NavMenu(key = "user", title = "System Users", url = Some(com.kyleu.projectile.controllers.admin.user.routes.SystemUserController.list().url), icon = Some("account_circle")),
    NavMenu(key = "sandbox", title = "Sandbox Methods", url = Some(controllers.admin.system.routes.SandboxController.list().url), icon = Some("widgets")),
    NavMenu(key = "tasks", title = "Scheduled Tasks", url = Some(controllers.admin.system.routes.ScheduleController.list().url), icon = Some("access_time"))
  ), flatSection = true)

  private[this] lazy val staticMenu = ComponentMenu.menu ++ Seq(graphQL, system)

  override def adminMenu(u: SystemUser) = staticMenu
}
