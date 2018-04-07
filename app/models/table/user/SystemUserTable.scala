package models.table.user

import java.time.LocalDateTime
import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.user.{Role, SystemUser, UserPreferences}
import services.database.SlickQueryService.imports._
import slick.jdbc.JdbcType
import util.JsonSerializers._

object SystemUserTable {
  implicit val loginInfoColumnType: JdbcType[LoginInfo] = MappedColumnType.base[LoginInfo, String](
    tmap = b => b.providerKey,
    tcomap = i => LoginInfo(CredentialsProvider.ID, i)
  )
  implicit val roleColumnType: JdbcType[Role] = MappedColumnType.base[Role, String](
    tmap = b => b.toString,
    tcomap = i => Role.withValue(i)
  )
  implicit val userPreferencesColumnType: JdbcType[UserPreferences] = MappedColumnType.base[UserPreferences, String](
    tmap = b => b.asJson.spaces2,
    tcomap = i => UserPreferences.readFrom(i)
  )

  val query = TableQuery[SystemUserTable]
}

class SystemUserTable(tag: Tag) extends Table[SystemUser](tag, "system_users") {
  import models.table.user.SystemUserTable.{loginInfoColumnType, roleColumnType, userPreferencesColumnType}

  def id = column[UUID]("id", O.PrimaryKey)
  def username = column[String]("username")
  def preferences = column[UserPreferences]("prefs")
  def profile = column[LoginInfo]("key")
  def role = column[Role]("role")
  def created = column[LocalDateTime]("created")

  override val * = (id, username, preferences, profile, role, created) <> ((SystemUser.apply _).tupled, SystemUser.unapply)
}
