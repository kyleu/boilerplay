package services.note

import models.auth.Credentials
import models.note.Note
import models.queries.note.{ModelNoteQueries, NoteQueries}
import services.database.ApplicationDatabase
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class ModelNoteService @javax.inject.Inject() (val svc: NoteService, tracing: TracingService) {
  def getFor(creds: Credentials, model: String, pk: Any*)(implicit trace: TraceData) = tracing.trace("get.by.model") { td =>
    ApplicationDatabase.queryF(ModelNoteQueries.GetByModel(model, pk.mkString("/")))(td)
  }

  def getForSeq(creds: Credentials, models: Seq[(String, String)])(implicit trace: TraceData) = tracing.trace("get.by.model.seq") { td =>
    ApplicationDatabase.queryF(ModelNoteQueries.GetByModelSeq(models.map(x => x._1 -> x._2.mkString("/"))))(td)
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = tracing.traceBlocking("note.service.note") { td =>
    util.CsvUtils.csvFor(Some(s"Notes for [$operation]"), totalCount, rows, NoteQueries.fields)(td)
  }
}
