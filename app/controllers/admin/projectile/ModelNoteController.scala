package controllers.admin.projectile

import controllers.BaseController
import controllers.admin.ServiceController
import models.Application
import scala.concurrent.ExecutionContext.Implicits.global
import models.note.NoteRow
import services.note.ModelNoteService
import com.kyleu.projectile.util.JsonSerializers._

import scala.concurrent.Future

@javax.inject.Singleton
class ModelNoteController @javax.inject.Inject() (override val app: Application, svc: ModelNoteService) extends BaseController("note") {
  def view(model: String, pk: String) = withSession("list", admin = true) { implicit request => implicit td =>
    svc.getFor(request, model, pk).map(notes => render {
      case Accepts.Html() => Ok(views.html.admin.note.modelNoteList(request.identity, notes, model, pk))
      case Accepts.Json() => Ok(notes.asJson)
      case ServiceController.acceptsCsv() => Ok(svc.csvFor(model + " " + pk.mkString("/"), 0, notes)).as("text/csv")
    })
  }

  def addForm(model: String, pk: String) = withSession("add.form", admin = true) { implicit request => implicit td =>
    val note = NoteRow.empty(relType = Some(model), relPk = Some(pk), author = request.identity.id)
    val cancel = controllers.admin.projectile.routes.ModelNoteController.view(model, pk)
    val call = controllers.admin.note.routes.NoteRowController.create()
    Future.successful(Ok(views.html.admin.note.noteRowForm(
      request.identity, note, s"Note for $model:$pk", cancel, call, isNew = true, debug = app.config.debug
    )))
  }
}
