package services.database

import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.pool.{ConnectionPool, PoolConfiguration}
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import util.FutureUtils.databaseContext
import util.tracing.TracingService

import scala.concurrent.Await
import scala.concurrent.duration._

abstract class BaseDatabase(configPrefix: String) extends DatabaseHelper {
  private[this] val poolConfig = new PoolConfiguration(maxObjects = 100, maxIdle = 10, maxQueueSize = 1000)

  private[this] var poolOption: Option[ConnectionPool[PostgreSQLConnection]] = None
  protected override def pool: ConnectionPool[PostgreSQLConnection] = poolOption.getOrElse {
    throw new IllegalStateException("Pool not configured. Did you forget to call \"open\"?")
  }

  private[this] var tracingServiceOpt: Option[TracingService] = None
  protected override def tracing = tracingServiceOpt.getOrElse {
    throw new IllegalStateException("Tracing service not configured. Did you forget to call \"open\"?")
  }

  private[this] var config: Option[Configuration] = None
  override def getConfig = config.getOrElse(throw new IllegalStateException("Database not open."))

  def open(cfg: play.api.Configuration, svc: TracingService): Unit = {
    val sectionName = cfg.get[String](configPrefix + ".section")
    val section = configPrefix + "." + sectionName

    def get(k: String) = cfg.get[String](section + "." + k)
    tracingServiceOpt = Some(svc)
    open(get("host"), get("port").toInt, get("username"), Some(get("password")), Some(get("database")))
  }

  private[this] def open(host: String, port: Int = 5432, username: String, password: Option[String] = None, database: Option[String] = None): Unit = {
    open(Configuration(username, host, port, password, database))
  }

  private[this] def open(configuration: Configuration): Unit = {
    val factory = new PostgreSQLConnectionFactory(configuration)
    poolOption = Some(new ConnectionPool(factory, poolConfig))
    config = Some(configuration)
    val healthCheck = pool.sendQuery("select now()")
    healthCheck.failed.foreach(x => throw new IllegalStateException("Cannot connect to database.", x))
    Await.result(healthCheck.map(r => r.rowsAffected == 1.toLong), 5.seconds)
  }

  def close() = {
    Await.result(pool.close, 5.seconds)
    true
  }
}
