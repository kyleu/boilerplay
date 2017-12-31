package models.table.user

import java.time.LocalDateTime
import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.table.QueryTypes._
import models.user.{Role, SystemUser, UserPreferences}
import services.database.SlickQueryService.imports._
import util.JsonSerializers.{readPreferences, writePreferences}

object SystemUserTable {
  implicit val loginInfoColumnType = MappedColumnType.base[LoginInfo, String](b => b.providerKey, i => LoginInfo("credentials", i))
  implicit val roleColumnType = MappedColumnType.base[Role, String](b => b.toString, i => Role.withName(i))
  implicit val userPreferencesColumnType = MappedColumnType.base[UserPreferences, String](b => writePreferences(b), i => readPreferences(i))

  val query = TableQuery[SystemUserTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
}

class SystemUserTable(tag: Tag) extends Table[SystemUser](tag, "system_user") {
  import SystemUserTable.{loginInfoColumnType, roleColumnType, userPreferencesColumnType}

  def id = column[UUID]("id", O.PrimaryKey)
  def username = column[String]("username")
  def preferences = column[UserPreferences]("preferences")
  def profile = column[LoginInfo]("profile")
  def role = column[Role]("role")
  def created = column[LocalDateTime]("created")

  override val * = (id, username, preferences, profile, role, created) <> ((SystemUser.apply _).tupled, SystemUser.unapply)
}
