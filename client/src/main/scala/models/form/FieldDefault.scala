package models.form

import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}
import util.Logging

import scala.scalajs.js

object FieldDefault {
  private[this] val startLi = """<li class="ac-item theme-text" data-id="<%= item.id %>" data-text="<%= item.id %>">"""
  private[this] val endLi = """</li>"""

  def onDefault(t: String, name: String, formEl: JQuery, checkbox: JQuery) = {
    val input = $(s"#input-$name", formEl)
    if (input.length != 1) {
      throw new IllegalStateException(s"Found [${input.length}] $t input elements with id [input-$name].")
    }

    if (input.hasClass("nullable")) {
      val nullable = $(s"#nullable-$name", formEl)
      if (nullable.length != 1) {
        throw new IllegalStateException(s"Found [${nullable.length}] $t nullable elements with id [nullable-$name].")
      }
      var lastVal: Option[String] = None
      nullable.click { _: JQueryEventObject =>
        lastVal match {
          case Some(v) =>
            lastVal = None
            input.value(v)
          case None =>
            lastVal = Some(input.value().toString)
            input.value(util.NullUtils.str)
        }
      }
    }

    if (input.hasClass("lookup")) {
      val model = input.data("model").toString
      val url = input.data("url").toString
      Logging.info(s" - Wiring [$model] autocomplete for [$name] as [$url].")

      val dyn = js.Dynamic.global.$(s"#input-$name", formEl)

      dyn.autocomplete(js.Dynamic.literal(
        limit = 5,
        multiple = js.Dynamic.literal(enable = false),
        dropdown = js.Dynamic.literal(
          el = s"#dropdown-$name",
          itemTemplate = startLi + """<a href="javascript:void(0)"><%= item.text %></a>""" + endLi,
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
