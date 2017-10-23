package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import models.queries.auth.PasswordInfoQueries
import services.database.ApplicationDatabase
import util.tracing.TracingService

import scala.concurrent.Future

@javax.inject.Singleton
class PasswordInfoService @javax.inject.Inject() (tracingService: TracingService) extends DelegableAuthInfoDAO[PasswordInfo] {

  override def find(loginInfo: LoginInfo) = tracingService.noopTrace("password.find") { implicit td =>
    Future.successful(ApplicationDatabase.query(PasswordInfoQueries.getByPrimaryKey(Seq(loginInfo.providerID, loginInfo.providerKey))))
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = tracingService.noopTrace("password.add") { implicit td =>
    ApplicationDatabase.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo))
    Future.successful(authInfo)
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = tracingService.noopTrace("password.update") { implicit td =>
    ApplicationDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo))
    Future.successful(authInfo)
  }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = tracingService.noopTrace("password.save") { implicit td =>
    ApplicationDatabase.transaction { (txTd, conn) =>
      val rowsAffected = ApplicationDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo), Some(conn))(txTd)
      if (rowsAffected == 0) {
        ApplicationDatabase.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo), Some(conn))(txTd)
      }
    }
    Future.successful(authInfo)
  }

  override def remove(loginInfo: LoginInfo) = tracingService.topLevelTrace("password.remove") { implicit td =>
    Future.successful(ApplicationDatabase.execute(PasswordInfoQueries.removeByPrimaryKey(Seq(loginInfo.providerID, loginInfo.providerKey))))
  }
}
