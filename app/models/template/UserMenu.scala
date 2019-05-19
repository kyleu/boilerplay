package models.template

import com.kyleu.projectile.models.menu.{MenuProvider, SystemMenu}
import com.kyleu.projectile.models.user.SystemUser

object UserMenu extends MenuProvider {
  private[this] lazy val staticMenu = ComponentMenu.menu :+ SystemMenu.currentMenu

  override def adminMenu(u: SystemUser) = standardMenu(u) ++ staticMenu
}
