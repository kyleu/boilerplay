package models.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.RichUser

trait AuthEnv extends Env {
  type I = RichUser
  type A = CookieAuthenticator
}
