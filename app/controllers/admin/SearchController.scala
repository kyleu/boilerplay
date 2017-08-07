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

    val userF = services.user.UserService.getById(id).map { modelOpt =>
      modelOpt.map(model => views.html.admin.user.user.searchResultUser(model, s"User [${model.id}] matched [$q].")).toSeq
    }

    val uuidSearches = Seq[Future[Seq[Html]]](userF)

    // End uuid searches

    Future.sequence(uuidSearches).map(_.flatten)
  }

  private[this] def searchInt(q: String, id: Int) = {
    // Start int searches

    val ddlF = services.ddl.DdlService.getById(id).map { modelOpt =>
      modelOpt.map(model => views.html.admin.ddl.ddl.searchResultDdl(model, s"Ddl [${model.id}] matched [$q].")).toSeq
    }

    val intSearches = Seq[Future[Seq[Html]]](ddlF)

    // End int searches

    Future.sequence(intSearches).map(_.flatten)
  }

  private[this] def searchString(q: String) = {
    // Start string searches

    val ddlF = services.ddl.DdlService.searchExact(q, limit = Some(5)).map { models =>
      models.map(model => views.html.admin.ddl.ddl.searchResultDdl(model, s"Ddl [${model.id}] matched [$q]."))
    }
    val passwordInfoF = services.user.PasswordInfoService.searchExact(q, limit = Some(5)).map { models =>
      models.map(model => views.html.admin.user.passwordInfo.searchResultPasswordInfo(model, s"PasswordInfo [${model.provider}, ${model.key}] matched [$q]."))
    }
    val settingValuesF = services.SettingValuesService.searchExact(q, limit = Some(5)).map { models =>
      models.map(model => views.html.admin.settingValues.searchResultSettingValues(model, s"SettingValues [${model.k}] matched [$q]."))
    }
    val userF = services.user.UserService.searchExact(q, limit = Some(5)).map { models =>
      models.map(model => views.html.admin.user.user.searchResultUser(model, s"User [${model.id}] matched [$q]."))
    }

    val stringSearches = Seq[Future[Seq[Html]]](ddlF, passwordInfoF, settingValuesF, userF)

    // End string searches

    Future.sequence(stringSearches).map(_.flatten)
  }
}
