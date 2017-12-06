package util.metrics

import akka.actor.Actor
import io.prometheus.client.{Counter, Gauge, Histogram}

import scala.util.control.NonFatal

trait InstrumentedActor extends Actor {
  private[this] def cn(x: Any) = x.getClass.getSimpleName.replaceAllLiterally("$", "")

  protected[this] lazy val metricsName = util.Config.projectId + "_" + cn(this)
  protected[this] lazy val errorCounter = Counter.build(metricsName + "_exception", s"Exception metrics for [$metricsName]").labelNames("msg", "ex").register()
  protected[this] lazy val receiveHistogram = Histogram.build(metricsName + "_receive", s"Merrage metrics for [$metricsName]").labelNames("msg").register()

  def receiveRequest: PartialFunction[Any, Unit]

  protected[this] def counter(key: String, help: String, labels: Seq[String] = Nil) = {
    Counter.build(metricsName + "_" + key, help).labelNames(labels: _*).register()
  }
  protected[this] def gauge(key: String, help: String, labels: Seq[String] = Nil) = {
    Gauge.build(metricsName + "_" + key, help).labelNames(labels: _*).register()
  }

  private[this] def timeReceive[A](msg: Any)(f: => A) = {
    val t = receiveHistogram.labels(cn(msg)).startTimer()
    try {
      f
    } catch {
      case NonFatal(x) => errorCounter.labels(cn(msg), cn(x)).inc()
    } finally {
      t.close()
    }
  }

  final override def receive = {
    case x if receiveRequest.isDefinedAt(x) => timeReceive(x)(receiveRequest.apply(x))
  }
}
