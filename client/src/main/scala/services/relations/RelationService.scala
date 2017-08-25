package services.relations

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.jquery.{JQuery, jQuery => $}
import scribe.Logging

@JSExportTopLevel("RelationService")
class RelationService(url: String) extends Logging {
  logger.info(s"Relation service running, using [$url].")

  private[this] def onOpen(el: JQuery) = {
    val header = $(".collapsible-header", el)
    val body = $(".collapsible-body", el)

    val url = el.data("url")

    if ($(".todo", body).length == 0) {
      body.html("<div class=\"todo\">Loaded!</div>")
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
