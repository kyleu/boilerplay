package models.database.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.database.queries.BaseQueries
import models.database.{ Row, Query, FlatSingleRowQuery }
import utils.DateUtils

object ProfileQueries extends BaseQueries[CommonSocialProfile] {
  override protected val tableName = "user_profiles"
  override protected val columns = Seq("provider", "key", "email", "first_name", "last_name", "full_name", "avatar_url", "created")
  override protected val searchColumns = Seq("key", "email", "first_name", "last_name", "full_name")

  val insert = Insert
  val remove = RemoveById

  case class FindProfile(provider: String, key: String) extends FlatSingleRowQuery[CommonSocialProfile] {
    override val sql = getSql(Some("provider = ? and key = ?"))
    override val values = Seq(provider, key)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  case class FindProfilesByUser(id: UUID) extends Query[List[CommonSocialProfile]] {
    override val sql = s"select ${columns.mkString(", ")} from $tableName p " +
      "where (p.provider || ':' || p.key) in (select unnest(profiles) from users where users.id = ?)"
    override val values = Seq(id)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  override protected def fromRow(row: Row) = {
    val loginInfo = LoginInfo(
      providerID = row.as[String]("provider"),
      providerKey = row.as[String]("key")
    )
    val firstName = row.asOpt[String]("first_name")
    val lastName = row.asOpt[String]("last_name")
    val fullName = row.asOpt[String]("full_name")
    val email = row.asOpt[String]("email")
    val avatarUrl = row.asOpt[String]("avatar_url")

    CommonSocialProfile(loginInfo, firstName, lastName, fullName, email, avatarUrl)
  }

  override protected def toDataSeq(p: CommonSocialProfile) = Seq(
    p.loginInfo.providerID, p.loginInfo.providerKey, p.email, p.firstName, p.lastName, p.fullName, p.avatarURL, DateUtils.now
  )
}
