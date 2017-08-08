package controllers.admin

import java.util.UUID

import controllers.BaseController
import play.twirl.api.Html
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
    // Start uuid searches
    val userF = services.user.UserService.getById(id).map {
      case Some(u) => Seq(views.html.admin.user.userSearchResult(u, s"User [${u.username}] matched id [$q]."))
      case None => Nil
    }

    val uuidSearches = Seq(userF)
    // End uuid searches

    Future.sequence(uuidSearches).map(_.flatten)
  }

  private[this] def searchInt(q: String, id: Int) = {
    // Start int searches
    val intSearches = Seq[Future[Seq[Html]]]()
    // End int searches

    Future.sequence(intSearches).map(_.flatten)
  }

  private[this] def searchString(q: String) = {
    // Start string searches
    val userF = services.user.UserService.searchExact(q, limit = Some(10)).map { users =>
      users.map(u => views.html.admin.user.userSearchResult(u, s"User [${u.username}] matched [$q]."))
    }

    val stringSearches = Seq(userF)
    // End string searches

    Future.sequence(stringSearches).map(_.flatten)
  }
}
