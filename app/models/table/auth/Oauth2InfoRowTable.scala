/* Generated File */
package models.table.auth

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import io.circe.Json
import java.time.LocalDateTime
import models.auth.Oauth2InfoRow

object Oauth2InfoRowTable {
  val query = TableQuery[Oauth2InfoRowTable]

  def getByPrimaryKey(provider: String, key: String) = query.filter(o => o.provider === provider && o.key === key).result.headOption
}

class Oauth2InfoRowTable(tag: slick.lifted.Tag) extends Table[Oauth2InfoRow](tag, "oauth2_info") {
  val provider = column[String]("provider")
  val key = column[String]("key")
  val accessToken = column[String]("access_token")
  val tokenType = column[Option[String]]("token_type")
  val expiresIn = column[Option[Long]]("expires_in")
  val refreshToken = column[Option[String]]("refresh_token")
  val params = column[Option[Json]]("params")
  val created = column[LocalDateTime]("created")

  val modelPrimaryKey = primaryKey("pk_oauth2_info", (provider, key))

  override val * = (provider, key, accessToken, tokenType, expiresIn, refreshToken, params, created) <> (
    (Oauth2InfoRow.apply _).tupled,
    Oauth2InfoRow.unapply
  )
}

