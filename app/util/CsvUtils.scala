package util

import java.io.ByteArrayOutputStream

import models.database.DatabaseField
import util.tracing.TraceData
import com.github.tototoshi.csv.CSVWriter

object CsvUtils {
  def csvFor(name: Option[String], totalCount: Int, records: Seq[Product], fields: Seq[DatabaseField])(trace: TraceData) = {
    val os = new ByteArrayOutputStream()
    val writer = CSVWriter.open(os)

    writer.writeRow(fields.map(_.prop))
    records.foreach(r => writer.writeRow(r.productIterator.toSeq.map {
      case o: Option[_] => o.getOrElse(NullUtils.inst)
      case x => x
    }))
    name.foreach { n =>
      val amt = util.NumberUtils.withCommas(records.size)
      val totes = util.NumberUtils.withCommas(totalCount)
      writer.writeRow(Seq(
        s"# $n export with $amt out of $totes results, generated ${util.DateUtils.niceDateTime(util.DateUtils.now)}."
      ))
    }

    new String(os.toByteArray)
  }
}
