package controllers.admin.user

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import controllers.BaseController
import models.Application
import models.result.RelationCount
import services.note.NoteService
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import services.audit.AuditService

import scala.concurrent.Future

@javax.inject.Singleton
class UserController @javax.inject.Inject() (
    override val app: Application, auditS: AuditService, noteS: NoteService, val authInfoRepository: AuthInfoRepository, val hasher: PasswordHasher
) extends BaseController("user.create") with UserEditHelper with UserSearchHelper {
  import app.contexts.webContext

  def relationCounts(id: UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val notesByAuthor = noteS.countByAuthor(id)
    val auditsByUserId = auditS.countByUserId(id)
    Future.successful(Ok(Seq(
      RelationCount(model = "note", field = "author", count = notesByAuthor),
      RelationCount(model = "audit", field = "user", count = auditsByUserId)
    ).asJson.spaces2).as(JSON))
  }
}
