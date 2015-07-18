package utils.cache

import play.twirl.api.Html

object TemplateCache {
  def cacheTemplate(key: String, html: Html) = {
    CacheService.set("template." + key, html)
    html
  }

  def getTemplate(key: String) = {
    CacheService.getAs[Html]("template." + key)
  }
}
