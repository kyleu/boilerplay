package controllers.admin.user

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import controllers.BaseController
import io.circe.syntax._
import models.Application
import models.result.RelationCount
import services.note.NoteService
import services.user.UserService
import util.FutureUtils.defaultContext

import scala.concurrent.Future

@javax.inject.Singleton
class UserController @javax.inject.Inject() (
    override val app: Application, svc: UserService, val authInfoRepository: AuthInfoRepository, val hasher: PasswordHasher, noteS: NoteService
) extends BaseController("user") with UserEditHelper with UserSearchHelper {
  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val creds = models.auth.Credentials.fromRequest(request)
    noteS.countByAuthor(creds, id).map { noteCountByAuthor =>
      Ok(Seq(
        RelationCount(model = "note", field = "author", count = noteCountByAuthor)
      ).asJson.spaces2).as(JSON)
    }
  }
}
