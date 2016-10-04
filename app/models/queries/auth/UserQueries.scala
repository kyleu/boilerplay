package models.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.database._
import models.queries.BaseQueries
import models.user.{Role, User, UserPreferences}
import org.joda.time.LocalDateTime
import upickle.default._
import utils.JsonSerializers.{themeReader, themeWriter}

object UserQueries extends BaseQueries[User] {
  override protected val tableName = "users"
  override protected val columns = Seq("id", "username", "prefs", "email", "role", "created")
  override protected val searchColumns = Seq("id::text", "username")

  val insert = Insert
  val getById = GetById
  def getAll(orderBy: String = "email") = GetAll(orderBy = Some(orderBy))
  val count = Count(s"""select count(*) as c from "$tableName" """)
  val search = Search
  val removeById = RemoveById

  case class IsUsernameInUse(name: String) extends SingleRowQuery[Boolean] {
    override val sql = s"""select count(*) as c from "$tableName" where "username" = ?"""
    override val values = Seq(name)
    override def map(row: Row) = row.as[Long]("c") != 0L
  }

  case class GetUsername(id: UUID) extends Query[Option[String]] {
    override val sql = s"""select "username" from "$tableName" where "id" = ?"""
    override val values = Seq(id)
    override def reduce(rows: Iterator[Row]) = rows.toSeq.headOption.map(_.as[String]("username"))
  }

  case class GetUsernames(ids: Set[UUID]) extends Query[Map[UUID, String]] {
    private[this] val idClause = ids.map(id => s"'$id'").mkString(", ")
    override val sql = s"""select "id", "username" from "$tableName" where "id" in ($idClause)"""
    override def reduce(rows: Iterator[Row]) = rows.map(r => r.as[UUID]("id") -> r.as[String]("username")).toMap
  }

  case class UpdateUser(u: User) extends Statement {
    override val sql = updateSql(Seq("username", "prefs", "email", "role"))
    override val values = Seq(u.username, write(u.preferences), u.profile.providerKey, u.role.toString, u.id)
  }

  case class SetPreferences(userId: UUID, userPreferences: UserPreferences) extends Statement {
    override val sql = updateSql(Seq("prefs"))
    override val values = Seq(write(userPreferences), userId)
  }

  case class SetRole(id: UUID, role: Role) extends Statement {
    override val sql = s"""update \"$tableName\" set \"role\" = ? where \"id\" = ?"""
    override val values = Seq(role.toString, id)
  }

  case class FindUserByUsername(username: String) extends FlatSingleRowQuery[User] {
    override val sql = getSql(Some("\"username\" = ?"))
    override val values = Seq(username)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  case class FindUserByProfile(loginInfo: LoginInfo) extends FlatSingleRowQuery[User] {
    override val sql = getSql(Some("\"email\" = ?"))
    override val values = Seq(loginInfo.providerKey)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  case object CountAdmins extends SingleRowQuery[Int]() {
    override val sql = "select count(*) as c from \"users\" where \"role\" = 'admin'"
    override def map(row: Row) = row.as[Long]("c").toInt
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val username = row.as[String]("username")
    val prefsString = row.as[String]("prefs")
    val preferences = read[UserPreferences](prefsString)
    val profile = LoginInfo("credentials", row.as[String]("email"))
    val role = Role.withName(row.as[String]("role").trim)
    val created = row.as[LocalDateTime]("created")
    User(id, username, preferences, profile, role, created)
  }

  override protected def toDataSeq(u: User) = {
    Seq(u.id, u.username, write(u.preferences), u.profile.providerKey, u.role.toString, u.created)
  }

  case class UpdateFields(id: UUID, username: String, email: String, role: Role) extends Statement {
    override val sql = s"""update "$tableName" set "username" = ?, "email" = ?, "role" = ? where "id" = ?"""
    override val values = Seq(username, email, role.toString, id)
  }
}
