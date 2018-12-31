package services.note

import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.models.auth.UserCredentials
import models.note.NoteRow
import models.queries.note.{ModelNoteQueries, NoteRowQueries}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class ModelNoteService @javax.inject.Inject() (val svc: NoteRowService, tracing: TracingService) {
  def getFor(creds: UserCredentials, model: String, pk: Any*)(implicit trace: TraceData) = tracing.trace("get.by.model") { td =>
    ApplicationDatabase.queryF(ModelNoteQueries.GetByModel(model, pk.mkString("/")))(td)
  }

  def getForSeq(creds: UserCredentials, models: Seq[(String, String)])(implicit trace: TraceData) = tracing.trace("get.by.model.seq") { td =>
    ApplicationDatabase.queryF(ModelNoteQueries.GetByModelSeq(models.map(x => x._1 -> x._2.mkString("/"))))(td)
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[NoteRow])(implicit trace: TraceData) = tracing.traceBlocking("note.service.note") { td =>
    CsvUtils.csvFor(Some(s"Notes for [$operation]"), totalCount, rows, NoteRowQueries.fields)(td)
  }
}
