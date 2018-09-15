package models.doobie

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import services.database.ApplicationDatabase
import util.metrics.MetricsConfig
import util.tracing.TracingService

object DoobieTestHelper {
  val cnf = Configuration(ConfigFactory.load("application.test.conf"))
  val svc = new TracingService(ActorSystem(), new MetricsConfig(cnf))

  ApplicationDatabase.open(cfg = cnf, svc = svc)

  val yolo = ApplicationDatabase.doobie.db.yolo
}
