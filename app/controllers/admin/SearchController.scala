package controllers.admin

import java.util.UUID

import controllers.BaseController
import util.Application
import util.FutureUtils.defaultContext

import scala.concurrent.Future

@javax.inject.Singleton
class SearchController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def search(q: String) = withAdminSession("admin.search") { implicit request =>
    val resultF = try {
      searchInt(q, q.toInt)
    } catch {
      case _: NumberFormatException => try {
        searchUuid(q, UUID.fromString(q))
      } catch {
        case _: IllegalArgumentException => searchString(q)
      }
    }

    resultF.map { results =>
      Ok(views.html.admin.explore.searchResults(q, results, request.identity))
    }
  }

  private[this] def searchUuid(q: String, id: UUID) = {
    val userF = app.userService.getById(id).map {
      case Some(u) => Seq(views.html.admin.user.userSearchResult(u, s"User [${u.username}] matched id [$q]."))
      case None => Nil
    }

    Future.sequence(Seq(userF)).map(_.flatten)
  }

  private[this] def searchInt(q: String, id: Int) = {
    Future.successful(Nil)
  }

  private[this] def searchString(q: String) = {
    val userF = app.userService.searchExact(q, Some(10), None).map { users =>
      users.map(u => views.html.admin.user.userSearchResult(u, s"User [${u.username}] matched [$q]."))
    }

    Future.sequence(Seq(userF)).map(_.flatten)
  }
}
