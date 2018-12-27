package models

import scala.concurrent.ExecutionContext

object ProjectileContext {
  implicit def defaultContext: ExecutionContext = ExecutionContext.global
  implicit def databaseContext: ExecutionContext = ExecutionContext.global
  implicit def serviceContext: ExecutionContext = ExecutionContext.global
  implicit def graphQlContext: ExecutionContext = ExecutionContext.global
  implicit def webContext: ExecutionContext = ExecutionContext.global
}
