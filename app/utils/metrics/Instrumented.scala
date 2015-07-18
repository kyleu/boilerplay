package utils.metrics

import java.lang.management.ManagementFactory

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.jvm._
import nl.grons.metrics.scala.{ MetricName, InstrumentedBuilder }
import utils.Config

object Instrumented {
  val metricRegistry = new MetricRegistry()
  metricRegistry.register("jvm.buffer-pools", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer))
  metricRegistry.register("jvm.class-loading", new ClassLoadingGaugeSet())
  metricRegistry.register("jvm.fd.usage", new FileDescriptorRatioGauge())
  metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet())
  metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet())
  metricRegistry.register("jvm.thread-states", new ThreadStatesGaugeSet())
}

trait Instrumented extends InstrumentedBuilder {
  override lazy val metricBaseName = MetricName(s"${Config.projectId}.${getClass.getSimpleName.replaceAllLiterally("$", "")}")
  override val metricRegistry = Instrumented.metricRegistry
}
