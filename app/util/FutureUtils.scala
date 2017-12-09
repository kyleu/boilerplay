package util

import _root_.akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, Future}

object FutureUtils {
  implicit val defaultContext: ExecutionContext = ExecutionContext.global
  implicit val databaseContext: ExecutionContext = ExecutionContext.global
  implicit val serviceContext: ExecutionContext = ExecutionContext.global
  implicit val graphQlContext: ExecutionContext = ExecutionContext.global

  def acc[T, U](seq: Seq[T], f: T => Future[U])(ec: ExecutionContext) = seq.foldLeft(Future.successful(Seq.empty[U])) { (ret, i) =>
    ret.flatMap(s => f(i).map(_ +: s)(ec))(ec)
  }
}

@javax.inject.Singleton
class FutureUtils @javax.inject.Inject() (actorSystem: ActorSystem) {
  private[this] def contextKey(k: String) = "context." + k

  implicit val webContext: ExecutionContext = actorSystem.dispatchers.lookup(contextKey("web"))
}
