package services.supervisor

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import models._
import models.user.User
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Akka
import utils.{ DateUtils, Logging }
import utils.metrics.{ InstrumentedActor, MetricsServletActor }

object ActorSupervisor extends Logging {
  lazy val instance = {
    import play.api.Play.current
    val instanceRef = Akka.system.actorOf(Props[ActorSupervisor], "supervisor")
    log.info(s"Actor Supervisor [${instanceRef.path.toString}] started for [${utils.Config.projectId}].")
    instanceRef
  }

  case class ConnectionRecord(userId: UUID, name: String, actorRef: ActorRef, var activeGame: Option[UUID], started: LocalDateTime)
}

class ActorSupervisor extends InstrumentedActor with  Logging {
  import ActorSupervisor.ConnectionRecord

  protected[this] val connections = collection.mutable.HashMap.empty[UUID, ConnectionRecord]
  protected[this] val connectionsCounter = metrics.counter("active-connections")

  override def preStart() {
    context.actorOf(Props[MetricsServletActor], "metrics-servlet")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }

  override def receiveRequest = {
    case cs: ConnectionStarted => timeReceive(cs) { handleConnectionStarted(cs.user, cs.connectionId, cs.conn) }
    case cs: ConnectionStopped => timeReceive(cs) { handleConnectionStopped(cs.connectionId) }

    case GetSystemStatus => timeReceive(GetSystemStatus) { handleGetSystemStatus() }
    case ct: ConnectionTrace => timeReceive(ct) { handleConnectionTrace(ct) }
    case ct: ClientTrace => timeReceive(ct) { handleClientTrace(ct) }

    case sm: InternalMessage => log.warn(s"Unhandled internal message [${sm.getClass.getSimpleName}] received.")
    case x => log.warn(s"ActorSupervisor encountered unknown message: ${x.toString}")
  }

  private[this] def handleGetSystemStatus() = {
    val connectionStatuses = connections.toList.sortBy(_._2.name).map(x => x._1 -> x._2.name)
    sender() ! SystemStatus(connectionStatuses)
  }

  private[this] def handleConnectionTrace(ct: ConnectionTrace) = connections.find(_._1 == ct.id) match {
    case Some(g) => g._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Connection", ct.id.toString)
  }

  private[this] def handleClientTrace(ct: ClientTrace) = connections.find(_._1 == ct.id) match {
    case Some(g) => g._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Client", ct.id.toString)
  }

  protected[this] def handleConnectionStarted(user: User, connectionId: UUID, conn: ActorRef) {
    log.debug(s"Connection [$connectionId] registered to [${user.username.getOrElse(user.id)}] with path [${conn.path}].")
    connections(connectionId) = ConnectionRecord(user.id, user.username.getOrElse("Guest"), conn, None, DateUtils.now)
    connectionsCounter.inc()
  }

  protected[this] def handleConnectionStopped(id: UUID) {
    connections.remove(id) match {
      case Some(conn) =>
        connectionsCounter.dec()
        log.debug(s"Connection [$id] [${conn.actorRef.path}] stopped.")
      case None => log.warn(s"Connection [$id] stopped but is not registered.")
    }
  }
}
