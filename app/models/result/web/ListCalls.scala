package models.result.web

import play.api.mvc.Call

final case class ListCalls(
    newCall: Option[Call] = None,
    orderBy: Option[(Option[String], Boolean) => Call] = None,
    search: Option[Call] = None,
    prev: Call,
    next: Call
)
