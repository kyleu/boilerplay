/* Generated File */
package controllers.admin.test

import com.google.inject.Injector
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.module.Application
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class TestController @javax.inject.Inject() (
    override val app: Application, inj: Injector
)(implicit ec: ExecutionContext) extends AuthController("test") {
  def test(t: Option[String] = None) = withSession("test") { implicit request => _ =>
    Future.successful(renderChoice(t) {
      case MimeTypes.HTML => Ok("TODO!")
      case MimeTypes.JSON => Ok("TODO.json!")
    })
  }
}
