package controllers.admin.user

import java.util.UUID

import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import models.result.orderBy.OrderBy
import models.user.UserResult

import scala.concurrent.Future

trait UserSearchHelper { this: UserController =>
  import app.contexts.webContext

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("user.list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      val r = q match {
        case Some(query) if query.nonEmpty => app.userService.searchWithCount(query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => app.userService.getAllWithCount(Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      Future.successful(render {
        case Accepts.Html() => Ok(views.html.admin.user.userList(
          request.identity, q, orderBy, orderAsc, Some(r._1), r._2, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(UserResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson.spaces2).as(JSON)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("user.autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      val r = q match {
        case Some(query) if query.nonEmpty => app.userService.search(query, Nil, orderBys, limit.orElse(Some(5)), None)
        case _ => app.userService.getAll(Nil, orderBys, limit.orElse(Some(5)))
      }
      Future.successful(Ok(r.map(_.toSummary).asJson.spaces2).as(JSON))
    }
  }

  def view(id: UUID) = withSession("user.view", admin = true) { implicit request => implicit td =>
    val notes = app.noteService.getFor("user", id)
    app.userService.getByPrimaryKey(id) match {
      case Some(model) => Future.successful(render {
        case Accepts.Html() => Ok(views.html.admin.user.userView(request.identity, model, notes, app.config.debug))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      })
      case None => Future.successful(NotFound(s"No user found with id [$id]."))
    }
  }
}
