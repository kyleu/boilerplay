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
    /* Projectile export section [boilerplay] */

    val intSearches = Seq[Future[Seq[Html]]]()
    /* End int searches */

    Future.sequence(intSearches).map(_.flatten)
  }

  private[this] def searchUuid(creds: Credentials, q: String, id: UUID)(implicit timing: TraceData) = {
    /* Start uuid searches */
    /* Projectile export section [boilerplay] */
    val auditRecordRow = services.auditServices.auditRecordRowService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.audit.auditRecordRowSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    }.toSeq)
    val auditRow = services.auditServices.auditRowService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.audit.auditRowSearchResult(model, s"Audit [${model.id}] matched [$q].")
    }.toSeq)
    val noteRow = services.noteServices.noteRowService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.note.noteRowSearchResult(model, s"Note [${model.id}] matched [$q].")
    }.toSeq)
    val scheduledTaskRunRow = services.taskServices.scheduledTaskRunRowService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.task.scheduledTaskRunRowSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q].")
    }.toSeq)
    val systemUser = services.userServices.systemUserService.getByPrimaryKey(creds, id).map(_.map { model =>
      views.html.admin.user.systemUserSearchResult(model, s"System User [${model.id}] matched [$q].")
    }.toSeq)

    val uuidSearches = Seq[Future[Seq[Html]]](auditRecordRow, auditRow, noteRow, scheduledTaskRunRow, systemUser)
    /* End uuid searches */
    Future.sequence(uuidSearches).map(_.flatten)
  }

  private[this] def searchString(creds: Credentials, q: String)(implicit timing: TraceData) = {
    /* Start string searches */
    /* Projectile export section [boilerplay] */
    val auditRecordRow = services.auditServices.auditRecordRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.audit.auditRecordRowSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    })
    val auditRow = services.auditServices.auditRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.audit.auditRowSearchResult(model, s"Audit [${model.id}] matched [$q].")
    })
    val flywaySchemaHistoryRow = services.ddlServices.flywaySchemaHistoryRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.ddl.flywaySchemaHistoryRowSearchResult(model, s"Flyway Schema History [${model.installedRank}] matched [$q].")
    })
    val noteRow = services.noteServices.noteRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.note.noteRowSearchResult(model, s"Note [${model.id}] matched [$q].")
    })
    val scheduledTaskRunRow = services.taskServices.scheduledTaskRunRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.task.scheduledTaskRunRowSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q].")
    })
    val setting = services.settingsServices.settingService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.settings.settingSearchResult(model, s"Setting [${model.k}] matched [$q].")
    })
    val syncProgressRow = services.syncServices.syncProgressRowService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.sync.syncProgressRowSearchResult(model, s"Sync Progress [${model.key}] matched [$q].")
    })
    val systemUser = services.userServices.systemUserService.searchExact(creds, q = q, limit = Some(5)).map(_.map { model =>
      views.html.admin.user.systemUserSearchResult(model, s"System User [${model.id}] matched [$q].")
    })

    val stringSearches = Seq[Future[Seq[Html]]](auditRecordRow, auditRow, flywaySchemaHistoryRow, noteRow, scheduledTaskRunRow, setting, syncProgressRow, systemUser)
    /* End string searches */

    Future.sequence(stringSearches).map(_.flatten)
  }
}
