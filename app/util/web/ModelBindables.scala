package util.web

import models.template.Theme
import models.user.Role
import play.api.mvc.{PathBindable, QueryStringBindable}

object ModelBindables {
  private[this] def roleExtractor(v: Either[String, String]) = v match {
    case Right(s) => Right(Role.withValue(s))
    case Left(x) => throw new IllegalStateException(x)
  }
  implicit def rolePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[Role] = new PathBindable[Role] {
    override def bind(key: String, value: String) = roleExtractor(stringBinder.bind(key, value))
    override def unbind(key: String, x: Role) = x.value
  }
  implicit def roleQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[Role] = new QueryStringBindable[Role] {
    override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map(roleExtractor)
    override def unbind(key: String, x: Role): String = x.value
  }

  private[this] def themeExtractor(v: Either[String, String]) = v match {
    case Right(s) => Right(Theme.withValue(s))
    case Left(x) => throw new IllegalStateException(x)
  }
  implicit def themePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[Theme] = new PathBindable[Theme] {
    override def bind(key: String, value: String) = themeExtractor(stringBinder.bind(key, value))
    override def unbind(key: String, x: Theme) = x.value
  }
  implicit def themeQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[Theme] = new QueryStringBindable[Theme] {
    override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map(themeExtractor)
    override def unbind(key: String, x: Theme): String = x.value
  }

  /* Start model bindables */
  /* Projectile export section [boilerplay] */
  import models.settings.SettingKeyType
  private[this] def settingKeyTypeExtractor(v: Either[String, String]) = v match {
    case Right(s) => Right(SettingKeyType.withValue(s))
    case Left(x) => throw new IllegalStateException(x)
  }
  implicit def settingKeyTypePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[SettingKeyType] = new PathBindable[SettingKeyType] {
    override def bind(key: String, value: String) = settingKeyTypeExtractor(stringBinder.bind(key, value))
    override def unbind(key: String, x: SettingKeyType) = x.value
  }
  implicit def settingKeyTypeQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[SettingKeyType] = new QueryStringBindable[SettingKeyType] {
    override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map(settingKeyTypeExtractor)
    override def unbind(key: String, x: SettingKeyType) = x.value
  }
  /* End model bindables */
}
