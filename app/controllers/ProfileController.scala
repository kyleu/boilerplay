package controllers

import models.database.queries.auth.{ UserQueries, ProfileQueries }
import models.user.Avatars
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.user.AuthenticationEnvironment
import utils.cache.UserCache

import scala.concurrent.Future

@javax.inject.Singleton
class ProfileController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def profile = withSession("profile") { implicit request =>
    Database.query(ProfileQueries.FindProfilesByUser(request.identity.id)).map { profiles =>
      Ok(views.html.profile(request.identity, profiles))
    }
  }

  def setOption(option: String, value: String) = withSession("set.option") { implicit request =>
    option match {
      case "avatar" =>
        val loginInfo = value match {
          case "facebook" => request.identity.profiles.find(_.providerID == "facebook")
          case "google" => request.identity.profiles.find(_.providerID == "google")
          case "twitter" => request.identity.profiles.find(_.providerID == "twitter")
          case _ => None
        }
        val urlFuture = if (Avatars.all.isDefinedAt(value)) {
          Future.successful(Avatars.all(value))
        } else {
          loginInfo match {
            case Some(li) => Database.query(ProfileQueries.FindProfile(li.providerID, li.providerKey)).map(_.flatMap(_.avatarURL).getOrElse(Avatars.default))
            case None => throw new IllegalStateException(s"Cannot find avatar to match [$value].")
          }
        }
        urlFuture.flatMap { url =>
          val prefs = request.identity.preferences.copy(avatar = url)
          Database.execute(UserQueries.SetPreferences(request.identity.id, prefs)).map { i =>
            UserCache.removeUser(request.identity.id)
            Redirect(controllers.routes.ProfileController.profile())
          }
        }
      case "color" =>
        val prefs = request.identity.preferences.copy(color = value)
        Database.execute(UserQueries.SetPreferences(request.identity.id, prefs)).map { x =>
          UserCache.removeUser(request.identity.id)
          Ok("OK")
        }
      case "username" =>
        val name = if (value.isEmpty) { None } else { Some(value) }
        Database.execute(UserQueries.SetUsername(request.identity.id, name)).map { i =>
          UserCache.removeUser(request.identity.id)
          Ok("OK")
        }.recoverWith {
          case x => Future.successful(Ok("ERROR:That username is in use."))
        }
      case _ => throw new IllegalArgumentException(s"Invalid option [$option] with value [$value].")
    }
  }
}
