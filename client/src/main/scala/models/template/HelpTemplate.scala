package models.template

import utils.{KeyboardShortcut, Messages, NumberUtils}

import scalatags.Text.all._

object HelpTemplate {
  val content = {
    val (globalShortcuts, nonGlobalShortcuts) = KeyboardShortcut.values.partition(_.isGlobal)

    val content = div(
      div(cls := "row")(
        div(cls := "col s12")(
          div(cls := "z-depth-1 help-panel")(
            h5(Messages("help.tips.and.tricks")),
            div(id := "tip-detail")(Messages("general.loading")),
            div(
              div(cls := "left")(a(cls := "previous-tip-link theme-text", href := "")(Messages("general.previous"))),
              div(cls := "right")(a(cls := "next-tip-link theme-text", href := "")(Messages("general.next"))),
              div(style := "clear: both;")
            )
          )
        )
      ),
      div(cls := "row")(
        div(cls := "col s12 m6")(
          div(cls := "z-depth-1 help-panel")(
            h5(Messages("help.global.shortcuts")),
            table(cls := "bordered highlight")(
              tbody(
                globalShortcuts.map(s => patternToRow(s))
              )
            )
          )
        ),
        div(cls := "col s12 m6")(
          div(cls := "z-depth-1 help-panel")(
            h5(Messages("help.editor.shortcuts")),
            table(cls := "bordered highlight")(
              tbody(
                nonGlobalShortcuts.map(s => patternToRow(s))
              )
            )
          )
        )
      ),
      div(cls := "row")(
        div(cls := "col s12")(
          div(cls := "z-depth-1 help-panel")(
            h5(Messages("th.connection")),
            div(cls := "connection-status")()
          )
        )
      )
    )
    StaticPanelTemplate.panelRow(content, iconAndTitle = Some(Icons.help -> span(Messages("help.title", utils.Config.projectName))))
  }

  private[this] def patternToRow(s: KeyboardShortcut) = tr(td(s.pattern), td(Messages("help.hotkey." + s.key)))
}
