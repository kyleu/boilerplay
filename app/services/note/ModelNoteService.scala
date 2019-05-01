package services.note

import com.kyleu.projectile.models.note.Note
import com.kyleu.projectile.services.Credentials
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.note.NoteRow
import models.queries.note.{ModelNoteQueries, NoteRowQueries}

import scala.concurrent.ExecutionContext

@javax.inject.Singleton
class ModelNoteService @javax.inject.Inject() (
    val svc: NoteRowService, db: JdbcDatabase, tracing: TracingService
)(implicit ec: ExecutionContext) extends NoteService {
  override def getFor(creds: Credentials, model: String, pk: Any*)(implicit trace: TraceData) = {
    tracing.trace("get.by.model") { td =>
      db.queryF(ModelNoteQueries.GetByModel(model, pk.mkString("/")))(td).map(_.map(fromRow))
    }
  }

  override def getForSeq(creds: Credentials, models: Seq[(String, String)])(implicit trace: TraceData) = {
    tracing.trace("get.by.model.seq") { td =>
      db.queryF(ModelNoteQueries.GetByModelSeq(models.map(x => x._1 -> x._2.mkString("/"))))(td).map(_.map(fromRow))
    }
  }

  override def csvFor(operation: String, totalCount: Int, rows: Seq[Note])(implicit trace: TraceData) = tracing.traceBlocking("note.service.note") { td =>
    CsvUtils.csvFor(Some(s"Notes for [$operation]"), totalCount, rows, NoteRowQueries.fields)(td)
  }

  private[this] def fromRow(nr: NoteRow) = Note(id = nr.id, relType = nr.relType, relPk = nr.relPk, text = nr.text, author = nr.author, created = nr.created)
}
