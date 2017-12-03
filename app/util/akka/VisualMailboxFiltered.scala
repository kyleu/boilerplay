package util.akka

import akka.actor.{ActorRef, ActorSystem}
import akka.dispatch._
import de.aktey.akka.visualmailbox.VisualMailbox

class VisualMailboxFiltered(backend: MessageQueue, owner: Option[ActorRef], system: Option[ActorSystem]) extends VisualMailbox(backend, owner, system) {
  override def enqueue(receiver: ActorRef, handle: Envelope) = {
    val path = receiver.path.toSerializationFormat + "/" + handle.sender.path.toSerializationFormat
    if (path.contains("inputStreamSource") || path.contains("headSink") || path.contains("actorRefSource")) {
      backend.enqueue(receiver, handle)
    } else {
      super.enqueue(receiver, handle)
    }
  }
}
