package models

import com.google.inject.{AbstractModule, Provides}
import com.kyleu.projectile.util.metrics.MetricsConfig
import com.kyleu.projectile.util.tracing.{OpenTracingService, TracingService}
import net.codingwell.scalaguice.ScalaModule

class ProjectileModule extends AbstractModule with ScalaModule {
  @Provides
  def provideTracingService(cnf: MetricsConfig): TracingService = new OpenTracingService(cnf)
}
