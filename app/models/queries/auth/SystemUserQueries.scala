package models.queries.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.database.DatabaseFieldType._
import models.database._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.{Role, SystemUser, UserPreferences}
import util.JsonSerializers._

object SystemUserQueries extends BaseQueries[SystemUser]("systemUser", "system_users") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Username", prop = "username", col = "username", typ = StringType),
    DatabaseField(title = "Preferences", prop = "prefs", col = "prefs", typ = StringType),
    DatabaseField(title = "Provider", prop = "provider", col = "provider", typ = StringType),
    DatabaseField(title = "Key", prop = "key", col = "key", typ = StringType),
    DatabaseField(title = "Role", prop = "role", col = "role", typ = EnumType(Role)),
    DatabaseField(title = "Created", prop = "created", col = "created", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "username", "provider", "key")

  def getByPrimaryKey(id: UUID) = new GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  final case class CountByRole(role: Role) extends ColCount(column = "role", values = Seq(role))
  final case class GetByRole(role: Role, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("author") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(role)
  )
  final case class GetByRoleSeq(roleSeq: Seq[Role]) extends ColSeqQuery(column = "role", values = roleSeq)

  def insert(model: SystemUser) = new Insert(model)
  def insertBatch(models: Seq[SystemUser]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq(id), fields)

  final case class UpdateUser(u: SystemUser) extends Statement {
    override val name = s"$key.update.user"
    override val sql = updateSql(Seq("username", "prefs", "provider", "key", "role"))
    override val values = Seq[Any](u.username, u.preferences.asJson.spaces2, u.profile.providerID, u.profile.providerKey, u.role.toString, u.id)
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
    override val sql = getSql(Some(quote("provider") + " = ? and " + quote("key") + " = ?"))
    override val values = Seq(loginInfo.providerID, loginInfo.providerKey)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  override protected def fromRow(row: Row) = {
    val id = UuidType(row, "id")
    val username = StringType(row, "username")
    val prefsString = StringType(row, "prefs")
    val preferences = UserPreferences.readFrom(prefsString)
    val profile = LoginInfo(StringType(row, "provider"), StringType(row, "key"))
    val role = Role.withValue(StringType(row, "role").trim)
    val created = TimestampType(row, "created")
    SystemUser(id, username, preferences, profile, role, created)
  }

  override protected def toDataSeq(u: SystemUser) = Seq[Any](
    u.id.toString, u.username, u.preferences.asJson.spaces2, u.profile.providerID, u.profile.providerKey, u.role.toString, u.created
  )
}
