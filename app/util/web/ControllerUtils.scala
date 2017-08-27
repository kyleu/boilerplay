package util.web

import play.api.data.FormError
import play.api.mvc.{Accepting, AnyContent, Request}

object ControllerUtils {
  val acceptsCsv = Accepting("text/csv")

  def getForm(request: Request[AnyContent]) = request.body.asFormUrlEncoded match {
    case Some(f) => f.mapValues(x => x.headOption.getOrElse(throw new IllegalStateException("Empty form element.")))
    case None => throw new IllegalStateException("Missing form post.")
  }

  def errorsToString(errors: Seq[FormError]) = errors.map(e => e.key + ": " + e.message).mkString(", ")
}
