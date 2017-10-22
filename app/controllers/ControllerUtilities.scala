package controllers

import brave.Span
import io.circe.Json
import models.result.data.DataField
import models.user.User
import play.api.mvc._
import sangria.marshalling.MarshallingUtil._
import sangria.marshalling.circe._
import zipkin.TraceKeys

object ControllerUtilities {
  def modelForm(rawForm: Option[Map[String, Seq[String]]]) = {
    val form = rawForm.getOrElse(Map.empty).mapValues(_.head)
    val fields = form.toSeq.filter(x => x._1.endsWith(".include") && x._2 == "true").map(_._1.stripSuffix(".include"))
    def valFor(f: String) = form.get(f) match {
      case Some(x) => x
      case None => form.get(f + "-date") match {
        case Some(d) => form.get(f + "-time") match {
          case Some(t) => s"$d $t"
          case None => throw new IllegalStateException(s"Cannot find matching time value for included date field [$f].")
        }
        case None => throw new IllegalStateException(s"Cannot find value for included field [$f].")
      }
    }
    fields.map(f => DataField(f, Some(valFor(f))))
  }

  def jsonFor(request: Request[AnyContent]) = {
    import sangria.marshalling.playJson._
    val playJson = request.body.asJson.getOrElse(throw new IllegalStateException("Invalid JSON."))
    playJson.convertMarshaled[Json]
  }

  def enhanceRequest(request: Request[AnyContent], user: Option[User], trace: Span) = {
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, request.body.asText.map(_.length).orElse(request.body.asRaw.map(_.size)).getOrElse(0).toString)
    user.foreach { u =>
      trace.tag("user.id", u.id.toString)
      trace.tag("user.username", u.username)
      trace.tag("user.email", u.profile.providerKey)
      trace.tag("user.role", u.role.toString)
    }
  }
}
