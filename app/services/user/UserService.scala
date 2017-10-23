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
import services.database.SystemDatabase
import services.cache.UserCache
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class UserService @javax.inject.Inject() (override val tracing: TracingService, hasher: PasswordHasher) extends ModelServiceHelper[User]("user") {
  def getByPrimaryKey(user: User, id: UUID)(implicit trace: TraceData) = traceB("get.by.primary.key") { td =>
    SystemDatabase.query(UserQueries.getByPrimaryKey(id))(td)
  }
  def getByPrimaryKeySeq(user: User, idSeq: Seq[UUID])(implicit trace: TraceData) = traceB("get.by.primary.key.sequence") { td =>
    SystemDatabase.query(UserQueries.getByPrimaryKeySeq(idSeq))(td)
  }

  def getByRoleSeq(roleSeq: Seq[Role])(implicit trace: TraceData) = traceB("get.by.role.sequence") { td =>
    SystemDatabase.query(UserQueries.getByRoleSeq(roleSeq))(td)
  }

  override def countAll(user: User, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = traceB("count.all") { td =>
    SystemDatabase.query(UserQueries.countAll(filters))(td)
  }
  override def getAll(
    user: User, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceB("get.all")(td => SystemDatabase.query(UserQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  override def searchCount(user: User, q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceB("search.count")(td => SystemDatabase.query(UserQueries.searchCount(q, filters))(td))
  }
  override def search(
    user: User, q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]
  )(implicit trace: TraceData) = {
    traceB("search")(td => SystemDatabase.query(UserQueries.search(q, filters, orderBys, limit, offset))(td))
  }
  def searchExact(user: User, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search.exact")(td => SystemDatabase.query(UserQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def isUsernameInUse(name: String)(implicit trace: TraceData) = traceB("username.in.use") { td =>
    SystemDatabase.query(UserSearchQueries.IsUsernameInUse(name))(td)
  }

  def insert(user: User, model: User)(implicit trace: TraceData) = traceB("insert") { td =>
    SystemDatabase.execute(UserQueries.insert(model))(td)
    log.info(s"Inserted user [$model].")
    UserCache.cacheUser(model)
    model
  }

  def create(user: User, fields: Seq[DataField])(implicit trace: TraceData) = traceB("create") { td =>
    SystemDatabase.execute(UserQueries.create(fields))(td)
    services.audit.AuditHelper.onInsert("Identity", Seq(fieldVal(fields, "id")), fields)
    getByPrimaryKey(user, UUID.fromString(fieldVal(fields, "id")))
  }

  def update(user: User, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceB("update")(td => SystemDatabase.query(UserQueries.getByPrimaryKey(id))(td) match {
      case Some(current) if fields.isEmpty => current -> s"No changes required for Identity [$id]."
      case Some(current) =>
        SystemDatabase.execute(UserQueries.update(id, fields))(td)
        getByPrimaryKey(user, id)(td) match {
          case Some(newModel) =>
            services.audit.AuditHelper.onUpdate("Identity", Seq(DataField("id", Some(id.toString))), current.toDataFields, fields)
            newModel -> s"Updated [${fields.size}] fields of Identity [$id]."
          case None => throw new IllegalStateException(s"Cannot find Identity matching [$id].")
        }
      case None => throw new IllegalStateException(s"Cannot find Identity matching [$id].")
    })
  }

  def updateUser(user: User, model: User)(implicit trace: TraceData) = traceB("update") { td =>
    SystemDatabase.execute(UserQueries.UpdateUser(model))(td)
    log.info(s"Updated user [$model].")
    UserCache.cacheUser(model)
    model
  }

  def remove(user: User, id: UUID)(implicit trace: TraceData) = traceB("remove")(td => SystemDatabase.transaction { (txTd, conn) =>
    getByPrimaryKey(user, id)(txTd) match {
      case Some(model) =>
        UserCache.getUser(id).foreach { user =>
          services.audit.AuditHelper.onRemove("User", Seq(id.toString), user.toDataFields)
        }
        SystemDatabase.execute(UserQueries.removeByPrimaryKey(id), Some(conn))(txTd)
        UserCache.removeUser(id)
        SystemDatabase.execute(PasswordInfoQueries.removeByPrimaryKey(Seq(model.profile.providerID, model.profile.providerKey)), Some(conn))
        model
      case None => throw new IllegalStateException("Invalid User")
    }
  }(td))

  def updateFields(id: UUID, username: String, email: String, password: Option[String], role: Role, originalEmail: String)(implicit trace: TraceData) = {
    traceB("update.fields") { _ =>
      val fields = Seq(
        DataField("username", Some(username)),
        DataField("email", Some(email)),
        DataField("role", Some(role.toString))
      )
      SystemDatabase.execute(UserQueries.update(id, fields))
      if (email != originalEmail) {
        SystemDatabase.execute(PasswordInfoQueries.UpdateEmail(originalEmail, email))
      }
      password match {
        case Some(p) =>
          val loginInfo = LoginInfo(CredentialsProvider.ID, email)
          val authInfo = hasher.hash(p)
          SystemDatabase.execute(PasswordInfoQueries.UpdatePasswordInfo(loginInfo, authInfo))
        case _ => Future.successful(id)
      }
      UserCache.removeUser(id)
      id
    }
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[User])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, UserQueries.fields)(td))
  }
}
