package com.kyleu.projectile.util

import scala.scalajs.js

object JavaScriptUtils {
  private[this] val offset = new scalajs.js.Date().getTimezoneOffset().toLong

  def niceCurrentTime() = DateUtils.niceTime(DateUtils.now.toLocalTime.plusMinutes(offset))

  def as[T <: js.Any](x: Any): T = x.asInstanceOf[T]
  def asDynamic(o: js.Any) = o.asInstanceOf[js.Dynamic]
}
