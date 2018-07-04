import java.io.Closeable

import play.sbt.PlayNonBlockingInteractionMode
import sbt.util.{Level, Logger}

object PlayUtils {
  object NonBlockingInteractionMode extends PlayNonBlockingInteractionMode {
    object NullLogger extends Logger {
      def trace(t: => Throwable): Unit = ()
      def success(message: => String): Unit = ()
      def log(level: Level.Value, message: => String): Unit = ()
    }

    private var runningServers: Set[Closeable] = scala.collection.immutable.HashSet.empty

    override def start(server: => Closeable): Unit = synchronized {
      val theServer = server
      if (runningServers(theServer)) {
        println("Noop: This server was already started")
      } else {
        runningServers += theServer
      }
    }

    override def stop(): Unit = synchronized {
      if (runningServers.size > 1) {
        println("Stopping all servers")
      } else if (runningServers.size == 1) {
        println("Stopping server")
      } else {
        println("No running server to stop")
      }
      runningServers.foreach(_.close())
      runningServers = scala.collection.immutable.HashSet.empty
    }
  }
}
