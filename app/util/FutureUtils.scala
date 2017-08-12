package util

import scala.concurrent.ExecutionContext

object FutureUtils {
  implicit val defaultContext = ExecutionContext.global
  implicit val graphQlContext = ExecutionContext.global
  implicit val webContext = ExecutionContext.global
  implicit val databaseContext = ExecutionContext.global

  // ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
}
