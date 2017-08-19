package models.result.paging

object PagingOptions {
  case class Range(start: Int, end: Int)

  def from(count: Int, limit: Option[Int], offset: Option[Int] = None) = {
    val off = offset.getOrElse(0)
    val lim = if (limit.contains(0)) { None } else { limit }
    val current = lim match {
      case Some(l) if l > 0 => off / l
      case None if count > 0 => off / count
      case _ => 0
    }
    PagingOptions(
      current = current,
      limit = limit,

      next = lim.flatMap(l => if (count > (off + l)) { Some(current + 1) } else { None }),
      previous = if (current == 0) { None } else { Some(current - 1) },
      pages = lim.map(l => count / l).getOrElse(1),
      itemsPerPage = lim.getOrElse(count),

      range = Range(off, off + lim.getOrElse(count)),

      total = count
    )
  }
}

case class PagingOptions(
    current: Int = 1,
    limit: Option[Int] = None,

    next: Option[Int] = None,
    previous: Option[Int] = None,
    pages: Int = 1,
    itemsPerPage: Int = 10,

    range: PagingOptions.Range = PagingOptions.Range(0, 0),

    total: Int = 1
) {
  lazy val sqlClause = s"limit $limit offset ${current * itemsPerPage}"
}
