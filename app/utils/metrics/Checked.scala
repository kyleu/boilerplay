package utils.metrics

import com.codahale.metrics.health.HealthCheckRegistry
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck
import nl.grons.metrics.scala.{ MetricName, CheckedBuilder }
import utils.Config

object Checked {
  val healthCheckRegistry = new HealthCheckRegistry()
  healthCheckRegistry.register("jvm.deadlocks", new ThreadDeadlockHealthCheck())
}

trait Checked extends CheckedBuilder {
  override lazy val metricBaseName = MetricName(Config.projectId)
  override val registry = Checked.healthCheckRegistry
}
