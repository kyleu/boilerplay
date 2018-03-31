package controllers.admin.note

import controllers.BaseController
import controllers.admin.ServiceController
import models.Application
import models.note.Note
import services.note.ModelNoteService
import util.FutureUtils.defaultContext
import util.JsonSerializers._

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
    val note = Note(relType = Some(model), relPk = Some(pk), text = "", author = request.identity.id)
    val cancel = controllers.admin.note.routes.ModelNoteController.view(model, pk)
    val call = controllers.admin.note.routes.NoteController.create()
    Future.successful(Ok(views.html.admin.note.noteForm(
      request.identity, note, s"Note for $model:$pk", cancel, call, isNew = true, debug = app.config.debug
    )))
  }
}
