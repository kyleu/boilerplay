/* Generated File */
package services.note

import java.util.UUID

import models.note.Note
import models.queries.note.NoteQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.user.User
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class NoteService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[Note]("note") {
  def getByPrimaryKey(user: User, id: UUID)(implicit trace: TraceData) = {
    traceB("get.by.primary.key")(td => ApplicationDatabase.query(NoteQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(user: User, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceB("get.by.primary.key.seq")(td => ApplicationDatabase.query(NoteQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(user: User, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceB("get.all.count")(td => ApplicationDatabase.query(NoteQueries.countAll(filters))(td))
  }
  override def getAll(user: User, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("get.all")(td => ApplicationDatabase.query(NoteQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(user: User, q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceB("search.count")(td => ApplicationDatabase.query(NoteQueries.searchCount(q, filters))(td))
  }
  override def search(
    user: User, q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceB("search")(td => ApplicationDatabase.query(NoteQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(user: User, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search.exact")(td => ApplicationDatabase.query(NoteQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuthor(user: User, author: UUID)(implicit trace: TraceData) = traceB("count.by.author") { td =>
    ApplicationDatabase.query(NoteQueries.CountByAuthor(author))(td)
  }
  def getByAuthor(user: User, author: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceB("get.by.author")(td => ApplicationDatabase.query(NoteQueries.GetByAuthor(author, orderBys, limit, offset))(td))
  }
  def getByAuthorSeq(user: User, authorSeq: Seq[UUID])(implicit trace: TraceData) = traceB("get.by.author.seq") { td =>
    ApplicationDatabase.query(NoteQueries.GetByAuthorSeq(authorSeq))(td)
  }

  // Mutations
  def insert(user: User, model: Note)(implicit trace: TraceData) = {
    traceB("insert")(td => ApplicationDatabase.execute(NoteQueries.insert(model))(td) match {
      case 1 => getByPrimaryKey(user, model.id)(td).map { model =>
        services.audit.AuditHelper.onInsert("Note", Seq(model.id.toString), model.toDataFields)
        model
      }
      case _ => throw new IllegalStateException("Unable to find newly-inserted Note.")
    })
  }
  def insertBatch(user: User, models: Seq[Note])(implicit trace: TraceData) = {
    traceB("insertBatch")(td => ApplicationDatabase.execute(NoteQueries.insertBatch(models))(td))
  }
  def create(user: User, fields: Seq[DataField])(implicit trace: TraceData) = traceB("create") { td =>
    ApplicationDatabase.execute(NoteQueries.create(fields))(td)
    services.audit.AuditHelper.onInsert("Note", Seq(fieldVal(fields, "id")), fields)
    getByPrimaryKey(user, UUID.fromString(fieldVal(fields, "id")))
  }

  def remove(user: User, id: UUID)(implicit trace: TraceData) = {
    traceB("remove")(td => getByPrimaryKey(user, id)(td) match {
      case Some(current) =>
        services.audit.AuditHelper.onRemove("Note", Seq(id.toString), current.toDataFields)
        ApplicationDatabase.execute(NoteQueries.removeByPrimaryKey(id))(td)
        current
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def update(user: User, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceB("update")(td => getByPrimaryKey(user, id)(td) match {
      case Some(current) if fields.isEmpty => current -> s"No changes required for Note [$id]."
      case Some(current) =>
        ApplicationDatabase.execute(NoteQueries.update(id, fields))(td)
        getByPrimaryKey(user, id)(td) match {
          case Some(newModel) =>
            services.audit.AuditHelper.onUpdate("Note", Seq(DataField("id", Some(id.toString))), current.toDataFields, fields)
            newModel -> s"Updated [${fields.size}] fields of Note [$id]."
          case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
        }
      case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
    })
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, NoteQueries.fields)(td))
  }
}
