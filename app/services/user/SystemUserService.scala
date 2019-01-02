package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.util.PasswordHasher

import scala.concurrent.ExecutionContext.Implicits.global
import models.queries.auth._
import com.kyleu.projectile.models.user.SystemUser
import com.kyleu.projectile.services.Credentials
import services.cache.UserCache
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import com.kyleu.projectile.models.auth.UserCredentials

@javax.inject.Singleton
class SystemUserService @javax.inject.Inject() (val tracing: TracingService, hasher: PasswordHasher) {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = tracing.trace("get.by.primary.key") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.getByPrimaryKey(id))(td)
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = tracing.trace("get.by.primary.key.sequence") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.getByPrimaryKeySeq(idSeq))(td)
  }

  def getByUsername(creds: Credentials, username: String)(implicit trace: TraceData) = tracing.trace("get.by.username") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.FindUserByUsername(username))(td)
  }
  def isUsernameInUse(creds: Credentials, name: String)(implicit trace: TraceData) = tracing.trace("username.in.use") { td =>
    ApplicationDatabase.queryF(UserSearchQueries.IsUsernameInUse(name))(td)
  }

  def insert(creds: Credentials, model: SystemUser)(implicit trace: TraceData) = tracing.trace("insert") { td =>
    ApplicationDatabase.executeF(SystemUserQueries.insert(model))(td).map { _ =>
      UserCache.cacheUser(model)
      model
    }
  }

  def updateUser(creds: Credentials, model: SystemUser)(implicit trace: TraceData) = tracing.trace("update") { td =>
    ApplicationDatabase.executeF(SystemUserQueries.UpdateUser(model))(td).map { rowsAffected =>
      if (rowsAffected != 1) { throw new IllegalStateException(s"Attempt to update user [${model.id}] affected [$rowsAffected}] rows.") }
      UserCache.cacheUser(model)
      model
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = tracing.trace("remove")(td => ApplicationDatabase.transaction { (txTd, conn) =>
    getByPrimaryKey(creds, id)(txTd).flatMap {
      case Some(model) =>
        UserCache.getUser(id).foreach { user =>
          services.audit.AuditHelper.onRemove("SystemUser", Seq(id.toString), user.toDataFields, creds.asInstanceOf[UserCredentials])
        }
        ApplicationDatabase.executeF(SystemUserQueries.removeByPrimaryKey(id))(txTd).flatMap { _ =>
          ApplicationDatabase.executeF(PasswordInfoQueries.removeByPrimaryKey(model.profile.providerID, model.profile.providerKey), Some(conn)).map { _ =>
            UserCache.removeUser(id)
            model
          }
        }
      case None => throw new IllegalStateException("Invalid User")
    }
  }(td))
}
