/* Generated File */
package services.note

import java.util.UUID

import models.auth.Credentials
import models.note.Note
import models.queries.note.NoteQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class NoteService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[Note]("note") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(NoteQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(NoteQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(NoteQueries.countAll(filters))(td))
  }
  override def getAll(
    creds: Credentials, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(NoteQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(NoteQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(NoteQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(NoteQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuthor(creds: Credentials, author: UUID)(implicit trace: TraceData) = traceF("count.by.author") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByAuthor(author))(td)
  }
  def getByAuthor(creds: Credentials, author: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceF("get.by.author")(td => ApplicationDatabase.queryF(NoteQueries.GetByAuthor(author, orderBys, limit, offset))(td))
  }
  def getByAuthorSeq(creds: Credentials, authorSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.author.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByAuthorSeq(authorSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: Note)(implicit trace: TraceData) = {
    traceF("insert")(td => ApplicationDatabase.executeF(NoteQueries.insert(model))(td).map {
      case 1 => getByPrimaryKey(creds, model.id)(td).map {
        case Some(n) =>
          services.audit.AuditHelper.onInsert("Note", Seq(n.id.toString), n.toDataFields, creds)
          n
        case _ => throw new IllegalStateException("Unable to retrieve newly-inserted Note.")
      }
      case _ => throw new IllegalStateException("Unable to find newly-inserted Note.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[Note])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(NoteQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(NoteQueries.create(fields))(td).flatMap { _ =>
      services.audit.AuditHelper.onInsert("Note", Seq(fieldVal(fields, "id")), fields, creds)
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        services.audit.AuditHelper.onRemove("Note", Seq(id.toString), current.toDataFields, creds)
        ApplicationDatabase.executeF(NoteQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Note [$id].")
      case Some(current) => ApplicationDatabase.executeF(NoteQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            services.audit.AuditHelper.onUpdate("Note", Seq(DataField("id", Some(id.toString))), current.toDataFields, fields, creds)
            newModel -> s"Updated [${fields.size}] fields of Note [$id]."
          case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, NoteQueries.fields)(td))
  }
}
