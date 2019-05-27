/* Generated File */
package controllers.admin.film

import com.kyleu.projectile.controllers.{AuthController, ServiceController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.util.JsonSerializers._
import models.film.MpaaRatingType
import play.twirl.api.Html
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class MpaaRatingTypeController @javax.inject.Inject() (override val app: Application)(implicit ec: ExecutionContext) extends AuthController("mpaaRatingType") {
  def list = withSession("list", ("film", "MpaaRatingType", "list")) { implicit request => implicit td =>
    Future.successful(render {
      case Accepts.Html() => Ok(com.kyleu.projectile.views.html.admin.layout.listPage(
        title = "MpaaRatingType",
        cfg = app.cfg(u = Some(request.identity), "film", "mpaa_rating"),
        vals = MpaaRatingType.values.map(v => Html(v.toString))
      ))
      case Accepts.Json() => Ok(MpaaRatingType.values.asJson)
      case ServiceController.acceptsCsv() => Ok(MpaaRatingType.values.mkString(", ")).as("text/csv")
    })
  }
}
