package models.form

import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}

object FieldHelper {
  def onBoolean(name: String, formEl: JQuery, checkbox: JQuery) = {
    val tInput = $(s"#input-$name-true", formEl)
    if (tInput.length != 1) {
      throw new IllegalStateException(s"Found [${tInput.length}] boolean input elements with id [input-$name-true].")
    }
    val fInput = $(s"#input-$name-false", formEl)
    if (fInput.length != 1) {
      throw new IllegalStateException(s"Found [${fInput.length}] boolean input elements with id [input-$name-false].")
    }
    val originalValue = tInput.filter(":checked").length == 1
    tInput.change((_: JQueryEventObject) => checkbox.prop("checked", !originalValue))
    fInput.change((_: JQueryEventObject) => checkbox.prop("checked", originalValue))
  }

  def onDate(name: String, formEl: JQuery, checkbox: JQuery) = {
    val input = $(s"#input-$name", formEl)
    if (input.length != 1) {
      throw new IllegalStateException(s"Found [${input.length}] date input elements with id [input-$name].")
    }
    val originalValue = input.value().toString
    input.keyup((_: JQueryEventObject) => {
      checkbox.prop("checked", originalValue != input.value().toString)
    })
  }

  def onTime(name: String, formEl: JQuery, checkbox: JQuery) = {
    val input = $(s"#input-$name", formEl)
    if (input.length != 1) {
      throw new IllegalStateException(s"Found [${input.length}] date input elements with id [input-$name].")
    }
    val originalValue = input.value().toString
    input.keyup((_: JQueryEventObject) => {
      checkbox.prop("checked", originalValue != input.value().toString)
    })
  }
}
