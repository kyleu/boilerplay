package services

object NavigationService {
  private[this] lazy val loc = org.scalajs.dom.document.location

  lazy val socketUrl = {
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    s"$wsProtocol://${loc.host}/connect"
  }
}
