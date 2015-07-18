package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import models.database.queries.auth.PasswordInfoQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database

import scala.concurrent.Future

object PasswordInfoService extends DelegableAuthInfoDAO[PasswordInfo] {
  override def find(loginInfo: LoginInfo) = Database.query(PasswordInfoQueries.getById(Seq(loginInfo.providerID, loginInfo.providerKey)))

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    Database.transaction { conn =>
      Database.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo), Some(conn)).flatMap { rowsAffected =>
        if (rowsAffected == 0) {
          Database.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo), Some(conn)).map(x => authInfo)
        } else {
          Future.successful(authInfo)
        }
      }
    }
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    Database.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo)).map(x => authInfo)
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    Database.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo)).map(x => authInfo)
  }

  override def remove(loginInfo: LoginInfo) = {
    Database.execute(PasswordInfoQueries.removeById(Seq(loginInfo.providerID, loginInfo.providerKey))).map(x => Unit)
  }
}
