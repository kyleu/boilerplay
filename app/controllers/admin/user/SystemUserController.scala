package controllers.admin.user

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import controllers.BaseController
import io.circe.syntax._
import models.Application
import models.result.RelationCount
import services.audit.AuditRecordService
import services.note.NoteService
import services.user.SystemUserService
import util.FutureUtils.defaultContext

@javax.inject.Singleton
class SystemUserController @javax.inject.Inject() (
    override val app: Application, svc: SystemUserService, val authInfoRepository: AuthInfoRepository, val hasher: PasswordHasher,
    noteS: NoteService, auditRecordS: AuditRecordService
) extends BaseController("user") with UserEditHelper with UserSearchHelper {
  def view(id: UUID) = withSession("user.view", admin = true) { implicit request => implicit td =>
    val modelF = app.coreServices.users.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "user", id)
    val auditsF = auditRecordS.getByModel(request, "user", id)

    notesF.flatMap(notes => auditsF.flatMap(audits => modelF.map {
      case Some(model) => render {
        case Accepts.Html() => Ok(views.html.admin.user.systemUserView(request.identity, model, notes, audits, app.config.debug))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      }
      case None => NotFound(s"No user found with id [$id].")
    }))
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val creds = models.auth.Credentials.fromRequest(request)
    noteS.countByAuthor(creds, id).map { noteCountByAuthor =>
      Ok(Seq(
        RelationCount(model = "note", field = "author", count = noteCountByAuthor)
      ).asJson.spaces2).as(JSON)
    }
  }
}
