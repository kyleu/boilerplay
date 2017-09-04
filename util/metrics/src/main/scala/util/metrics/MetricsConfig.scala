package util.metrics

import play.api.Configuration

@javax.inject.Singleton
class MetricsConfig @javax.inject.Inject() (cnf: Configuration) {
  val jmxEnabled = cnf.get[Boolean]("metrics.jmx.enabled")

  val servletEnabled = cnf.get[Boolean]("metrics.servlet.enabled")
  val servletPort = cnf.get[Int]("metrics.servlet.port")

  val graphiteEnabled = cnf.get[Boolean]("metrics.graphite.enabled")
  val graphiteServer = Option(System.getenv("CARBON_RELAY_SERVICE_HOST")) match {
    case Some(host) => host
    case _ => cnf.get[String]("metrics.graphite.server")
  }
  val graphitePort = Option(System.getenv("CARBON_RELAY_SERVICE_PORT")) match {
    case Some(port) => port.toInt
    case _ => cnf.get[Int]("metrics.graphite.port")
  }

  val tracingEnabled = cnf.get[Boolean]("metrics.tracing.enabled")
  val tracingServer = Option(System.getenv("ZIPKIN_SERVICE_HOST")) match {
    case Some(host) => host
    case _ => cnf.get[String]("metrics.tracing.server")
  }
  val tracingPort = Option(System.getenv("ZIPKIN_SERVICE_PORT")) match {
    case Some(port) => port.toInt
    case _ => cnf.get[Int]("metrics.tracing.port")
  }
  val tracingService = cnf.get[String]("metrics.tracing.service")
  val tracingSampleRate = cnf.get[Double]("metrics.tracing.sampleRate").toFloat
}

