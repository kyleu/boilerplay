package services

import models.request.RestRequest
import play.api._
import play.core.server.{ProdServerStart, RealServerProcess, ServerConfig, ServerProvider}
import services.request.RestRequestService

object Entrypoint {
  def main(args: Array[String]): Unit = if (args.isEmpty) {
    startServer(new RealServerProcess(args))
  } else {
    val req = RestRequest(name = "Default REST Request", url = "https://google.com")
    new RestRequestService(util.NullUtils.inst).call(req, s => println(s))
  }

  def startServer(process: RealServerProcess) = {
    val config: ServerConfig = ProdServerStart.readServerConfigSettings(process)
    val application: Application = {
      val environment = Environment(config.rootDir, process.classLoader, Mode.Prod)
      val context = ApplicationLoader.createContext(environment)
      val loader = ApplicationLoader(context)
      loader.load(context)
    }
    Play.start(application)

    val serverProvider: ServerProvider = ServerProvider.fromConfiguration(process.classLoader, config.configuration)
    val server = serverProvider.createServer(config, application)
    process.addShutdownHook(server.stop())
    server
  }
}
