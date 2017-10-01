package models.database

trait Row {
  def asOpt[T](s: String): Option[T]
  def as[T](s: String): T
}
