package models.table

import java.util.UUID

import com.github.tminglei.slickpg._

trait PostgresProfileEx extends ExPostgresProfile
  with PgArraySupport with PgDate2Support /* with PgRangeSupport */ with PgHStoreSupport
  with PgCirceJsonSupport with PgSearchSupport with PgNetSupport with PgLTreeSupport {
  def pgjson = "jsonb"

  override protected def computeCapabilities = super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api = APIEx

  object APIEx extends API
    with ArrayImplicits with DateTimeImplicits with JsonImplicits with NetImplicits
    with LTreeImplicits /* with RangeImplicits */ with HStoreImplicits with SearchImplicits with SearchAssistants {

    implicit val intSeqTypeMapper: DriverJdbcType[Seq[Int]] = new SimpleArrayJdbcType[Int]("int").to(_.toSeq)
    implicit val longSeqTypeMapper: DriverJdbcType[Seq[Long]] = new SimpleArrayJdbcType[Long]("int").to(_.toSeq)
    implicit val uuidSeqTypeMapper: DriverJdbcType[Seq[UUID]] = new SimpleArrayJdbcType[UUID]("uuid").to(_.toSeq)
    implicit val strSeqTypeMapper: DriverJdbcType[Seq[String]] = new SimpleArrayJdbcType[String]("text").to(_.toSeq)
  }
}

object PostgresProfileEx extends PostgresProfileEx
