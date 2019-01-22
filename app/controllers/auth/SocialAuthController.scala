package controllers.auth

import java.util.UUID

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.user.{Role, SystemUser}
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, Silhouette}
import com.mohiva.play.silhouette.impl.providers.{CommonSocialProfileBuilder, SocialProvider, SocialProviderRegistry}

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.{AuthEnv, UserCredentials}
import models.settings.SettingKeyType
import services.settings.SettingsService
import services.user.{SystemUserSearchService, SystemUserService}

import scala.concurrent.Future

@javax.inject.Singleton
class SocialAuthController @javax.inject.Inject() (
    override val app: Application,
    silhouette: Silhouette[AuthEnv],
    userService: SystemUserService,
    userSearchService: SystemUserSearchService,
    authInfoRepository: AuthInfoRepository,
    socialProviderRegistry: SocialProviderRegistry,
    settings: SettingsService
) extends AuthController("socialAuth") {
  def authenticate(provider: String) = withoutSession("form") { implicit request => implicit td =>
    socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        val rsp = p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => p.retrieveProfile(authInfo).flatMap { profile =>
            val li = LoginInfo(profile.loginInfo.providerID, profile.loginInfo.providerKey)

            val userF = userSearchService.getByLoginInfo(li).flatMap {
              case Some(u) => userService.updateUser(UserCredentials(u, request.remoteAddress), u)
              case None =>
                val username = profile.fullName.orElse(profile.firstName).orElse(profile.email).getOrElse(profile.loginInfo.providerKey)
                userService.getByUsername(UserCredentials.system, username).flatMap { existing =>
                  val newUser = SystemUser(
                    id = UUID.randomUUID,
                    username = if (existing.isDefined) { username + "-" + scala.util.Random.alphanumeric.take(4).mkString } else { username },
                    profile = profile.loginInfo,
                    role = Role.withValue(settings(SettingKeyType.DefaultNewUserRole))
                  )
                  userService.insert(UserCredentials(newUser, request.remoteAddress), newUser)
                }
            }

            for {
              user <- userF
              _ <- authInfoRepository.save(profile.loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
              value <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(value, Redirect(controllers.routes.HomeController.home()))
            } yield {
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
          }
        }

        rsp.recover {
          case e: ProviderException =>
            log.error("Unexpected provider error.", e)
            Redirect(controllers.auth.routes.AuthenticationController.signInForm()).flashing("error" -> "Could not authenticate.")
        }
      case _ => throw new IllegalStateException(s"Invalid provider [$provider].")
    }
  }
}
