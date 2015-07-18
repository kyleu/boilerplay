import java.util.UUID

import models._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object Boilerplay extends js.JSApp {
  override def main(): Unit = {}

  private[this] var sendCallback: js.Function1[String, Unit] = _

  @JSExport
  def register(callback: js.Function1[String, Unit]) = {
    sendCallback = callback
  }

  @JSExport
  def receive(c: String, v: js.Dynamic): Unit = {
    c match {
      case "GetVersion" => send(VersionResponse("0.0"))
      case "Ping" => send(Pong(JsonUtils.getLong(v.timestamp)))
      case _ => throw new IllegalStateException(s"Invalid message [$c].")
    }
  }

  protected def send(rm: ResponseMessage): Unit = {
    val json = JsonSerializers.write(rm)
    sendCallback(JsonSerializers.write(json))
  }
}
