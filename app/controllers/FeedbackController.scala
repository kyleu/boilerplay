package controllers

import java.util.UUID

import models.audit.UserFeedback
import models.database.queries.audit.UserFeedbackQueries
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.email.EmailService
import services.user.AuthenticationEnvironment
import utils.DateUtils

import scala.concurrent.Future

@javax.inject.Singleton
class FeedbackController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    emailService: EmailService
) extends BaseController {
  def feedbackForm = withSession("form") { implicit request =>
    Future.successful(Ok(views.html.feedback(request.identity)))
  }

  def submitFeedback = withSession("submit") { implicit request =>
    request.body.asFormUrlEncoded match {
      case Some(form) => form.get("feedback") match {
        case Some(feedback) =>
          val obj = UserFeedback(
            id = UUID.randomUUID,
            userId = request.identity.id,
            feedback = feedback.mkString("\n\n"),
            occurred = DateUtils.now
          )

          emailService.feedbackSubmitted(obj, request.identity)

          Database.execute(UserFeedbackQueries.insert(obj)).map { x =>
            Redirect(routes.FeedbackController.feedbackForm()).flashing("success" -> "Your feedback has been submitted. Thanks!")
          }
        case None => Future.successful(Redirect(routes.FeedbackController.feedbackForm()).flashing("error" -> "Please include some feedback."))
      }
      case None => Future.successful(Redirect(routes.FeedbackController.feedbackForm()).flashing("error" -> "Please include some feedback."))
    }
  }
}
