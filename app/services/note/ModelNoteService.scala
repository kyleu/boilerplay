package services.note

import models.note.Note
import models.queries.note.{ModelNoteQueries, NoteQueries}
import services.database.SystemDatabase
import util.tracing.{TraceData, TracingService}

object ModelNoteService {
  private[this] var svc: Option[ModelNoteService] = None

  def init(s: ModelNoteService) = {
    svc.foreach(_ => throw new IllegalStateException("Double init."))
    svc = Some(s)
  }

  def getFor(model: String, pk: Any*)(implicit trace: TraceData) = {
    svc.getOrElse(throw new IllegalStateException("Note service not initialized.")).getFor(model, pk.mkString("/"))
  }
}

@javax.inject.Singleton
class ModelNoteService @javax.inject.Inject() (tracing: TracingService) {
  ModelNoteService.init(this)

  def getFor(model: String, pk: String)(implicit trace: TraceData) = {
    SystemDatabase.query(ModelNoteQueries.GetByModel(model, pk))
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = {
    tracing.traceBlocking("note.service.note") { td =>
      util.CsvUtils.csvFor(Some(s"Notes for [$operation]"), totalCount, rows, NoteQueries.fields)(td)
    }
  }
}
