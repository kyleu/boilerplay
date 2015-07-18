package services.user

import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.daos.AuthenticatorDAO
import models.database.queries.auth.AuthenticatorQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import utils.cache.UserCache

import scala.concurrent.Future

object SessionInfoService extends AuthenticatorDAO[CookieAuthenticator] {
  override def find(id: String) = UserCache.getSession(id) match {
    case Some(sess) => Future.successful(Some(sess))
    case None => Database.query(AuthenticatorQueries.getById(Seq(id))).map {
      case Some(dbSess) =>
        UserCache.cacheSession(dbSess)
        Some(dbSess)
      case None => None
    }
  }

  override def add(session: CookieAuthenticator) = Database.execute(AuthenticatorQueries.insert(session)).map { x =>
    UserCache.cacheSession(session)
  }

  override def update(session: CookieAuthenticator) = Database.execute(AuthenticatorQueries.UpdateAuthenticator(session)).map { x =>
    UserCache.cacheSession(session)
  }

  override def remove(id: String) = Database.execute(AuthenticatorQueries.removeById(Seq(id))).map { x =>
    UserCache.removeSession(id)
  }
}
