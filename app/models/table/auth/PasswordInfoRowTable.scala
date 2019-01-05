/* Generated File */
package models.table.auth

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.LocalDateTime
import models.auth.PasswordInfoRow

object PasswordInfoRowTable {
  val query = TableQuery[PasswordInfoRowTable]

  def getByPrimaryKey(provider: String, key: String) = query.filter(o => o.provider === provider && o.key === key).result.headOption
}

class PasswordInfoRowTable(tag: slick.lifted.Tag) extends Table[PasswordInfoRow](tag, "password_info") {
  val provider = column[String]("provider")
  val key = column[String]("key")
  val hasher = column[String]("hasher")
  val password = column[String]("password")
  val salt = column[Option[String]]("salt")
  val created = column[LocalDateTime]("created")

  val modelPrimaryKey = primaryKey("pk_password_info", (provider, key))

  override val * = (provider, key, hasher, password, salt, created) <> (
    (PasswordInfoRow.apply _).tupled,
    PasswordInfoRow.unapply
  )
}

