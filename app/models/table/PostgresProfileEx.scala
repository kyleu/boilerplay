package models.table

import com.github.tminglei.slickpg._

trait PostgresProfileEx extends ExPostgresProfile
  with PgArraySupport with PgDate2Support with PgRangeSupport with PgHStoreSupport
  with PgCirceJsonSupport with PgSearchSupport with PgNetSupport with PgLTreeSupport {
  def pgjson = "jsonb"

  override protected def computeCapabilities = super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api = APIEx

  object APIEx extends API
    with ArrayImplicits with DateTimeImplicits with JsonImplicits with NetImplicits
    with LTreeImplicits with RangeImplicits with HStoreImplicits with SearchImplicits with SearchAssistants
}

object PostgresProfileEx extends PostgresProfileEx
