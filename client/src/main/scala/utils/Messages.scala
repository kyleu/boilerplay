package utils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess

object Messages {
  @js.native
  trait MessageObject extends js.Object {
    @JSBracketAccess
    def apply(key: String): Any = js.native
  }

  lazy val jsMessages = {
    val ret = js.Dynamic.global.messages.asInstanceOf[MessageObject]
    if (ret == None.orNull) {
      throw new IllegalStateException("Missing localization object [messages].")
    }
    ret
  }

  def apply(s: String, args: Any*) = {
    val msg = Option(jsMessages(s)) match {
      case Some(x) => x.toString match {
        case "undefined" => s
        case y => y
      }
      case None => s
    }
    args.zipWithIndex.foldLeft(msg) { (x, y) =>
      x.replaceAllLiterally(s"{${y._2}}", y._1.toString)
    }
  }
}
