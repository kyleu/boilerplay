package services.form

import org.scalajs.jquery.{jQuery => $}
import scribe.Logging
import services.InitService

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("FormService")
class FormService(id: String) extends Logging {
  InitService.initIfNeeded()

  val el = $("#" + id)
  if(el.length != 1) {
    throw new IllegalStateException(s"Found [${el.length}] elements with id [$id].")
  }

  logger.info("Form service started. [0] fields.")
}
