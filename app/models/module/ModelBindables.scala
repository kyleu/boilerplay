package models.module

import play.api.mvc.{PathBindable, QueryStringBindable}

object ModelBindables {
  /* Start model bindables */
  /* Projectile export section [boilerplay] */
  private[this] def boilerplayMpaaRatingTypeExtractor(v: Either[String, String]) = v match {
    case Right(s) => Right(models.film.MpaaRatingType.withValue(s))
    case Left(x) => throw new IllegalStateException(x)
  }
  implicit def boilerplayMpaaRatingTypePathBindable(implicit binder: play.api.mvc.PathBindable[String]): play.api.mvc.PathBindable[models.film.MpaaRatingType] = new play.api.mvc.PathBindable[models.film.MpaaRatingType] {
    override def bind(key: String, value: String) = boilerplayMpaaRatingTypeExtractor(binder.bind(key, value))
    override def unbind(key: String, e: models.film.MpaaRatingType) = e.value
  }
  implicit def boilerplayMpaaRatingTypeQueryStringBindable(implicit binder: play.api.mvc.QueryStringBindable[String]): play.api.mvc.QueryStringBindable[models.film.MpaaRatingType] = new play.api.mvc.QueryStringBindable[models.film.MpaaRatingType] {
    override def bind(key: String, params: Map[String, Seq[String]]) = binder.bind(key, params).map(boilerplayMpaaRatingTypeExtractor)
    override def unbind(key: String, e: models.film.MpaaRatingType) = e.value
  }
  /* End model bindables */
}
