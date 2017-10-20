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

        if (input.hasClass("lookup")) {
          val model = input.data("model").toString
          val url = input.data("url").toString
          Logging.info(s" - Wiring [$model] autocomplete for [$name] as [$url].")

          val dyn = js.Dynamic.global.$(s"#input-$name", formEl)

          val autocomplete = dyn.materialize_autocomplete(js.Dynamic.literal(
            limit = 5,
            multiple = js.Dynamic.literal(enable = false),
            dropdown = js.Dynamic.literal(
              el = s"#dropdown-$name",
              itemTemplate = """<li class="ac-item" data-id="<%= item.id %>" data-text="<%= item.id %>"><a href="javascript:void(0)"><%= item.text %></a></li>""",
              noItem = s"No matching $model records found."
            ),
            getData = (s: String, callback: js.Function2[String, js.Array[js.Object with js.Dynamic], Unit]) => {
              $.getJSON(url, js.Dynamic.literal(q = s), (data: js.Array[js.Object with js.Dynamic]) => {
                callback(s, data.map(r => js.Dynamic.literal(id = r.pk.toString, text = r.title)))
              })
            }
          ))
        }
        val originalValue = input.value().toString
        input.keyup((_: JQueryEventObject) => {
          checkbox.prop("checked", originalValue != input.value().toString)
        })
    }
  }
}
