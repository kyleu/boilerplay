package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.UserQueries
import models.user.User
import utils.FutureUtils.defaultContext
import services.database.Database
import utils.Logging
import utils.cache.UserCache

import scala.concurrent.Future

@javax.inject.Singleton
class UserSearchService @javax.inject.Inject() () extends IdentityService[User] with Logging {
  def retrieve(id: UUID) = UserCache.getUser(id) match {
    case Some(u) => Future.successful(Some(u))
    case None => Database.query(UserQueries.getById(Seq(id))).map { x =>
      x.foreach(UserCache.cacheUser)
      x
    }
  }

  def getUsername(id: UUID) = UserCache.getUser(id).map(_.username) match {
    case Some(un) => Future.successful(un)
    case None => Database.query(UserQueries.GetUsername(id))
  }

  def retrieve(username: String) = Database.query(UserQueries.FindUserByUsername(username))

  override def retrieve(loginInfo: LoginInfo) = UserCache.getUserByLoginInfo(loginInfo) match {
    case Some(u) => Future.successful(Some(u))
    case None => Database.query(UserQueries.FindUserByProfile(loginInfo)).map { x =>
      x.foreach(UserCache.cacheUser)
      x
    }
  }
}
