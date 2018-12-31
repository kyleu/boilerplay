package services.user

import com.kyleu.projectile.models.user.SystemUser
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.queries.auth.SystemUserQueries
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.Logging
import services.cache.UserCache
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.util.tracing.OpenTracingService

import scala.concurrent.Future

@javax.inject.Singleton
class SystemUserSearchService @javax.inject.Inject() (tracingService: OpenTracingService) extends IdentityService[SystemUser] with Logging {
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
