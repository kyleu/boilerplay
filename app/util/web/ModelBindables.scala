package util.web

import com.kyleu.projectile.models.user.Role
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

  /* Start model bindables */
  /* Projectile export section [boilerplay] */
  private[this] def boilerplaySettingKeyTypeExtractor(v: Either[String, String]) = v match {
    case Right(s) => Right(models.settings.SettingKeyType.withValue(s))
    case Left(x) => throw new IllegalStateException(x)
  }
  implicit def boilerplaySettingKeyTypePathBindable(implicit binder: PathBindable[String]): PathBindable[models.settings.SettingKeyType] = new PathBindable[models.settings.SettingKeyType] {
    override def bind(key: String, value: String) = boilerplaySettingKeyTypeExtractor(binder.bind(key, value))
    override def unbind(key: String, e: models.settings.SettingKeyType) = e.value
  }
  implicit def boilerplaySettingKeyTypeQueryStringBindable(implicit binder: QueryStringBindable[String]): QueryStringBindable[models.settings.SettingKeyType] = new QueryStringBindable[models.settings.SettingKeyType] {
    override def bind(key: String, params: Map[String, Seq[String]]) = binder.bind(key, params).map(boilerplaySettingKeyTypeExtractor)
    override def unbind(key: String, e: models.settings.SettingKeyType) = e.value
  }
  /* End model bindables */
}
