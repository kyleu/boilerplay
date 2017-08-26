package services.relations

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}
import scribe.Logging
import services.InitService

@JSExportTopLevel("RelationService")
class RelationService(url: String) extends Logging {
  InitService.initIfNeeded()
  logger.info(s"Relation service running, using [$url].")

  private[this] def onComplete(body: JQuery, data: String): Unit = {
    body.html(data)
    $(".sort-link, .next-link", body).click { e: JQueryEventObject =>
      val jq = $(e.currentTarget)
      val href = jq.attr("href").toString
      call(body, href)
      false
    }
  }

  private[this] def call(body: JQuery, url: String): Unit = {
    $.get(url = url, data = scalajs.js.Dynamic.literal(), success = (data: String) => onComplete(body, data))
  }

  private[this] def onOpen(el: JQuery) = {
    val body = $(".collapsible-body", el)
    if ($("table", body).length == 0) {
      val url = el.data("url").toString
      call(body, url)
      logger.info("Initialized.")
    } else {
      logger.info("Cached.")
    }
  }

  private[this] def processCount(key: String, count: Int) = {
    val jq = $(s"#relation-$key")
    val title = $(".title", jq)
    if (jq.length != 1) {
      throw new IllegalStateException(s"Missing relation section for [$key].")
    }
    if (count == 1) {
      val singular = jq.data("singular").toString
      title.text(util.NumberUtils.withCommas(count) + " " + singular)
    } else {
      val plural = jq.data("plural").toString
      title.text(util.NumberUtils.withCommas(count) + " " + plural)
    }
  }

  $.get(url = url, data = scalajs.js.Dynamic.literal(), success = (data: scalajs.js.Dictionary[Int]) => data.foreach {
    case (key, count) => processCount(key, count)
  })

  val arg = scalajs.js.Dynamic.literal("onOpen" -> onOpen _)
  scalajs.js.Dynamic.global.$("#model-relations").collapsible(arg)
}
