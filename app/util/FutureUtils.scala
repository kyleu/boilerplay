package util

import java.util.concurrent.Executors

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

object FutureUtils {
  implicit val defaultContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
  implicit val databaseContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
  implicit val graphQlContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
}

@javax.inject.Singleton
class FutureUtils @javax.inject.Inject() (actorSystem: ActorSystem) {
  private[this] def contextKey(k: String) = "context." + k

  implicit val webContext = actorSystem.dispatchers.lookup(contextKey("web"))
}
