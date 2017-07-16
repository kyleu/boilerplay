package util.metrics

import com.codahale.metrics.health.HealthCheckRegistry
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck
import nl.grons.metrics.scala.{MetricName, CheckedBuilder}

object Checked {
  val healthCheckRegistry = new HealthCheckRegistry()
  healthCheckRegistry.register("jvm.deadlocks", new ThreadDeadlockHealthCheck())
}

trait Checked extends CheckedBuilder {
  override lazy val metricBaseName = MetricName("boilerplay")
  override val registry = Checked.healthCheckRegistry
}
