/* Generated File */
package models.doobie.auth

import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.auth.Oauth2InfoRow

object Oauth2InfoRowDoobie extends DoobieQueries[Oauth2InfoRow]("oauth2_info") {
  override val countFragment = fr"""select count(*) from "oauth2_info""""
  override val selectFragment = fr"""select "provider", "key", "access_token", "token_type", "expires_in", "refresh_token", "params", "created" from "oauth2_info""""

  override val columns = Seq("provider", "key", "access_token", "token_type", "expires_in", "refresh_token", "params", "created")
  override val searchColumns = Seq("provider", "key")

  override def searchFragment(q: String) = {
    fr""""provider"::text = $q or "key"::text = $q or "access_token"::text = $q or "token_type"::text = $q or "expires_in"::text = $q or "refresh_token"::text = $q or "params"::text = $q or "created"::text = $q"""
  }

  // def getByPrimaryKey(provider: String, key: String) = ???
}
