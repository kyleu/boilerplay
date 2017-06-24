package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import models.queries.auth.PasswordInfoQueries
import utils.FutureUtils.defaultContext
import services.database.Database

import scala.concurrent.Future

@javax.inject.Singleton
class PasswordInfoService @javax.inject.Inject() () extends DelegableAuthInfoDAO[PasswordInfo] {
  def getByLoginInfo(loginInfo: LoginInfo) = Database.query(PasswordInfoQueries.getById(Seq(loginInfo.providerID, loginInfo.providerKey)))

  override def find(loginInfo: LoginInfo) = {
    getByLoginInfo(loginInfo)
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    Database.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo)).map { _ => authInfo }
  }

  def updatePassword(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    Database.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo))
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    updatePassword(loginInfo, authInfo).map { _ => authInfo }
  }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = Database.transaction { conn =>
    Database.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo)).flatMap { rowsAffected =>
      if (rowsAffected == 0) {
        Database.execute(PasswordInfoQueries.CreatePasswordInfo(loginInfo, authInfo)).map { _ => authInfo }
      } else {
        Future.successful(authInfo)
      }
    }
  }

  override def remove(loginInfo: LoginInfo) = {
    Database.execute(PasswordInfoQueries.removeById(Seq(loginInfo.providerID, loginInfo.providerKey))).map(_ => Unit)
  }
}
