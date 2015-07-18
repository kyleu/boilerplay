package utils.metrics

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.graphite.{ Graphite, GraphiteReporter }
import com.codahale.metrics.{ MetricFilter, JmxReporter }
import com.codahale.metrics.servlets.AdminServlet
import org.eclipse.jetty.server.{ ServerConnector, Server }
import org.eclipse.jetty.servlet.ServletContextHandler
import utils.{ Logging, Config }

class MetricsServletActor extends InstrumentedActor with Logging {
  private[this] var jmxReporter: Option[JmxReporter] = None
  private[this] var graphiteReporter: Option[GraphiteReporter] = None
  private[this] var httpServer: Option[Server] = None

  override def preStart(): Unit = {
    if (Config.jmxEnabled) {
      log.info("Reporting metrics over JMX.")
      jmxReporter = Some(JmxReporter.forRegistry(Instrumented.metricRegistry).build())
      jmxReporter.foreach(r => r.start())
    }

    if (Config.graphiteEnabled) {
      log.info(s"Starting Graphite reporter for [${Config.graphiteServer}:${Config.graphitePort}].")
      val graphiteServer = new Graphite(new InetSocketAddress(Config.graphiteServer, Config.graphitePort))
      graphiteReporter = Some(
        GraphiteReporter.forRegistry(Instrumented.metricRegistry)
          .convertRatesTo(TimeUnit.SECONDS)
          .convertDurationsTo(TimeUnit.MILLISECONDS)
          .filter(MetricFilter.ALL)
          .build(graphiteServer)
      )
      graphiteReporter.foreach(r => r.start(1, TimeUnit.MINUTES))
    }

    if (Config.servletEnabled) {
      log.info(s"Starting metrics servlet at [http://0.0.0.0:${Config.servletPort}/].")
      httpServer = Some(createJettyServer())
      httpServer.foreach(s => s.start())
    }

    super.preStart()
  }

  override def postStop(): Unit = {
    jmxReporter.foreach { r =>
      r.stop()
      r.close()
    }
    jmxReporter = None

    graphiteReporter.foreach { r =>
      r.stop()
      r.close()
    }
    graphiteReporter = None

    httpServer.foreach { s =>
      s.stop()
      s.join()
    }
    httpServer = None

    super.postStop()
  }

  override def receiveRequest = {
    case _ => // Ignore...
  }

  def createJettyServer(): Server = {
    val server = new Server()

    val connector = new ServerConnector(server)
    connector.setHost("0.0.0.0")
    connector.setPort(Config.servletPort)
    server.addConnector(connector)

    val handler = new ServletContextHandler()
    handler.getServletContext.setAttribute("com.codahale.metrics.servlets.MetricsServlet.registry", Instrumented.metricRegistry)
    handler.getServletContext.setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.registry", Checked.healthCheckRegistry)
    handler.setContextPath("/")
    handler.addServlet(classOf[AdminServlet], "/*")
    server.setHandler(handler)

    server
  }
}
