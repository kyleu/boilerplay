package services.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.auth.Credentials
import models.queries.auth._
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.{Role, SystemUser}
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import services.cache.UserCache
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class SystemUserService @javax.inject.Inject() (
    override val tracing: TracingService, hasher: PasswordHasher
) extends ModelServiceHelper[SystemUser]("systemUser") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("get.by.primary.key") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.getByPrimaryKey(id))(td)
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.primary.key.sequence") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.getByPrimaryKeySeq(idSeq))(td)
  }

  def getByRoleSeq(roleSeq: Seq[Role])(implicit trace: TraceData) = traceF("get.by.role.sequence") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.GetByRoleSeq(roleSeq))(td)
  }

  def getByUsername(username: String)(implicit trace: TraceData) = traceF("get.by.username") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.FindUserByUsername(username))(td)
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = traceF("count.all") { td =>
    ApplicationDatabase.queryF(SystemUserQueries.countAll(filters))(td)
  }
  override def getAll(
    creds: Credentials, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(SystemUserQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter])(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(SystemUserQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(SystemUserQueries.search(q, filters, orderBys, limit, offset))(td))
  }
  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(SystemUserQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def isUsernameInUse(name: String)(implicit trace: TraceData) = traceF("username.in.use") { td =>
    ApplicationDatabase.queryF(UserSearchQueries.IsUsernameInUse(name))(td)
  }

  def insert(creds: Credentials, model: SystemUser)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(SystemUserQueries.insert(model))(td).map { _ =>
      log.info(s"Inserted user [$model].")
      UserCache.cacheUser(model)
      model
    }
  }

  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(SystemUserQueries.create(fields))(td).flatMap { _ =>
      services.audit.AuditHelper.onInsert("SystemUser", Seq(fieldVal(fields, "id")), fields, creds)
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Identity [$id].")
      case Some(current) => ApplicationDatabase.executeF(SystemUserQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            services.audit.AuditHelper.onUpdate("SystemUser", Seq(DataField("id", Some(id.toString))), current.toDataFields, fields, creds)
            newModel -> s"Updated [${fields.size}] fields of Identity [$id]."
          case None => throw new IllegalStateException(s"Cannot find Identity matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find Identity matching [$id].")
    })
  }

  def updateUser(creds: Credentials, model: SystemUser)(implicit trace: TraceData) = traceF("update") { td =>
    ApplicationDatabase.executeF(SystemUserQueries.UpdateUser(model))(td).map { rowsAffected =>
      if (rowsAffected != 1) { throw new IllegalStateException(s"Attempt to update user [${model.id}] affected [$rowsAffected}] rows.") }
      log.info(s"Updated user [$model].")
      UserCache.cacheUser(model)
      model
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("remove")(td => ApplicationDatabase.transaction { (txTd, conn) =>
    getByPrimaryKey(creds, id)(txTd).flatMap {
      case Some(model) =>
        UserCache.getUser(id).foreach { user =>
          services.audit.AuditHelper.onRemove("SystemUser", Seq(id.toString), user.toDataFields, creds)
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

  def updateFields(
    creds: Credentials, id: UUID, username: String, provider: String, key: String, password: Option[String], role: Role, originalEmail: String
  )(implicit trace: TraceData) = {
    traceF("update.fields") { _ =>
      val fields = Seq(
        DataField("username", Some(username)),
        DataField("provider", Some(provider)),
        DataField("key", Some(key)),
        DataField("role", Some(role.toString))
      )
      ApplicationDatabase.executeF(SystemUserQueries.update(id, fields)).flatMap { _ =>
        if (key != originalEmail) {
          ApplicationDatabase.executeF(PasswordInfoQueries.UpdateEmail(originalEmail, key))
        } else {
          Future.successful(0)
        }
      }.flatMap { _ =>
        password match {
          case Some(p) =>
            val loginInfo = LoginInfo(CredentialsProvider.ID, key)
            val authInfo = hasher.hash(p)
            ApplicationDatabase.executeF(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo)).map { _ =>
              UserCache.removeUser(id)
              services.audit.AuditHelper.onInsert("SystemUser", Seq(id.toString), fields, creds)
              id
            }
          case _ => Future.successful(id)
        }
      }
    }
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[SystemUser])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, SystemUserQueries.fields)(td))
  }
}
