package util.metrics

import play.api.Configuration

@javax.inject.Singleton
class MetricsConfig @javax.inject.Inject() (cnf: Configuration) {
  val prometheusEnabled = cnf.get[Boolean]("metrics.prometheus.enabled")
  val prometheusPort = cnf.get[Int]("metrics.prometheus.port")

  val tracingEnabled = cnf.get[Boolean]("metrics.tracing.enabled")
  val tracingServer = Option(System.getenv("ZIPKIN_SERVICE_HOST")) match {
    case Some(host) => host
    case _ => cnf.get[String]("metrics.tracing.server")
  }
  val tracingPort = Option(System.getenv("ZIPKIN_SERVICE_PORT")) match {
    case Some(port) => port.toInt
    case _ => cnf.get[Int]("metrics.tracing.port")
  }
  val tracingSampleRate = cnf.get[Double]("metrics.tracing.sampleRate").toFloat
}

