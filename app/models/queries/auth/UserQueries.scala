package models.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.database._
import models.queries.BaseQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.user.{Role, User, UserPreferences}
import util.JsonSerializers

object UserQueries extends BaseQueries[User] {
  override val tableName = "users"
  override val fields = Seq(
    DatabaseField("id"), DatabaseField("username"), DatabaseField("prefs"), DatabaseField("email"), DatabaseField("role"), DatabaseField("created")
  )
  override protected val searchColumns = Seq("id", "username", "email")

  val insert = Insert
  val getByPrimaryKey = GetByPrimaryKey
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery("id", idSeq)

  def getByRole(role: Role) = new SeqQuery(s"where ${quote("role")} = ?", Seq(role))
  def getByRoleSeq(roleSeq: Seq[Role]) = new ColSeqQuery("role", roleSeq.map(_.toString))

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  val removeByPrimaryKey = RemoveByPrimaryKey
  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq(id), fields)

  case class UpdateUser(u: User) extends Statement {
    override val sql = updateSql(Seq("username", "prefs", "email", "role"))
    override val values = Seq(u.username, JsonSerializers.writePreferences(u.preferences), u.profile.providerKey, u.role.toString, u.id)
  }

  case class SetPreferences(userId: UUID, userPreferences: UserPreferences) extends Statement {
    override val sql = updateSql(Seq("prefs"))
    override val values = Seq(JsonSerializers.writePreferences(userPreferences), userId)
  }

  case class SetRole(id: UUID, role: Role) extends Statement {
    override val sql = s"update ${quote(tableName)} set ${quote("role")} = ? where ${quote("id")} = ?"
    override val values = Seq(role.toString, id)
  }

  case class FindUserByUsername(username: String) extends FlatSingleRowQuery[User] {
    override val sql = getSql(Some(quote("username") + " = ?"))
    override val values = Seq(username)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  case class FindUserByProfile(loginInfo: LoginInfo) extends FlatSingleRowQuery[User] {
    override val sql = getSql(Some(quote("email") + " = ?"))
    override val values = Seq(loginInfo.providerKey)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  override protected def fromRow(row: Row) = {
    val id = UUID.fromString(row.as[String]("id"))
    val username = row.as[String]("username")
    val prefsString = row.as[String]("prefs")
    val preferences = JsonSerializers.readPreferences(prefsString)
    val profile = LoginInfo("credentials", row.as[String]("email"))
    val role = Role.withName(row.as[String]("role").trim)
    val created = fromJoda(row.as[org.joda.time.LocalDateTime]("created"))
    User(id, username, preferences, profile, role, created)
  }

  override protected def toDataSeq(u: User) = {
    Seq(u.id.toString, u.username, JsonSerializers.writePreferences(u.preferences), u.profile.providerKey, u.role.toString, toJoda(u.created))
  }
}
