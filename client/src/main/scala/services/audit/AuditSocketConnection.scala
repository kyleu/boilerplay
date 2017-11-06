package services.audit

import services.event.AuditEventHandler
import services.socket.SocketConnection

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("AuditSocketConnection")
class AuditSocketConnection(binary: Boolean = false, debug: Boolean = false) extends SocketConnection("audit", new AuditEventHandler, binary, debug) {
  connect("/admin/audit/activity/connect")
}
