package services.relations

import models.entrypoint.Entrypoint
import models.result.RelationCount
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}
import util.Logging

@JSExportTopLevel("RelationService")
class RelationService(url: String) extends Entrypoint("relation") {
  Logging.debug(s"Relation service running, using [$url].")

  private[this] def onComplete(body: JQuery, data: String): Unit = {
    body.html(data)
    $(".sort-link, .next-link, .prev-link", body).click { e: JQueryEventObject =>
      val jq = $(e.currentTarget)
      val href = jq.attr("href").toString
      call(body, href)
      false
    }
  }

  private[this] def call(body: JQuery, url: String): Unit = {
    $.get(url = url, data = scalajs.js.Dynamic.literal(), success = (data: String) => onComplete(body, data))
  }

  private[this] def onOpen(el: dom.Element) = {
    val body = $(".collapsible-body", el)
    if ($("table", body).length == 0) {
      val url = $(el).data("url").toString
      call(body, url)
      Logging.debug("Initialized new relation")
    } else {
      Logging.debug("Using cached relation")
    }
  }

  private[this] def processCount(model: String, field: String, count: Int) = {
    val jq = $(s"#relation-$model-$field")
    val title = $(".title", jq)
    if (jq.length != 1) {
      throw new IllegalStateException(s"Missing relation section for [$model:$field].")
    }
    if (count == 1) {
      val singular = jq.data("singular").toString
      title.text(util.NumberUtils.withCommas(count) + " " + singular)
    } else {
      val plural = jq.data("plural").toString
      title.text(util.NumberUtils.withCommas(count) + " " + plural)
    }
  }

  $.get(url = url, data = scalajs.js.Dynamic.literal(), success = (data: String) => {
    import util.JsonSerializers._
    decodeJson[Seq[RelationCount]](data) match {
      case Right(seq) => seq.foreach(rc => processCount(rc.model, rc.field, rc.count))
      case Left(x) => throw x
    }
  }, dataType = "text")

  val arg = scalajs.js.Dynamic.literal("onOpenStart" -> onOpen _)
  scalajs.js.Dynamic.global.$("#model-relations").collapsible(arg)
}
