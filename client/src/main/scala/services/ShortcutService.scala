package services

import java.util.UUID

import org.scalajs.dom
import utils.KeyboardShortcut

import scala.scalajs.js

object ShortcutService {
  private[this] val mt = js.Dynamic.global.Mousetrap

  def init() = {
    KeyboardShortcut.values.filter(_.isGlobal).foreach { shortcut =>
      mt.bind(shortcut.pattern, (e: js.Dynamic) => {
        e.preventDefault()
        shortcut.call(None)
      })
    }
  }

  def configureEditor(id: UUID) = {
    val sel = dom.document.querySelector(s"#sql-textarea-$id")
    val trap = mt(sel)

    KeyboardShortcut.values.filterNot(_.isGlobal).foreach { shortcut =>
      trap.bind(shortcut.pattern, (e: js.Dynamic) => {
        e.preventDefault()
        shortcut.call(Some(id))
      })
    }
  }
}
