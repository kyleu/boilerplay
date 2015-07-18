package services.user

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{ Clock, HTTPLayer, IDGenerator, PasswordHasher }
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.providers.oauth1.TwitterProvider
import com.mohiva.play.silhouette.impl.providers.oauth1.secrets.{ CookieSecretProvider, CookieSecretSettings }
import com.mohiva.play.silhouette.impl.providers.oauth1.services.PlayOAuth1Service
import com.mohiva.play.silhouette.impl.providers.oauth2.state.{ CookieStateProvider, CookieStateSettings }
import com.mohiva.play.silhouette.impl.providers.oauth2.{ FacebookProvider, GoogleProvider }
import play.api.Configuration

import scala.concurrent.duration._

object SocialAuthProviders {
  val providers = Seq(
    "facebook" -> "Facebook",
    "google" -> "Google",
    "twitter" -> "Twitter"
  )
}

class SocialAuthProviders(
    config: Configuration,
    httpLayer: HTTPLayer,
    hasher: PasswordHasher,
    authInfoService: AuthInfoRepository,
    credentials: CredentialsProvider,
    idGenerator: IDGenerator,
    clock: Clock
) {
  private[this] val oAuth1TokenSecretProvider = new CookieSecretProvider(CookieSecretSettings(
    cookieName = config.getString("silhouette.oauth1TokenSecretProvider.cookieName").getOrElse(throw new IllegalArgumentException()),
    cookiePath = config.getString("silhouette.oauth1TokenSecretProvider.cookiePath").getOrElse(throw new IllegalArgumentException()),
    cookieDomain = config.getString("silhouette.oauth1TokenSecretProvider.cookieDomain"),
    secureCookie = config.getBoolean("silhouette.oauth1TokenSecretProvider.secureCookie").getOrElse(throw new IllegalArgumentException()),
    httpOnlyCookie = config.getBoolean("silhouette.oauth1TokenSecretProvider.httpOnlyCookie").getOrElse(throw new IllegalArgumentException()),
    expirationTime = config.getInt("silhouette.oauth1TokenSecretProvider.expirationTime").map(_.seconds).getOrElse(throw new IllegalArgumentException())
  ), clock)

  private[this] val oAuth2StateProvider = new CookieStateProvider(CookieStateSettings(
    cookieName = config.getString("silhouette.oauth2StateProvider.cookieName").getOrElse(throw new IllegalArgumentException()),
    cookiePath = config.getString("silhouette.oauth2StateProvider.cookiePath").getOrElse(throw new IllegalArgumentException()),
    cookieDomain = config.getString("silhouette.oauth2StateProvider.cookieDomain"),
    secureCookie = config.getBoolean("silhouette.oauth2StateProvider.secureCookie").getOrElse(throw new IllegalArgumentException()),
    httpOnlyCookie = config.getBoolean("silhouette.oauth2StateProvider.httpOnlyCookie").getOrElse(throw new IllegalArgumentException()),
    expirationTime = config.getInt("silhouette.oauth2StateProvider.expirationTime").map(_.seconds).getOrElse(throw new IllegalArgumentException())
  ), idGenerator, clock)

  private[this] val facebookSettings = OAuth2Settings(
    authorizationURL = config.getString("silhouette.facebook.authorizationUrl"),
    accessTokenURL = config.getString("silhouette.facebook.accessTokenUrl").getOrElse(throw new IllegalArgumentException()),
    redirectURL = config.getString("silhouette.facebook.redirectURL").getOrElse(throw new IllegalArgumentException()),
    clientID = config.getString("silhouette.facebook.clientId").getOrElse(throw new IllegalArgumentException()),
    clientSecret = config.getString("silhouette.facebook.clientSecret").getOrElse(throw new IllegalArgumentException()),
    scope = config.getString("silhouette.facebook.scope")
  )

  private[this] val facebook = new FacebookProvider(httpLayer, oAuth2StateProvider, facebookSettings)

  private[this] val googleSettings = OAuth2Settings(
    authorizationURL = config.getString("silhouette.google.authorizationUrl"),
    accessTokenURL = config.getString("silhouette.google.accessTokenUrl").getOrElse(throw new IllegalArgumentException()),
    redirectURL = config.getString("silhouette.google.redirectUrl").getOrElse(throw new IllegalArgumentException()),
    clientID = config.getString("silhouette.google.clientId").getOrElse(throw new IllegalArgumentException()),
    clientSecret = config.getString("silhouette.google.clientSecret").getOrElse(throw new IllegalArgumentException()),
    scope = config.getString("silhouette.google.scope")
  )

  private[this] val google = new GoogleProvider(httpLayer, oAuth2StateProvider, googleSettings)

  private[this] val twitterSettings = OAuth1Settings(
    requestTokenURL = config.getString("silhouette.twitter.requestTokenUrl").getOrElse(throw new IllegalArgumentException()),
    accessTokenURL = config.getString("silhouette.twitter.accessTokenUrl").getOrElse(throw new IllegalArgumentException()),
    authorizationURL = config.getString("silhouette.twitter.authorizationUrl").getOrElse(throw new IllegalArgumentException()),
    callbackURL = config.getString("silhouette.twitter.callbackUrl").getOrElse(throw new IllegalArgumentException()),
    consumerKey = config.getString("silhouette.twitter.consumerKey").getOrElse(throw new IllegalArgumentException()),
    consumerSecret = config.getString("silhouette.twitter.consumerSecret").getOrElse(throw new IllegalArgumentException())
  )

  private[this] val twitter = new TwitterProvider(httpLayer, new PlayOAuth1Service(twitterSettings), oAuth1TokenSecretProvider, twitterSettings)

  val providers = Seq("credentials" -> credentials, "facebook" -> facebook, "google" -> google, "twitter" -> twitter)
}
