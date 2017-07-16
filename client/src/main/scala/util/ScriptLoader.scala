package util

import org.scalajs.jquery.{jQuery => $}

import scala.scalajs.js

object ScriptLoader {
  private[this] val scriptRoutes = js.Dynamic.global.scriptRoutes

  private[this] val scripts = Seq(
    "charting" -> scriptRoutes.charting.toString,
    "plotly" -> scriptRoutes.plotly.toString
  )
  private[this] var loadedScripts = Seq.empty[String]

  $.ajaxSetup(js.Dynamic.literal(
    "cache" -> true
  ))

  def loadScript(key: String, callback: () => Unit): Unit = {
    if (loadedScripts.contains(key)) {
      callback()
    } else {
      val url = scripts.find(_._1 == key).map(_._2).getOrElse(throw new IllegalStateException(s"Invalid script [$key]."))
      $.getScript(url, () => {
        loadedScripts = loadedScripts :+ key
        callback()
      })
    }
  }
}
