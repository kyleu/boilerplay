package models.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.SystemUser

trait AuthEnv extends Env {
  type I = SystemUser
  type A = CookieAuthenticator
}
