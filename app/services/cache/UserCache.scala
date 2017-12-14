package services.cache

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.SystemUser

object UserCache {
  def getUser(id: UUID) = {
    CacheService.getAs[SystemUser](s"user.$id")
  }

  def cacheUser(user: SystemUser) = {
    CacheService.set(s"user.${user.id}", user)
    CacheService.set(s"user.${user.profile.providerKey}", user)
    user
  }

  def getUserByLoginInfo(loginInfo: LoginInfo) = {
    CacheService.getAs[SystemUser](s"user.${loginInfo.providerKey}")
  }

  def removeUser(id: UUID) = {
    CacheService.getAs[SystemUser](s"user.$id").foreach { u =>
      CacheService.remove(s"user.${u.profile.providerKey}")
    }
    CacheService.remove(s"user.$id")
  }

  def cacheSession(session: CookieAuthenticator) = {
    CacheService.set(session.id, session)
    session
  }

  def getSession(id: String) = {
    CacheService.getAs[CookieAuthenticator](id)
  }

  def removeSession(id: String) = {
    CacheService.remove(id)
  }
}
