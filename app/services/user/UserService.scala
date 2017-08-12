package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.queries.auth._
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.{Role, User}
import services.ModelServiceHelper
import util.FutureUtils.databaseContext
import services.database.Database
import util.cache.UserCache

import scala.concurrent.Future

@javax.inject.Singleton
class UserService @javax.inject.Inject() (hasher: PasswordHasher) extends ModelServiceHelper[User] {
  def getById(id: UUID) = Database.query(UserQueries.getById(Seq(id)))
  def getByIdSeq(idSeq: Seq[UUID]) = Database.query(UserQueries.getByIdSeq(idSeq))

  override def countAll(filters: Seq[Filter] = Nil) = Database.query(UserQueries.countAll(filters))
  override def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(UserQueries.getAll(filters, orderBys, limit, offset))
  }

  override def searchCount(q: String, filters: Seq[Filter]) = Database.query(UserQueries.searchCount(q, filters))
  override def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = {
    Database.query(UserQueries.search(q, filters, orderBys, limit, offset))
  }

  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = {
    Database.query(UserQueries.searchExact(q, orderBys, limit, offset))
  }

  def isUsernameInUse(name: String) = Database.query(UserQueries.IsUsernameInUse(name))
  def usernameLookup(id: UUID) = Database.query(UserQueries.GetUsername(id))

  def save(user: User, update: Boolean = false) = {
    log.info(s"${if (update) { "Updating" } else { "Creating" }} user [$user].")
    val statement = if (update) {
      UserQueries.UpdateUser(user)
    } else {
      UserQueries.insert(user)
    }
    Database.execute(statement).map { _ =>
      UserCache.cacheUser(user)
      user
    }
  }

  def usernameLookupMulti(ids: Set[UUID]) = if (ids.isEmpty) {
    Future.successful(Map.empty[UUID, String])
  } else {
    Database.query(UserQueries.GetUsernames(ids))
  }

  def remove(userId: UUID) = Database.transaction { conn =>
    val startTime = System.nanoTime
    val f = getById(userId).flatMap {
      case Some(user) => Database.execute(PasswordInfoQueries.removeById(Seq(user.profile.providerID, user.profile.providerKey)), Some(conn))
      case None => throw new IllegalStateException("Invalid User")
    }
    f.flatMap { _ =>
      Database.execute(UserQueries.removeById(Seq(userId)), Some(conn)).map { users =>
        UserCache.removeUser(userId)
        val timing = ((System.nanoTime - startTime) / 1000000).toInt
        Map("users" -> users, "timing" -> timing)
      }
    }
  }

  def enableAdmin(user: User) = Database.query(UserQueries.CountAdmins).flatMap { adminCount =>
    if (adminCount == 0) {
      Database.execute(UserQueries.SetRole(user.id, Role.Admin)).map(_ => UserCache.removeUser(user.id))
    } else {
      throw new IllegalStateException("An admin already exists.")
    }
  }

  def update(id: UUID, username: String, email: String, password: Option[String], role: Role, originalEmail: String) = {
    Database.execute(UserQueries.UpdateFields(id, username, email, role)).flatMap { _ =>
      val emailUpdated = if (email != originalEmail) {
        Database.execute(PasswordInfoQueries.UpdateEmail(originalEmail, email))
      } else {
        Future.successful(0)
      }
      emailUpdated.flatMap { _ =>
        password match {
          case Some(p) =>
            val loginInfo = LoginInfo(CredentialsProvider.ID, email)
            val authInfo = hasher.hash(p)
            Database.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo))
          case _ => Future.successful(id)
        }
      }.map { _ =>
        UserCache.removeUser(id)
        id
      }
    }
  }
}
