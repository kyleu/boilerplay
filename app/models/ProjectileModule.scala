package models

import com.kyleu.projectile.graphql.GraphQLSchema
import com.kyleu.projectile.models.AdminModule
import com.kyleu.projectile.models.config.NavMenu
import com.kyleu.projectile.models.user.{Role, SystemUser}
import models.graphql.Schema
import models.template.UserMenu
import util.Version

class ProjectileModule extends AdminModule(allowSignup = true, initialRole = Role.Admin) {
  override def projectName = Version.projectName
  override def guestMenu = UserMenu.guestMenu
  override def userMenu(u: SystemUser) = if (u.isAdmin) { UserMenu.adminMenu(u) } else { UserMenu.standardMenu(u) }
  override def breadcrumbs(menu: Seq[NavMenu], keys: Seq[String]) = UserMenu.breadcrumbs(menu, keys)

  override protected[this] def schema: GraphQLSchema = Schema
}
