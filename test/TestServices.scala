/* Generated File */
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.tracing.TracingService
import scala.concurrent.ExecutionContext

object TestServices {
  private[this] implicit val ec = ExecutionContext.global
  private[this] val trace = TracingService.noop
  private[this] val db = new JdbcDatabase("application", "database.application")

  val scheduledTaskRunRowService = new services.task.ScheduledTaskRunRowService(db, trace)
  val syncProgressRowService = new services.sync.SyncProgressRowService(db, trace)
}
