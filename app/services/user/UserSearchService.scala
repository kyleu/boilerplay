package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.UserQueries
import models.user.User
import services.database.Database
import util.Logging
import services.cache.UserCache
import util.FutureUtils.databaseContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class UserSearchService @javax.inject.Inject() (tracingService: TracingService) extends IdentityService[User] with Logging {
  override def retrieve(loginInfo: LoginInfo) = tracingService.topLevelTrace("user.retreive") { implicit td =>
    UserCache.getUserByLoginInfo(loginInfo) match {
      case Some(u) => Future.successful(Some(u))
      case None => Database.query(UserQueries.FindUserByProfile(loginInfo)).map { x =>
        x.foreach(UserCache.cacheUser)
        x
      }
    }
  }
}
