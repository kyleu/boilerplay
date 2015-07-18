package utils.metrics

import akka.actor.Actor
import nl.grons.metrics.scala.{ Timer, Meter }

trait InstrumentedActor extends Actor with Instrumented {
  def receiveRequest: PartialFunction[Any, Unit]

  protected[this] def timeReceive[A](msg: Any)(f: => A) = {
    metrics.timer("receive", msg.getClass.getSimpleName.replaceAllLiterally("$", "")).time(f)
  }

  private[this] lazy val exceptionMeter: Meter = metrics.meter("exceptionMeter")

  private[this] lazy val wrappedExceptionMeter = {
    import scala.language.reflectiveCalls
    exceptionMeter.exceptionMarkerPF(receiveRequest)
  }

  private[this] lazy val timer: Timer = metrics.timer("receiveTimer")
  private[this] lazy val wrappedTimer = timer.timePF(wrappedExceptionMeter)

  final override def receive = wrappedTimer
}
