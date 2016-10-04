package models.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.queries.BaseQueries
import models.database.{Row, Statement}
import utils.DateUtils

object PasswordInfoQueries extends BaseQueries[PasswordInfo] {
  override protected val tableName = "password_info"
  override protected val columns = Seq("provider", "key", "hasher", "password", "salt", "created")
  override protected def idColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("key")

  val getById = GetById
  val removeById = RemoveById

  case class CreatePasswordInfo(l: LoginInfo, p: PasswordInfo) extends Statement {
    override val sql = insertSql
    override val values = Seq(l.providerID, l.providerKey) ++ toDataSeq(p)
  }

  case class UpdatePasswordInfo(l: LoginInfo, p: PasswordInfo) extends Statement {
    override val sql = s"""update "$tableName" set "hasher" = ?, "password" = ?, "salt" = ?, "created" = ? where "provider" = ? and "key" = ?"""
    override val values = toDataSeq(p) ++ Seq(l.providerID, l.providerKey)
  }

  override protected def fromRow(row: Row) = PasswordInfo(
    hasher = row.as[String]("hasher"),
    password = row.as[String]("password"),
    salt = row.asOpt[String]("salt")
  )

  override protected def toDataSeq(p: PasswordInfo) = Seq(p.hasher, p.password, p.salt, DateUtils.now)

  case class UpdateEmail(originalEmail: String, email: String) extends Statement {
    override val sql = s"""update $tableName set "key" = ? where "key" = ? and "provider" = ?"""
    override val values = Seq(email, originalEmail, CredentialsProvider.ID)
  }
}
