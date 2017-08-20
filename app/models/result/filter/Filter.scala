package models.result.filter

case class Filter(
  k: String = "?",
  o: FilterOp = FilterOp.Equal,
  v: Seq[String] = Nil
)
