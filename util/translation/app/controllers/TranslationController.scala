package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}
import services.translation.TranslationService

import scala.concurrent.Future

@javax.inject.Singleton
class TranslationController @javax.inject.Inject() (translationService: TranslationService) extends Controller {
  val apiProvider = "yandex"

  def index = Action.async {
    Future.successful(Ok(views.html.index()))
  }

  def client = translate("./conf/client")
  def server = translate("./conf")
  def site = translate("./site/conf")
  def test = translate("./tmp/messagetest")

  private[this] def translate(s: String) = Action.async { request =>
    val force = request.queryString.get("force").flatMap(_.headOption).contains("true")
    val root = new java.io.File(s)
    val keys = translationService.translateAll(apiProvider, root, force)
    keys._2.map(v => Ok(views.html.display(v)))
  }
}
