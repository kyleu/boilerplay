package services.test

import java.util.UUID

import models.test.{ Tree, TestResult, Test }
import models.user.{ UserPreferences, User }
import org.joda.time.LocalDateTime
import utils.Logging

object TestService extends Logging {
  val testUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")
  val testUser = User(
    id = TestService.testUserId,
    username = Some("test-user"),
    preferences = UserPreferences(),
    profiles = Nil,
    created = new LocalDateTime()
  )

  def run(tree: Tree[Test]): Tree[TestResult] = {
    val result = run(tree.node)
    val childResults = tree.children.map(run)
    Tree(result, childResults)
  }

  def run(test: Test): TestResult = {
    val startMs = System.currentTimeMillis
    try {
      val result = test.run()
      TestResult(test.id, (System.currentTimeMillis - startMs).toInt, Some(result))
    } catch {
      case x: Exception =>
        log.warn(s"Exception encountered processing test [${test.id}].", x)
        TestResult(test.id, (System.currentTimeMillis - startMs).toInt, None, Some(x))
      case x: Error =>
        log.warn(s"Error encountered processing test [${test.id}].", x)
        TestResult(test.id, (System.currentTimeMillis - startMs).toInt, None, Some(x))
    }
  }
}
