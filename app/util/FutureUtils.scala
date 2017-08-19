package util

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

object FutureUtils {
  implicit val defaultContext = ExecutionContext.global
  implicit val databaseContext = ExecutionContext.global
  implicit val graphQlContext = ExecutionContext.global
}

@javax.inject.Singleton
class FutureUtils @javax.inject.Inject() (actorSystem: ActorSystem) {
  private[this] def contextKey(k: String) = "context." + k

  implicit val webContext = actorSystem.dispatchers.lookup(contextKey("web"))
}
