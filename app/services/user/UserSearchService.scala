package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.AuthQueries
import models.user.RichUser
import services.auth.AuthService
import util.FutureUtils.defaultContext
import services.database.Database
import util.Logging
import util.cache.UserCache

import scala.concurrent.Future

@javax.inject.Singleton
class UserSearchService @javax.inject.Inject() (authService: AuthService) extends IdentityService[RichUser] with Logging {
  def retrieve(id: UUID) = UserCache.getUser(id) match {
    case Some(u) => Future.successful(Some(u))
    case None => authService.getById(id).map { x =>
      x.foreach(UserCache.cacheUser)
      x
    }
  }

  def getUsername(id: UUID) = UserCache.getUser(id).map(_.username) match {
    case Some(un) => Future.successful(un)
    case None => Database.query(AuthQueries.GetUsername(id))
  }

  def retrieve(username: String) = Database.query(AuthQueries.FindUserByUsername(username))

  override def retrieve(loginInfo: LoginInfo) = UserCache.getUserByLoginInfo(loginInfo) match {
    case Some(u) => Future.successful(Some(u))
    case None => Database.query(AuthQueries.FindUserByProfile(loginInfo)).map { x =>
      x.foreach(UserCache.cacheUser)
      x
    }
  }
}
