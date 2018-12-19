package controllers.admin.system

import java.util.UUID

import controllers.BaseController
import models.Application
import models.auth.Credentials
import play.twirl.api.Html
import services.ServiceRegistry
import util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class SearchController @javax.inject.Inject() (override val app: Application, services: ServiceRegistry) extends BaseController("search") {
  import app.contexts.webContext

  def search(q: String) = withSession("admin.search", admin = true) { implicit request => implicit td =>
    val creds = Credentials.fromRequest(request)
    val results = try {
      searchInt(creds, q, q.toInt)
    } catch {
      case _: NumberFormatException => try {
        searchUuid(creds, q, UUID.fromString(q))
      } catch {
        case _: IllegalArgumentException => searchString(creds, q)
      }
    }
    results.map { r =>
      Ok(views.html.admin.explore.searchResults(q, r, request.identity))
    }
  }

  private[this] def searchInt(creds: Credentials, q: String, id: Int)(implicit timing: TraceData) = {
    /* Start int searches */

    val intSearches = Seq[Future[Seq[Html]]]()
    /* End int searches */

    Future.sequence(intSearches).map(_.flatten)
  }

  private[this] def searchUuid(creds: Credentials, q: String, id: UUID)(implicit timing: TraceData) = {
    /* Start uuid searches */
    val auditRecord = services.auditServices.auditRecordService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.audit.auditRecordSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    }.toSeq)
    val note = services.noteServices.noteService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.note.noteSearchResult(model, s"Note [${model.id}] matched [$q].")
    }.toSeq)
    val scheduledTaskRun = services.taskServices.scheduledTaskRunService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.task.scheduledTaskRunSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q].")
    }.toSeq)
    val systemUser = services.userServices.systemUserService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.user.systemUserSearchResult(model, s"System User [${model.id}] matched [$q].")
    }.toSeq)

    val uuidSearches = Seq[Future[Seq[Html]]](auditRecord, note, scheduledTaskRun, systemUser)
    /* End uuid searches */

    val auditR = app.coreServices.audits.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.audit.auditSearchResult(model, s"Audit [${model.id}] matched [$q].")
    }.toSeq)

    Future.sequence(auditR +: uuidSearches).map(_.flatten)
  }

  private[this] def searchString(creds: Credentials, q: String)(implicit timing: TraceData) = {
    /* Start string searches */
    val auditRecord = services.auditServices.auditRecordService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.audit.auditRecordSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    })
    val flywaySchemaHistory = services.ddlServices.flywaySchemaHistoryService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.ddl.flywaySchemaHistorySearchResult(model, s"Flyway Schema History [${model.installedRank}] matched [$q].")
    })
    val note = services.noteServices.noteService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.note.noteSearchResult(model, s"Note [${model.id}] matched [$q].")
    })
    val scheduledTaskRun = services.taskServices.scheduledTaskRunService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.task.scheduledTaskRunSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q].")
    })
    val setting = services.settingsServices.settingService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.settings.settingSearchResult(model, s"Setting [${model.k}] matched [$q].")
    })
    val syncProgress = services.syncServices.syncProgressService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.sync.syncProgressSearchResult(model, s"Sync Progress [${model.key}] matched [$q].")
    })
    val systemUser = services.userServices.systemUserService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.user.systemUserSearchResult(model, s"System User [${model.id}] matched [$q].")
    })

    val stringSearches = Seq[Future[Seq[Html]]](auditRecord, flywaySchemaHistory, note, scheduledTaskRun, setting, syncProgress, systemUser)
    /* End string searches */

    val auditR = app.coreServices.audits.searchExact(creds = creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.audit.auditSearchResult(model, s"Audit [${model.id}] matched [$q].")
    })

    Future.sequence(auditR +: stringSearches).map(_.flatten)
  }
}
