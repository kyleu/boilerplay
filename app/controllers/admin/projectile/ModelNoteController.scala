package controllers.admin.projectile

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.controllers.ServiceController

import scala.concurrent.ExecutionContext.Implicits.global
import models.note.NoteRow
import services.note.ModelNoteService
import com.kyleu.projectile.util.JsonSerializers._

import scala.concurrent.Future

@javax.inject.Singleton
class ModelNoteController @javax.inject.Inject() (override val app: Application, svc: ModelNoteService) extends AuthController("note") {
  def view(model: String, pk: String) = withSession("list", admin = true) { implicit request => implicit td =>
    svc.getFor(request, model, pk).map(notes => render {
      case Accepts.Html() => Ok(views.html.admin.note.modelNoteList(app.cfg(Some(request.identity), admin = true), notes, model, pk))
      case Accepts.Json() => Ok(notes.asJson)
      case ServiceController.acceptsCsv() => Ok(svc.csvFor(model + " " + pk.mkString("/"), 0, notes)).as("text/csv")
    })
  }

  def addForm(model: String, pk: String) = withSession("add.form", admin = true) { implicit request => implicit td =>
    val note = NoteRow.empty(relType = Some(model), relPk = Some(pk), author = request.identity.id)
    val cancel = controllers.admin.projectile.routes.ModelNoteController.view(model, pk)
    val call = controllers.admin.note.routes.NoteRowController.create()
    val cfg = app.cfg(Some(request.identity), admin = true)
    Future.successful(Ok(views.html.admin.note.noteRowForm(cfg, note, s"Note for $model:$pk", cancel, call, isNew = true, debug = app.config.debug)))
  }
}
