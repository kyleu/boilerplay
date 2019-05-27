package models.template

import com.kyleu.projectile.models.menu.{MenuProvider, SystemMenu}
import com.kyleu.projectile.models.user.SystemUser

object UserMenu extends MenuProvider {
  override def userMenu(u: SystemUser) = ComponentMenu.menu :+ SystemMenu.currentMenu(u.role)
}
