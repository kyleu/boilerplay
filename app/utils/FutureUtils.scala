package utils

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object FutureUtils {
  implicit val defaultContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
}
