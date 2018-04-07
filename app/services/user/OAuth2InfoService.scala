package services.user

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import models.queries.auth.OAuth2InfoQueries
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.TracingService

import scala.concurrent.Future

@javax.inject.Singleton
class OAuth2InfoService @javax.inject.Inject() (tracingService: TracingService) extends DelegableAuthInfoDAO[OAuth2Info] {

  override def find(loginInfo: LoginInfo) = tracingService.noopTrace("oauth2.find") { implicit td =>
    Future.successful(ApplicationDatabase.query(OAuth2InfoQueries.getByPrimaryKey(loginInfo.providerID, loginInfo.providerKey)))
  }

  override def add(loginInfo: LoginInfo, authInfo: OAuth2Info) = tracingService.noopTrace("oauth2.add") { implicit td =>
    ApplicationDatabase.executeF(OAuth2InfoQueries.CreateOAuth2Info(loginInfo, authInfo)).map(_ => authInfo)
  }

  override def update(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = tracingService.noopTrace("oauth2.update") { implicit td =>
    ApplicationDatabase.executeF(OAuth2InfoQueries.UpdateOAuth2Info(loginInfo, authInfo)).map(_ => authInfo)
  }

  override def save(loginInfo: LoginInfo, authInfo: OAuth2Info) = tracingService.noopTrace("oauth2.save") { implicit td =>
    ApplicationDatabase.executeF(OAuth2InfoQueries.UpdateOAuth2Info(loginInfo, authInfo))(td).flatMap { rowsAffected =>
      if (rowsAffected == 0) {
        ApplicationDatabase.executeF(OAuth2InfoQueries.CreateOAuth2Info(loginInfo, authInfo))(td).map(_ => authInfo)
      } else {
        Future.successful(authInfo)
      }
    }
  }

  override def remove(loginInfo: LoginInfo) = tracingService.topLevelTrace("oauth2.remove") { implicit td =>
    ApplicationDatabase.executeF(OAuth2InfoQueries.removeByPrimaryKey(loginInfo.providerID, loginInfo.providerKey)).map(_ => {})
  }
}
