package models.auth

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder, Signer}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings, JcaSigner, JcaSignerSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, CookieAuthenticatorService}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import models.Configuration
import net.codingwell.scalaguice.ScalaModule
import util.FutureUtils.defaultContext
import play.api.libs.ws.WSClient
import play.api.mvc.DefaultCookieHeaderEncoding
import services.user.{PasswordInfoService, UserSearchService}

object AuthModule {
  val encKey = "dead60d84c1a41648ae258b57e8b5727"
}

class AuthModule extends AbstractModule with ScalaModule {
  override def configure() = {
    bind[Silhouette[AuthEnv]].to[SilhouetteProvider[AuthEnv]]
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher())
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    //bind[].to[]
  }

  @Provides
  def provideHTTPLayer(client: WSClient): HTTPLayer = new PlayHTTPLayer(client)

  @Provides
  def provideEnvironment(
    authenticatorService: AuthenticatorService[CookieAuthenticator],
    eventBus: EventBus,
    userSearchService: UserSearchService
  ): Environment[AuthEnv] = {
    Environment[AuthEnv](userSearchService, authenticatorService, Seq.empty, eventBus)
  }

  @Provides
  def provideAuthenticatorCrypter(): Crypter = {
    val settings = JcaCrypterSettings(AuthModule.encKey)
    new JcaCrypter(settings)
  }

  @Provides
  def provideCookieSigner(): Signer = {
    val config = JcaSignerSettings(AuthModule.encKey)
    new JcaSigner(config)
  }

  @Provides
  def provideAuthenticatorService(
    cookieSigner: Signer, crypter: Crypter, encoding: DefaultCookieHeaderEncoding,
    fpg: FingerprintGenerator, idg: IDGenerator, config: Configuration, clock: Clock
  ): AuthenticatorService[CookieAuthenticator] = {
    val authenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)
    new CookieAuthenticatorService(config.cookieAuthSettings, None, cookieSigner, encoding, authenticatorEncoder, fpg, idg, clock)
  }

  @Provides
  def provideCredentialsProvider(authInfoRepository: AuthInfoRepository, passwordHasher: PasswordHasher): CredentialsProvider = {
    val passwordHasherRegisty = PasswordHasherRegistry(passwordHasher)
    new CredentialsProvider(authInfoRepository, passwordHasherRegisty)
  }

  @Provides
  def provideAuthInfoRepository(passwordInfoService: PasswordInfoService): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoService)
  }
}
