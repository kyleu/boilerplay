package models

import play.api.{Environment, Mode}
import util.metrics.MetricsConfig

@javax.inject.Singleton
class Configuration @javax.inject.Inject() (val cnf: play.api.Configuration, val metrics: MetricsConfig, env: Environment) {
  val debug = env.mode == Mode.Dev
  val secretKey = cnf.get[String]("play.http.secret.key")
}
