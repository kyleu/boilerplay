package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.UserQueries
import models.user.User
import services.database.SystemDatabase
import util.Logging
import services.cache.UserCache
import util.FutureUtils.databaseContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class UserSearchService @javax.inject.Inject() (tracingService: TracingService) extends IdentityService[User] with Logging {
  override def retrieve(loginInfo: LoginInfo) = tracingService.noopTrace("user.retreive") { implicit td =>
    UserCache.getUserByLoginInfo(loginInfo) match {
      case Some(u) => Future.successful(Some(u))
      case None => SystemDatabase.query(UserQueries.FindUserByProfile(loginInfo))(td).map { x =>
        x.foreach(UserCache.cacheUser)
        x
      }
    }
  }

  def getByLoginInfo(loginInfo: LoginInfo)(implicit trace: TraceData) = tracingService.trace("user.get.by.login.info") { td =>
    UserCache.getUserByLoginInfo(loginInfo) match {
      case Some(u) => Future.successful(Some(u))
      case None => SystemDatabase.query(UserQueries.FindUserByProfile(loginInfo))(td).map { x =>
        x.foreach(UserCache.cacheUser)
        x
      }
    }
  }
}
