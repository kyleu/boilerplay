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

  private[this] def wireField(checkbox: JQuery): Unit = {
    val name = checkbox.data("name").toString
    val t = checkbox.data("type").toString
    Logging.info(s" - Wiring [$name:$t].")
    t match {
      case "boolean" =>
        val tInput = $(s"#input-$name-true", formEl)
        if (tInput.length != 1) {
          throw new IllegalStateException(s"Found [${tInput.length}] $t input elements with id [$id / true].")
        }
        val fInput = $(s"#input-$name-false", formEl)
        if (fInput.length != 1) {
          throw new IllegalStateException(s"Found [${fInput.length}] $t input elements with id [$id / false].")
        }
        val originalValue = tInput.filter(":checked").length == 1
        tInput.change((_: JQueryEventObject) => checkbox.prop("checked", !originalValue))
        fInput.change((_: JQueryEventObject) => checkbox.prop("checked", originalValue))
      case _ =>
        val input = $(s"#input-$name", formEl)
        if (input.length != 1) {
          throw new IllegalStateException(s"Found [${input.length}] $t input elements with id [$id].")
        }
        val originalValue = input.value().toString
        input.keyup((_: JQueryEventObject) => checkbox.prop("checked", originalValue != input.value().toString))
    }
  }
}
