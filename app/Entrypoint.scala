import play.api._
import play.core.server.{ProdServerStart, RealServerProcess, ServerConfig, ServerProvider}

object Entrypoint {
  def main(args: Array[String]): Unit = if (args.isEmpty) {
    startServer(new RealServerProcess(args))
  } else {
    throw new IllegalStateException("Unable to parse arguments.")
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
