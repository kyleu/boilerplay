package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import models.queries.auth.PasswordInfoQueries
import util.FutureUtils.databaseContext
import services.database.MasterDatabase
import util.tracing.TracingService

import scala.concurrent.Future

@javax.inject.Singleton
class PasswordInfoService @javax.inject.Inject() (tracingService: TracingService) extends DelegableAuthInfoDAO[PasswordInfo] {

  override def find(loginInfo: LoginInfo) = tracingService.noopTrace("password.find") { implicit td =>
    MasterDatabase.query(PasswordInfoQueries.getByPrimaryKey(Seq(loginInfo.providerID, loginInfo.providerKey)))
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = tracingService.noopTrace("password.add") { implicit td =>
    MasterDatabase.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo)).map { _ => authInfo }
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = tracingService.noopTrace("password.update") { implicit td =>
    MasterDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo)).map { _ => authInfo }
  }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = tracingService.noopTrace("password.save") { implicit td =>
    MasterDatabase.transaction { (txTd, conn) =>
      MasterDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo), Some(conn))(txTd).flatMap { rowsAffected =>
        if (rowsAffected == 0) {
          MasterDatabase.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo), Some(conn))(txTd).map { _ => authInfo }
        } else {
          Future.successful(authInfo)
        }
      }
    }
  }

  override def remove(loginInfo: LoginInfo) = tracingService.topLevelTrace("password.remove") { implicit td =>
    MasterDatabase.execute(PasswordInfoQueries.removeByPrimaryKey(Seq(loginInfo.providerID, loginInfo.providerKey))).map(_ => Unit)
  }
}
