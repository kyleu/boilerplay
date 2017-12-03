package services.audit

import services.socket.SocketConnection

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("AuditSocketConnection")
class AuditSocketConnection(binary: Boolean = false) extends SocketConnection("audit", new AuditEventHandler, binary) {
  connect("/admin/audit/activity/connect")
}
