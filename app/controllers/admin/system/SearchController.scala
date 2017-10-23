package controllers.admin.system

import java.util.UUID

import controllers.BaseController
import models.Application
import play.twirl.api.Html
import services.ServiceRegistry
import util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class SearchController @javax.inject.Inject() (override val app: Application, services: ServiceRegistry) extends BaseController("search") {
  import app.contexts.webContext

  def search(q: String) = withSession("admin.search", admin = true) { implicit request => implicit td =>
    val results = try {
      searchInt(q, q.toInt)
    } catch {
      case _: NumberFormatException => try {
        searchUuid(q, UUID.fromString(q))
      } catch {
        case _: IllegalArgumentException => searchString(q)
      }
    }
    Future.successful(Ok(views.html.admin.explore.searchResults(q, results, request.identity)))
  }

  private[this] def searchInt(q: String, id: Int)(implicit timing: TraceData) = {
    // Start int searches
    val intSearches = Seq.empty[Seq[Html]]
    // End int searches

    intSearches.flatten
  }

  private[this] def searchUuid(q: String, id: UUID)(implicit timing: TraceData) = {
    // Start uuid searches

    val auditRecord = services.auditServices.auditRecordService.getByPrimaryKey(id).map { model =>
      views.html.admin.audit.auditRecordSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    }.toSeq
    val note = services.noteServices.noteService.getByPrimaryKey(id).map { model =>
      views.html.admin.note.noteSearchResult(model, s"Note [${model.id}] matched [$q].")
    }.toSeq
    val user = services.userServices.userService.getByPrimaryKey(id).map { model =>
      views.html.admin.user.userSearchResult(model, s"User [${model.id}] matched [$q].")
    }.toSeq

    val uuidSearches = Seq[Seq[Html]](auditRecord, note, user)

    // End uuid searches

    val auditR = app.auditService.getByPrimaryKey(id).map { model =>
      views.html.admin.audit.auditSearchResult(model, s"Audit [${model.id}] matched [$q].")
    }.toSeq

    (auditR +: uuidSearches).flatten
  }

  private[this] def searchString(q: String)(implicit timing: TraceData) = {
    // Start string searches

    val auditRecord = services.auditServices.auditRecordService.searchExact(q = q, limit = Some(5)).map { model =>
      views.html.admin.audit.auditRecordSearchResult(model, s"Audit Record [${model.id}] matched [$q].")
    }
    val note = services.noteServices.noteService.searchExact(q = q, limit = Some(5)).map { model =>
      views.html.admin.note.noteSearchResult(model, s"Note [${model.id}] matched [$q].")
    }
    val user = services.userServices.userService.searchExact(q = q, limit = Some(5)).map { model =>
      views.html.admin.user.userSearchResult(model, s"User [${model.id}] matched [$q].")
    }

    val stringSearches = Seq[Seq[Html]](auditRecord, note, user)

    // End string searches

    val auditR = app.auditService.searchExact(q = q, limit = Some(5)).map { model =>
      views.html.admin.audit.auditSearchResult(model, s"Audit [${model.id}] matched [$q].")
    }
    val noteR = app.noteService.svc.searchExact(q = q, orderBys = Nil, limit = Some(10), offset = None).map { n =>
      views.html.admin.note.noteSearchResult(n, s"Note [${n.id}] matched [$q].")
    }
    val userR = app.userService.searchExact(q = q, orderBys = Nil, limit = Some(10), offset = None).map { u =>
      views.html.admin.user.userSearchResult(u, s"User [${u.username}] matched [$q].")
    }

    (auditR +: noteR +: userR +: stringSearches).flatten
  }
}
