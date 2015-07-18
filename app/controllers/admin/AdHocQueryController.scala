package controllers.admin

import java.util.UUID

import akka.util.Timeout
import models.database.queries.adhoc.{ AdHocQuery, AdHocQueries }
import org.joda.time.LocalDateTime
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.MessagesApi
import services.database.Database
import services.user.AuthenticationEnvironment
import utils.DateUtils
import scala.concurrent.Future
import scala.concurrent.duration._
import controllers.BaseController
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@javax.inject.Singleton
class AdHocQueryController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment
) extends BaseController {
  case class QueryExecution(action: String, id: Option[String], title: String, sql: String)

  val executionForm = Form(
    mapping(
      "action" -> nonEmptyText,
      "id" -> optional(text),
      "title" -> text,
      "sql" -> nonEmptyText
    )(QueryExecution.apply)(QueryExecution.unapply)
  )

  implicit val timeout = Timeout(10.seconds)

  def queryList(query: Option[UUID], action: Option[String]) = withAdminSession("list") { implicit request =>
    if (action.contains("load")) {
      Database.query(AdHocQueries.search("", "title", None)).map { queries =>
        val q = query.flatMap(x => queries.find(_.id == x))
        Ok(views.html.admin.report.adhoc(query, q.map(_.sql).getOrElse(""), Seq.empty -> Seq.empty, 0, queries))
      }
    } else if (action.contains("delete")) {
      Database.execute(AdHocQueries.removeById(Seq(query.getOrElse(throw new IllegalStateException())))).map { ok =>
        Redirect(controllers.admin.routes.AdHocQueryController.queryList(query, Some("load")))
      }
    } else {
      Database.query(AdHocQueries.search("", "title", None)).map { queries =>
        Ok(views.html.admin.report.adhoc(query, "", Seq.empty -> Seq.empty, 0, queries))
      }
    }
  }

  def run() = withAdminSession("run") { implicit request =>
    import DateUtils._

    executionForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest("Couldn't process form:\n  " + formWithErrors.errors.mkString("  \n"))),
      form => form.action match {
        case "save" => if (form.id.isEmpty || form.id.getOrElse("").isEmpty) {
          val q = AdHocQuery(UUID.randomUUID, form.title, request.identity.id, form.sql, new LocalDateTime, new LocalDateTime)
          Database.execute(AdHocQueries.insert(q)).flatMap { ok =>
            Database.query(AdHocQueries.search("", "title", None)).map { queries =>
              val newId = queries.sortBy(_.created).headOption.map(_.id)
              Ok(views.html.admin.report.adhoc(newId, form.sql, Seq.empty -> Seq.empty, 0, queries))
            }
          }
        } else {
          val queryId = form.id.map(UUID.fromString)
          val q = AdHocQueries.UpdateAdHocQuery(queryId.getOrElse(throw new IllegalStateException()), form.title, request.identity.id, form.sql)
          Database.execute(q).flatMap { ok =>
            Database.query(AdHocQueries.search("", "title", None)).map { queries =>
              Ok(views.html.admin.report.adhoc(queryId, form.sql, Seq.empty -> Seq.empty, 0, queries))
            }
          }
        }
        case "run" =>
          Database.query(AdHocQueries.search("", "title", None)).flatMap { queries =>
            val startTime = System.currentTimeMillis
            Database.query(AdHocQueries.AdHocQueryExecute(form.sql, Seq.empty)).map { result =>
              val executionTime = (System.currentTimeMillis - startTime).toInt
              val queryId = form.id.map(UUID.fromString)
              Ok(views.html.admin.report.adhoc(queryId, form.sql, result, executionTime, queries))
            }
          }
        case x => throw new IllegalStateException(x)
      }
    )
  }
}
