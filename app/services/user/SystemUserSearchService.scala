package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.SystemUserQueries
import models.user.SystemUser
import services.database.ApplicationDatabase
import util.Logging
import services.cache.UserCache
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class SystemUserSearchService @javax.inject.Inject() (tracingService: TracingService) extends IdentityService[SystemUser] with Logging {
  override def retrieve(loginInfo: LoginInfo) = tracingService.noopTrace("user.retreive") { implicit td =>
    UserCache.getUserByLoginInfo(loginInfo) match {
      case Some(u) => Future.successful(Some(u))
      case None =>
        val u = ApplicationDatabase.query(SystemUserQueries.FindUserByProfile(loginInfo))(td)
        u.foreach(UserCache.cacheUser)
        Future.successful(u)
    }
  }

  def getByLoginInfo(loginInfo: LoginInfo)(implicit trace: TraceData) = tracingService.trace("user.get.by.login.info") { td =>
    UserCache.getUserByLoginInfo(loginInfo) match {
      case Some(u) => Future.successful(Some(u))
      case None =>
        val u = ApplicationDatabase.query(SystemUserQueries.FindUserByProfile(loginInfo))(td)
        u.foreach(UserCache.cacheUser)
        Future.successful(u)
    }
  }
}
