package models.table.user

import java.time.LocalDateTime
import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.user.{Role, SystemUser, UserPreferences}
import services.database.SlickQueryService.imports._
import slick.jdbc.JdbcType
import util.JsonSerializers._

object SystemUserTable {
  implicit val loginInfoColumnType: JdbcType[LoginInfo] = MappedColumnType.base[LoginInfo, String](
    tmap = b => b.providerKey,
    tcomap = i => LoginInfo("credentials", i)
  )
  implicit val roleColumnType: JdbcType[Role] = MappedColumnType.base[Role, String](
    tmap = b => b.toString,
    tcomap = i => Role.withName(i)
  )
  implicit val userPreferencesColumnType: JdbcType[UserPreferences] = MappedColumnType.base[UserPreferences, String](
    tmap = b => b.asJson.spaces2,
    tcomap = i => UserPreferences.readFrom(i)
  )

  val query = TableQuery[SystemUserTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(ids: Seq[UUID]) = query.filter(_.id.inSet(ids)).result

  def getByRoleSeq(roles: Seq[Role]) = query.filter(_.role.inSet(roles)).result

  def removeByPrimaryKey(id: UUID) = query.filter(_.id === id).delete

  def insert(model: SystemUser) = query += model
  def insertBatch(seq: Seq[SystemUser]) = query ++= seq
}

class SystemUserTable(tag: Tag) extends Table[SystemUser](tag, "system_users") {
  import models.table.user.SystemUserTable.{loginInfoColumnType, roleColumnType, userPreferencesColumnType}

  def id = column[UUID]("id", O.PrimaryKey)
  def username = column[String]("username")
  def preferences = column[UserPreferences]("prefs")
  def profile = column[LoginInfo]("email")
  def role = column[Role]("role")
  def created = column[LocalDateTime]("created")

  override val * = (id, username, preferences, profile, role, created) <> ((SystemUser.apply _).tupled, SystemUser.unapply)
}
