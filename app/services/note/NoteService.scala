/* Generated File */
package services.note

import java.time.LocalDateTime
import java.util.UUID
import models.auth.Credentials
import models.note.Note
import models.queries.note.NoteQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import scala.concurrent.Future
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class NoteService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[Note]("note") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(NoteQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, id: UUID)(implicit trace: TraceData) = getByPrimaryKey(creds, id).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load note with id [$id]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(NoteQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(NoteQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(NoteQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(NoteQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(NoteQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(NoteQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuthor(creds: Credentials, author: UUID)(implicit trace: TraceData) = traceF("count.by.author") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByAuthor(author))(td)
  }
  def getByAuthor(creds: Credentials, author: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.author") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByAuthor(author, orderBys, limit, offset))(td)
  }
  def getByAuthorSeq(creds: Credentials, authorSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.author.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByAuthorSeq(authorSeq))(td)
  }

  def countByCreated(creds: Credentials, created: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.created") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByCreated(created))(td)
  }
  def getByCreated(creds: Credentials, created: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.created") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByCreated(created, orderBys, limit, offset))(td)
  }
  def getByCreatedSeq(creds: Credentials, createdSeq: Seq[LocalDateTime])(implicit trace: TraceData) = traceF("get.by.created.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByCreatedSeq(createdSeq))(td)
  }

  def countById(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("count.by.id") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountById(id))(td)
  }
  def getById(creds: Credentials, id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.id") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetById(id, orderBys, limit, offset))(td)
  }
  def getByIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.id.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByIdSeq(idSeq))(td)
  }

  def countByRelPk(creds: Credentials, relPk: String)(implicit trace: TraceData) = traceF("count.by.relPk") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByRelPk(relPk))(td)
  }
  def getByRelPk(creds: Credentials, relPk: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.relPk") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByRelPk(relPk, orderBys, limit, offset))(td)
  }
  def getByRelPkSeq(creds: Credentials, relPkSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.relPk.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByRelPkSeq(relPkSeq))(td)
  }

  def countByRelType(creds: Credentials, relType: String)(implicit trace: TraceData) = traceF("count.by.relType") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByRelType(relType))(td)
  }
  def getByRelType(creds: Credentials, relType: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.relType") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByRelType(relType, orderBys, limit, offset))(td)
  }
  def getByRelTypeSeq(creds: Credentials, relTypeSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.relType.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByRelTypeSeq(relTypeSeq))(td)
  }

  def countByText(creds: Credentials, text: String)(implicit trace: TraceData) = traceF("count.by.text") { td =>
    ApplicationDatabase.queryF(NoteQueries.CountByText(text))(td)
  }
  def getByText(creds: Credentials, text: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.text") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByText(text, orderBys, limit, offset))(td)
  }
  def getByTextSeq(creds: Credentials, textSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.text.seq") { td =>
    ApplicationDatabase.queryF(NoteQueries.GetByTextSeq(textSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: Note)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(NoteQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Note.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[Note])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(NoteQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(NoteQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(NoteQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Note [$id].")
      case Some(_) => ApplicationDatabase.executeF(NoteQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Note [$id]."
          case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, NoteQueries.fields)(td))
  }
}
