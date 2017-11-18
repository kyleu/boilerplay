package util.web

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorRefFactory, OneForOneStrategy, PoisonPill, Props, Status, SupervisorStrategy, Terminated}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}

object WebsocketUtils {
  // Copied from `play.api.libs.streams.ActorFlow`, added actor names.
  def actorRef[In, Out](connId: UUID)(
    props: ActorRef => Props, bufferSize: Int = 16, overflowStrategy: OverflowStrategy = OverflowStrategy.dropNew
  )(implicit factory: ActorRefFactory, mat: Materializer): Flow[In, Out, _] = {
    val (outActor, publisher) = Source.actorRef[Out](bufferSize, overflowStrategy).toMat(Sink.asPublisher(false))(Keep.both).run()

    Flow.fromSinkAndSource(
      Sink.actorRef(factory.actorOf(Props(new Actor {
        val flowActor = context.watch(context.actorOf(props(outActor), "flow-" + connId))

        def receive = {
          case Status.Success(_) | Status.Failure(_) => flowActor ! PoisonPill
          case Terminated(_) => context.stop(self)
          case other => flowActor ! other
        }

        override def supervisorStrategy = OneForOneStrategy() {
          case _ => SupervisorStrategy.Stop
        }
      }), "websocket-" + connId), Status.Success(())),
      Source.fromPublisher(publisher)
    )
  }
}
