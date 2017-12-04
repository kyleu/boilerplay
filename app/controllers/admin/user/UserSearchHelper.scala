package controllers.admin.user

import io.circe.syntax._
import models.result.orderBy.OrderBy
import models.user.UserResult

trait UserSearchHelper { this: UserController =>
  import app.contexts.webContext

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("user.list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => app.coreServices.users.searchWithCount(request, query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => app.coreServices.users.getAllWithCount(request, Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      f.map(r => render {
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
      val f = q match {
        case Some(query) if query.nonEmpty => app.coreServices.users.search(request, query, Nil, orderBys, limit.orElse(Some(5)), None)
        case _ => app.coreServices.users.getAll(request, Nil, orderBys, limit.orElse(Some(5)))
      }
      f.map(r => Ok(r.map(_.toSummary).asJson.spaces2).as(JSON))
    }
  }
}
