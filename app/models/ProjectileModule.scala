package models

import com.kyleu.projectile.graphql.GraphQLSchema
import com.kyleu.projectile.models.AdminModule
import com.kyleu.projectile.models.user.Role
import models.graphql.Schema
import models.template.UserMenu
import util.Version

class ProjectileModule extends AdminModule(projectName = Version.projectName, allowSignup = true, initialRole = Role.Admin, menuProvider = UserMenu) {
  override protected[this] def schema: GraphQLSchema = Schema
}
