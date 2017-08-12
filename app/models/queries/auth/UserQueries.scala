package models.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.database._
import models.queries.BaseQueries
import models.result.filter.Filter
import models.user.{Role, User, UserPreferences}
import util.JsonSerializers

object UserQueries extends BaseQueries[User] {
  override protected val tableName = "users"
  override protected val fields = Seq(
    DatabaseField("id"), DatabaseField("username"), DatabaseField("prefs"), DatabaseField("email"), DatabaseField("role"), DatabaseField("created")
  )
  override protected val searchColumns = Seq("id", "username", "email")

  val insert = Insert
  val getById = GetById
  def getByIdSeq(idSeq: Seq[UUID]) = new ColSeqQuery("id", idSeq)

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

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
    override val values = Seq(u.username, JsonSerializers.writePreferences(u.preferences), u.profile.providerKey, u.role.toString, u.id)
  }

  case class SetPreferences(userId: UUID, userPreferences: UserPreferences) extends Statement {
    override val sql = updateSql(Seq("prefs"))
    override val values = Seq(JsonSerializers.writePreferences(userPreferences), userId)
  }

  case class SetRole(id: UUID, role: Role) extends Statement {
    override val sql = s"""update "$tableName" set \"role\" = ? where \"id\" = ?"""
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

  case class UpdateFields(id: UUID, username: String, email: String, role: Role) extends Statement {
    override val sql = s"""update "$tableName" set "username" = ?, "email" = ?, "role" = ? where "id" = ?"""
    override val values = Seq(username, email, role.toString, id)
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val username = row.as[String]("username")
    val prefsString = row.as[String]("prefs")
    val preferences = JsonSerializers.readPreferences(prefsString)
    val profile = LoginInfo("credentials", row.as[String]("email"))
    val role = Role.withName(row.as[String]("role").trim)
    val created = fromJoda(row.as[org.joda.time.LocalDateTime]("created"))
    User(id, username, preferences, profile, role, created)
  }

  override protected def toDataSeq(u: User) = {
    Seq(u.id, u.username, JsonSerializers.writePreferences(u.preferences), u.profile.providerKey, u.role.toString, toJoda(u.created))
  }
}
