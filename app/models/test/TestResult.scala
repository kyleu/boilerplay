package models.test

case class TestResult(id: String, executionMs: Int, value: Option[Any], ex: Option[Throwable] = None)
