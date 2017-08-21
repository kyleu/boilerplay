package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.queries.auth._
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.{Role, User}
import services.ModelServiceHelper
import util.FutureUtils.databaseContext
import services.database.MasterDatabase
import services.cache.UserCache
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class UserService @javax.inject.Inject() (override val tracing: TracingService, hasher: PasswordHasher) extends ModelServiceHelper[User]("user") {
  def getByPrimaryKey(id: UUID)(implicit trace: TraceData) = traceF("get.by.primary.key") { td =>
    MasterDatabase.query(UserQueries.getByPrimaryKey(Seq(id)))(td)
  }
  def getByPrimaryKeySeq(idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.primary.key.sequence") { td =>
    MasterDatabase.query(UserQueries.getByPrimaryKeySeq(idSeq))(td)
  }

  def getByRoleSeq(roleSeq: Seq[Role])(implicit trace: TraceData) = traceF("get.by.role.sequence") { td =>
    MasterDatabase.query(UserQueries.getByRoleSeq(roleSeq))(td)
  }

  override def countAll(filters: Seq[Filter] = Nil)(implicit trace: TraceData) = traceF("count.all") { td =>
    MasterDatabase.query(UserQueries.countAll(filters))(td)
  }
  override def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => MasterDatabase.query(UserQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  override def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceF("search.count")(td => MasterDatabase.query(UserQueries.searchCount(q, filters))(td))
  }
  override def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceF("search")(td => MasterDatabase.query(UserQueries.search(q, filters, orderBys, limit, offset))(td))
  }
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceF("search.exact")(td => MasterDatabase.query(UserQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def isUsernameInUse(name: String)(implicit trace: TraceData) = traceF("username.in.use") { td =>
    MasterDatabase.query(UserSearchQueries.IsUsernameInUse(name))(td)
  }

  def insert(user: User)(implicit trace: TraceData) = traceF("insert")(td => MasterDatabase.execute(UserQueries.insert(user))(td).map { _ =>
    log.info(s"Inserted user [$user].")
    UserCache.cacheUser(user)
    user
  })

  def update(user: User)(implicit trace: TraceData) = traceF("update")(td => MasterDatabase.execute(UserQueries.UpdateUser(user))(td).map { _ =>
    log.info(s"Updated user [$user].")
    UserCache.cacheUser(user)
    user
  })

  def remove(userId: UUID)(implicit trace: TraceData) = traceF("remove")(td => MasterDatabase.transaction { (txTd, conn) =>
    val startTime = System.nanoTime
    val f = getByPrimaryKey(userId)(txTd).flatMap {
      case Some(user) => MasterDatabase.execute(PasswordInfoQueries.removeByPrimaryKey(Seq(user.profile.providerID, user.profile.providerKey)), Some(conn))
      case None => throw new IllegalStateException("Invalid User")
    }
    f.flatMap { _ =>
      MasterDatabase.execute(UserQueries.removeByPrimaryKey(Seq(userId)), Some(conn))(txTd).map { users =>
        UserCache.removeUser(userId)
        val timing = ((System.nanoTime - startTime) / 1000000).toInt
        Map("users" -> users, "timing" -> timing)
      }
    }
  }(td))

  def updateFields(id: UUID, username: String, email: String, password: Option[String], role: Role, originalEmail: String)(implicit trace: TraceData) = {
    traceF("update.fields") { _ =>
      val fields = Seq(
        DataField("username", Some(username)),
        DataField("email", Some(email)),
        DataField("role", Some(role.toString))
      )
      MasterDatabase.execute(UserQueries.update(id, fields)).flatMap { _ =>
        val emailUpdated = if (email != originalEmail) {
          MasterDatabase.execute(PasswordInfoQueries.UpdateEmail(originalEmail, email))
        } else {
          Future.successful(0)
        }
        emailUpdated.flatMap { _ =>
          password match {
            case Some(p) =>
              val loginInfo = LoginInfo(CredentialsProvider.ID, email)
              val authInfo = hasher.hash(p)
              MasterDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo))
            case _ => Future.successful(id)
          }
        }.map { _ =>
          UserCache.removeUser(id)
          id
        }
      }
    }
  }
}
