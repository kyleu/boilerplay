package controllers.admin

import controllers.BaseController
import play.api.i18n.MessagesApi
import services.test._
import services.user.AuthenticationEnvironment

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@javax.inject.Singleton
class TestController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def tests = withAdminSession("list") { implicit request =>
    Future.successful(Ok(views.html.admin.test.tests()))
  }

  def runTest(test: String) = withAdminSession("run." + test) { implicit request =>
    Future {
      val testTree = test match {
        case "all" => new AllTests().all
        case "basic" => new BasicTests().all
        case _ => throw new IllegalArgumentException(s"Invalid test [$test].")
      }

      val resultTree = TestService.run(testTree)

      Ok(views.html.admin.test.testResults(resultTree.toSeq()))
    }
  }
}
