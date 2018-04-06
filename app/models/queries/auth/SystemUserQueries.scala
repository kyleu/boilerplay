package models.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.database.DatabaseFieldType._
import models.database._
import models.queries.BaseQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.{Role, SystemUser, UserPreferences}
import util.JsonSerializers._

object SystemUserQueries extends BaseQueries[SystemUser]("systemUser", "system_users") {
  override val fields = Seq(
    DatabaseField("id"), DatabaseField("username"), DatabaseField("prefs"), DatabaseField("email"), DatabaseField("role"), DatabaseField("created")
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "username", "email")

  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: String, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: String, filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq(id), fields)

  final case class UpdateUser(u: SystemUser) extends Statement {
    override val name = s"$key.update.user"
    override val sql = updateSql(Seq("username", "prefs", "email", "role"))
    override val values = Seq[Any](u.username, u.preferences.asJson.spaces2, u.profile.providerKey, u.role.toString, u.id)
  }

  final case class SetPreferences(userId: UUID, prefs: UserPreferences) extends Statement {
    override val name = s"$key.set.preferences"
    override val sql = updateSql(Seq("prefs"))
    override val values = Seq[Any](prefs.asJson.spaces2, userId)
  }

  final case class SetRole(id: UUID, role: Role) extends Statement {
    override val name = s"$key.set.role"
    override val sql = s"update ${quote(tableName)} set ${quote("role")} = ? where ${quote("id")} = ?"
    override val values = Seq[Any](role.toString, id)
  }

  final case class FindUserByUsername(username: String) extends FlatSingleRowQuery[SystemUser] {
    override val name = s"$key.find.by.username"
    override val sql = getSql(Some(quote("username") + " = ?"))
    override val values = Seq(username)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  final case class FindUserByProfile(loginInfo: LoginInfo) extends FlatSingleRowQuery[SystemUser] {
    override val name = s"$key.find.by.profile"
    override val sql = getSql(Some(quote("email") + " = ?"))
    override val values = Seq(loginInfo.providerKey)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  override protected def fromRow(row: Row) = {
    val id = UuidType(row, "id")
    val username = StringType(row, "username")
    val prefsString = StringType(row, "prefs")
    val preferences = UserPreferences.readFrom(prefsString)
    val profile = LoginInfo("credentials", StringType(row, "email"))
    val role = Role.withNameInsensitive(StringType(row, "role").trim)
    val created = TimestampType(row, "created")
    SystemUser(id, username, preferences, profile, role, created)
  }

  override protected def toDataSeq(u: SystemUser) = Seq[Any](
    u.id.toString, u.username, u.preferences.asJson.spaces2, u.profile.providerKey, u.role.toString, u.created
  )
}
