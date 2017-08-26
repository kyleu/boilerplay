package models.result.filter

case class Filter(
  k: String = "?",
  o: FilterOp = Equal,
  v: Seq[String] = Nil
)
