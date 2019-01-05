/* Generated File */
package models.doobie.auth

import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.auth.PasswordInfoRow

object PasswordInfoRowDoobie extends DoobieQueries[PasswordInfoRow]("password_info") {
  override val countFragment = fr"""select count(*) from "password_info""""
  override val selectFragment = fr"""select "provider", "key", "hasher", "password", "salt", "created" from "password_info""""

  override val columns = Seq("provider", "key", "hasher", "password", "salt", "created")
  override val searchColumns = Seq("provider", "key")

  override def searchFragment(q: String) = {
    fr""""provider"::text = $q or "key"::text = $q or "hasher"::text = $q or "password"::text = $q or "salt"::text = $q or "created"::text = $q"""
  }

  // def getByPrimaryKey(provider: String, key: String) = ???
}
