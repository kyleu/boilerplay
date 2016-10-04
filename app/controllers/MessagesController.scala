package controllers

import java.net.URL

import models.user.Language
import play.api.i18n.Messages
import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class MessagesController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  private[this] def parseMsgs(url: URL) = Messages.parse(Messages.UrlMessageSource(url), url.toString).fold(e => throw e, identity)

  private[this] lazy val msgs = Language.values.map { x =>
    val l = x.code
    val filename = if (l == "en") { "client/messages" } else { s"client/messages.$l" }
    l -> parseMsgs(getClass.getClassLoader.getResource(filename))
  }.toMap

  private[this] lazy val responses = msgs.map { ms =>
    val vals = ms._2.map { m =>
      s""""${m._1}": "${m._2}""""
    }.mkString(",\n  ")
    ms._1 -> s"""window.messages = {\n  $vals\n}"""
  }

  def strings() = withoutSession("strings") { implicit request =>
    val lang = request.identity match {
      case Some(u) => u.preferences.language.code
      case None => request.acceptLanguages.find(l => msgs.keySet.contains(l.code)).map(_.code).getOrElse("en")
    }
    Future.successful(Ok(responses(lang)).as("application/javascript"))
  }
}
