package services.form

import org.scalajs.dom
import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}
import services.{InitService, Logging}

import scala.scalajs.js
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
        val autocomplete = input.hasClass("autocomplete")
        if (autocomplete) {
          val model = input.data("model")
          Logging.info(s" - Wiring [$model] autocomplete for [$name].")
          val dyn = scalajs.js.Dynamic.global.$(s"#input-$name", formEl)
          val autocomplete = dyn.materialize_autocomplete(scalajs.js.Dynamic.literal(
            limit = 5,
            multiple = scalajs.js.Dynamic.literal(enable = false),
            dropdown = scalajs.js.Dynamic.literal(
              el = s"#dropdown-$name",
              itemTemplate = """<li class="ac-item" data-id="<%= item.id %>" data-text="<%= item.id %>"><a href="javascript:void(0)"><%= item.text %></a></li>""",
              noItem = s"No matching $model records found."
            ),
            getData = (s: String, callback: js.Function2[String, js.Array[js.Dynamic], Unit]) => {
              Logging.info(s + "!!!")
              callback(s, js.Array(
                scalajs.js.Dynamic.literal(id = "00000000", text = "Kyle"),
                scalajs.js.Dynamic.literal(id = "11111111", text = "Trish")
              ))
            }
          ))
          /*
          var autocomplete = $('#el').materialize_autocomplete({
            limit: 20,
            multiple: {
                enable: true,
                maxSize: 10,
                onExist: function (item) { /* ... */ },
                onExceed: function (maxSize, item) { /* ... */ }
            },
            appender: {
                el: '#someEl'
            },
            getData: function (value, callback) {
                // ...
                callback(value, data);
            }
         });
         */
        }
        val originalValue = input.value().toString
        input.keyup((_: JQueryEventObject) => {
          checkbox.prop("checked", originalValue != input.value().toString)
        })
    }
  }
}
