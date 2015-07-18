package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import models.database.queries.auth.OAuth1InfoQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database

import scala.concurrent.Future

object OAuth1InfoService extends DelegableAuthInfoDAO[OAuth1Info] {
  override def find(loginInfo: LoginInfo) = Database.query(OAuth1InfoQueries.getById(Seq(loginInfo.providerID, loginInfo.providerKey)))

  override def save(loginInfo: LoginInfo, authInfo: OAuth1Info) = {
    Database.transaction { conn =>
      Database.execute(OAuth1InfoQueries.UpdateOAuth1Info(loginInfo, authInfo), Some(conn)).flatMap { rowsAffected =>
        if (rowsAffected == 0) {
          Database.execute(OAuth1InfoQueries.CreateOAuth1Info(loginInfo, authInfo), Some(conn)).map(x => authInfo)
        } else {
          Future.successful(authInfo)
        }
      }
    }
  }

  override def add(loginInfo: LoginInfo, authInfo: OAuth1Info) = {
    Database.execute(OAuth1InfoQueries.CreateOAuth1Info(loginInfo, authInfo)).map(x => authInfo)
  }

  override def update(loginInfo: LoginInfo, authInfo: OAuth1Info) = {
    Database.execute(OAuth1InfoQueries.UpdateOAuth1Info(loginInfo, authInfo)).map(x => authInfo)
  }

  override def remove(loginInfo: LoginInfo) = {
    Database.execute(OAuth1InfoQueries.removeById(Seq(loginInfo.providerID, loginInfo.providerKey))).map(x => Unit)
  }
}
