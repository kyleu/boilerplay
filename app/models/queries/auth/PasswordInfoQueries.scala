package models.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.queries.BaseQueries
import models.database.{DatabaseField, Row, Statement}
import util.DateUtils

object PasswordInfoQueries extends BaseQueries[PasswordInfo]("password.info", "password_info") {
  override val fields = Seq(
    DatabaseField("provider"), DatabaseField("key"), DatabaseField("hasher"), DatabaseField("password"), DatabaseField("salt"), DatabaseField("created")
  )
  override protected val pkColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("key")

  val getByPrimaryKey = GetByPrimaryKey
  val removeByPrimaryKey = RemoveByPrimaryKey

  case class CreatePasswordInfo(l: LoginInfo, p: PasswordInfo) extends Statement {
    override val name = s"$key.create.password.info"
    override val sql = insertSql
    override val values = Seq(l.providerID, l.providerKey) ++ toDataSeq(p)
  }

  case class UpdatePasswordInfo(l: LoginInfo, p: PasswordInfo) extends Statement {
    override val name = s"$key.update.password.info"
    override val sql = s"""update ${quote(tableName)}
      set ${quote("hasher")} = ?, ${quote("password")} = ?, ${quote("salt")} = ?, ${quote("created")} = ?
      where ${quote("provider")} = ? and ${quote("key")} = ?
    """.trim
    override val values = toDataSeq(p) ++ Seq(l.providerID, l.providerKey)
  }

  override protected def fromRow(row: Row) = PasswordInfo(
    hasher = row.as[String]("hasher"),
    password = row.as[String]("password"),
    salt = row.asOpt[String]("salt")
  )

  override protected def toDataSeq(p: PasswordInfo) = Seq(p.hasher, p.password, p.salt, DateUtils.now.toString)

  case class UpdateEmail(originalEmail: String, email: String) extends Statement {
    override val name = s"$key.update.email"
    override val sql = s"""update ${quote(tableName)} set ${quote("key")} = ? where ${quote("key")} = ? and ${quote("provider")} = ?"""
    override val values = Seq(email, originalEmail, CredentialsProvider.ID)
  }
}
