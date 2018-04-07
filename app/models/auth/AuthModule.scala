package models.auth

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder, Signer}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, AvatarService}
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings, JcaSigner, JcaSignerSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, CookieAuthenticatorService}
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.providers.oauth2.GoogleProvider
import com.mohiva.play.silhouette.impl.providers.state.{CsrfStateItemHandler, CsrfStateSettings}
import com.mohiva.play.silhouette.impl.services.GravatarService
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import models.Configuration
import net.codingwell.scalaguice.ScalaModule
import util.FutureUtils.defaultContext
import play.api.libs.ws.WSClient
import play.api.mvc.DefaultCookieHeaderEncoding
import services.user.{OAuth2InfoService, PasswordInfoService, SystemUserSearchService}

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
    userSearchService: SystemUserSearchService
  ): Environment[AuthEnv] = {
    Environment[AuthEnv](userSearchService, authenticatorService, Seq.empty, eventBus)
  }

  @Provides
  def provideAuthenticatorCrypter(config: Configuration): Crypter = {
    new JcaCrypter(JcaCrypterSettings(config.secretKey))
  }

  @Provides
  def provideSigner(config: Configuration): Signer = {
    new JcaSigner(JcaSignerSettings(config.secretKey))
  }

  @Provides
  def provideAuthenticatorService(
    cookieSigner: Signer, crypter: Crypter, encoding: DefaultCookieHeaderEncoding,
    fpg: FingerprintGenerator, idg: IDGenerator, config: Configuration, clock: Clock
  ): AuthenticatorService[CookieAuthenticator] = {
    val authenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)
    new CookieAuthenticatorService(config.authCookieSettings, None, cookieSigner, encoding, authenticatorEncoder, fpg, idg, clock)
  }

  @Provides
  def provideCredentialsProvider(authInfoRepository: AuthInfoRepository, passwordHasher: PasswordHasher): CredentialsProvider = {
    val passwordHasherRegisty = PasswordHasherRegistry(passwordHasher)
    new CredentialsProvider(authInfoRepository, passwordHasherRegisty)
  }

  @Provides
  def provideAuthInfoRepository(passwordInfoService: PasswordInfoService, oauth2InfoService: OAuth2InfoService): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoService, oauth2InfoService)
  }

  @Provides
  def provideAvatarService(httpLayer: HTTPLayer): AvatarService = new GravatarService(httpLayer)

  @Provides
  def provideCsrfStateItemHandler(idGenerator: IDGenerator, signer: Signer, configuration: Configuration): CsrfStateItemHandler = {
    new CsrfStateItemHandler(CsrfStateSettings(secureCookie = false), idGenerator, signer)
  }

  @Provides
  def provideSocialStateHandler(csrfStateItemHandler: CsrfStateItemHandler, signer: Signer): SocialStateHandler = {
    new DefaultSocialStateHandler(Set(csrfStateItemHandler), signer)
  }

  @Provides
  def provideGoogleProvider(httpLayer: HTTPLayer, socialStateHandler: SocialStateHandler, config: Configuration): GoogleProvider = {
    new GoogleProvider(httpLayer, socialStateHandler, config.authGoogleSettings)
  }

  @Provides
  def provideSocialProviderRegistry(googleProvider: GoogleProvider) = {
    SocialProviderRegistry(Seq(googleProvider))
  }
}
