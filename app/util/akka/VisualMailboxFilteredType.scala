package util.akka

import akka.actor.{ActorRef, ActorSystem}
import akka.dispatch._
import com.typesafe.config.Config

class VisualMailboxFilteredType() extends MailboxType with ProducesMessageQueue[VisualMailboxFiltered] {
  def this(settings: ActorSystem.Settings, config: Config) = this()

  final override def create(owner: Option[ActorRef], system: Option[ActorSystem]): MessageQueue = {
    new VisualMailboxFiltered(UnboundedMailbox().create(owner, system), owner, system) with UnboundedMessageQueueSemantics with MultipleConsumerSemantics
  }
}
