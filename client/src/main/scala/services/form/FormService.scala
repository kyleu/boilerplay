package services.form

import org.scalajs.dom
import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}
import services.{InitService, Logging}

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("FormService")
class FormService(id: String) {
  InitService.initIfNeeded()

  val formEl = $("#" + id)
  if (formEl.length != 1) {
    throw new IllegalStateException(s"Found [${formEl.length}] form elements with id [$id].")
  }

  val fields = $(".data-input", formEl)
  fields.each { e: dom.Element => wireField($(e)) }
  Logging.info(s"Form service started. [${fields.length}] fields.")

  private[this] def wireField(checkbox: JQuery) = {
    val name = checkbox.data("name").toString
    val t = checkbox.data("type").toString
    t match {
      case _ =>
        val input = $(s"#input-$name", formEl)
        if (input.length != 1) {
          throw new IllegalStateException(s"Found [${input.length}] $t input elements with id [$id].")
        }
        val originalValue = input.value().toString
        input.keyup { _: JQueryEventObject =>
          val newValue = input.value().toString
          if (originalValue == newValue) {
            checkbox.prop("checked", false)
          } else {
            checkbox.prop("checked", true)
          }
        }
    }
    Logging.info(s" - Wiring [$name:$t].")
  }
}
