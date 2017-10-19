package services.note

import models.note.Note
import models.queries.note.{ModelNoteQueries, NoteQueries}
import services.database.SystemDatabase
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class ModelNoteService @javax.inject.Inject() (val svc: NoteService, tracing: TracingService) {
  def getFor(model: String, pk: Any*)(implicit trace: TraceData) = {
    SystemDatabase.query(ModelNoteQueries.GetByModel(model, pk.mkString("/")))
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = {
    tracing.traceBlocking("note.service.note") { td =>
      util.CsvUtils.csvFor(Some(s"Notes for [$operation]"), totalCount, rows, NoteQueries.fields)(td)
    }
  }
}
