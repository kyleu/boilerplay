/* Generated File */
package services.film

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.film.ActorRow
import models.queries.film.ActorRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class ActorRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[ActorRow]("actorRow", "film" -> "ActorRow") {
  def getByPrimaryKey(creds: Credentials, actorId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(ActorRowQueries.getByPrimaryKey(actorId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, actorId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, actorId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load actorRow with actorId [$actorId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, actorIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (actorIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(ActorRowQueries.getByPrimaryKeySeq(actorIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(ActorRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(ActorRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(ActorRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(ActorRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(ActorRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByActorId(creds: Credentials, actorId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.actorId")(td => db.queryF(ActorRowQueries.CountByActorId(actorId))(td))
  }
  def getByActorId(creds: Credentials, actorId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.actorId")(td => db.queryF(ActorRowQueries.GetByActorId(actorId, orderBys, limit, offset))(td))
  }
  def getByActorIdSeq(creds: Credentials, actorIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (actorIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.actorId.seq") { td =>
        db.queryF(ActorRowQueries.GetByActorIdSeq(actorIdSeq))(td)
      }
    }
  }

  def countByFirstName(creds: Credentials, firstName: String)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.firstName")(td => db.queryF(ActorRowQueries.CountByFirstName(firstName))(td))
  }
  def getByFirstName(creds: Credentials, firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.firstName")(td => db.queryF(ActorRowQueries.GetByFirstName(firstName, orderBys, limit, offset))(td))
  }
  def getByFirstNameSeq(creds: Credentials, firstNameSeq: Seq[String])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (firstNameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.firstName.seq") { td =>
        db.queryF(ActorRowQueries.GetByFirstNameSeq(firstNameSeq))(td)
      }
    }
  }

  def countByLastName(creds: Credentials, lastName: String)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastName")(td => db.queryF(ActorRowQueries.CountByLastName(lastName))(td))
  }
  def getByLastName(creds: Credentials, lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastName")(td => db.queryF(ActorRowQueries.GetByLastName(lastName, orderBys, limit, offset))(td))
  }
  def getByLastNameSeq(creds: Credentials, lastNameSeq: Seq[String])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastNameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastName.seq") { td =>
        db.queryF(ActorRowQueries.GetByLastNameSeq(lastNameSeq))(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(ActorRowQueries.CountByLastUpdate(lastUpdate))(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(ActorRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(ActorRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: ActorRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(ActorRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.actorId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Actor.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[ActorRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => db.executeF(ActorRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(ActorRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "actorId").toInt)
    })
  }

  def remove(creds: Credentials, actorId: Int)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, actorId)(td).flatMap {
      case Some(current) =>
        db.executeF(ActorRowQueries.removeByPrimaryKey(actorId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find ActorRow matching [$actorId]")
    })
  }

  def update(creds: Credentials, actorId: Int, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, actorId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Actor [$actorId]")
      case Some(_) => db.executeF(ActorRowQueries.update(actorId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "actorId").flatMap(_.v).map(s => s.toInt).getOrElse(actorId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Actor [$actorId]"
          case None => throw new IllegalStateException(s"Cannot find ActorRow matching [$actorId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find ActorRow matching [$actorId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[ActorRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, ActorRowQueries.fields)(td))
  }
}
