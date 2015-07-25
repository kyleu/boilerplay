package utils.cache

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.User

object UserCache {
  def getUser(id: UUID) = {
    CacheService.getAs[User](s"user.$id")
  }

  def cacheUser(user: User) = {
    CacheService.set(s"user.${user.id}", user)
    user
  }

  def cacheUserForLoginInfo(user: User, loginInfo: LoginInfo) = {
    CacheService.set(s"user.${user.id}", user)
    CacheService.set(s"user.${loginInfo.providerID}:${loginInfo.providerKey}", user)
  }

  def getUserByLoginInfo(loginInfo: LoginInfo) = {
    CacheService.getAs[User](s"user.${loginInfo.providerID}:${loginInfo.providerKey}")
  }

  def removeUser(id: UUID) = {
    CacheService.getAs[User](s"user.$id").foreach { u =>
      for (p <- u.profiles) {
        CacheService.remove(s"user.${p.providerID}:${p.providerKey}")
      }
    }
    CacheService.remove(s"user.$id")
    CacheService.remove(s"user.anonymous:$id")
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
