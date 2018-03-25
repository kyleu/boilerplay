package controllers.admin.user

import io.circe.syntax._
import models.result.orderBy.OrderBy
import models.user.SystemUserResult

trait UserSearchHelper { this: SystemUserController =>
  import app.contexts.webContext

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("user.list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => render {
        case Accepts.Html() => Ok(views.html.admin.user.systemUserList(
          request.identity, q, orderBy, orderAsc, Some(r._1), r._2, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(SystemUserResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("user.autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }
}
