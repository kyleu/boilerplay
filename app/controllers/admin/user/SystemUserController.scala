package controllers.admin.user

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import controllers.admin.ServiceController
import models.Application
import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.result.RelationCount
import play.api.http.MimeTypes
import services.audit.AuditRecordRowService
import services.note.NoteRowService
import services.user.SystemUserService
import com.kyleu.projectile.util.JsonSerializers._
import util.ReftreeUtils._

@javax.inject.Singleton
class SystemUserController @javax.inject.Inject() (
    override val app: Application, svc: SystemUserService, auditRecordSvc: AuditRecordRowService, val authInfoRepository: AuthInfoRepository, val hasher: PasswordHasher,
    noteS: NoteRowService
) extends ServiceController(svc) with UserEditHelper with UserSearchHelper {
  def view(id: java.util.UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "systemUser", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.user.systemUserView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No SystemUser found with id [$id].")
    })
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val creds = models.auth.UserCredentials.fromRequest(request)
    val noteByAuthorF = noteS.countByAuthor(creds, id)
    for (noteC <- noteByAuthorF) yield {
      Ok(Seq(
        RelationCount(model = "note", field = "author", count = noteC)
      ).asJson)
    }
  }
}
