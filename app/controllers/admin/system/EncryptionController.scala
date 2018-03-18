package controllers.admin.system

import controllers.BaseController
import models.Application
import util.EncryptionUtils
import util.FutureUtils.defaultContext
import util.web.ControllerUtils

import scala.concurrent.Future

@javax.inject.Singleton
class EncryptionController @javax.inject.Inject() (override val app: Application) extends BaseController("encryption") {
  def form = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.encryption(request.identity)))
  }

  def post() = withSession("list", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val action = form.get("action")
    val (unenc, enc) = action match {
      case Some("encrypt") =>
        val u = form.getOrElse("unenc", throw new IllegalStateException("Must provide [unenc] value when action is [encrypt]."))
        u -> EncryptionUtils.encrypt(u)
      case Some("decrypt") =>
        val e = form.getOrElse("enc", throw new IllegalStateException("Must provide [enc] value when action is [decrypt]."))
        EncryptionUtils.decrypt(e) -> e
      case _ => throw new IllegalStateException("Must provide [action] value of \"encrypt\" or \"decrypt\".")
    }

    Future.successful(Ok(views.html.admin.encryption(request.identity, unenc, enc)))
  }
}
