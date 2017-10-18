package services.database

import java.time.LocalDateTime

import better.files.File
import models.ddl.DdlQueries.DdlStatement
import models.ddl.{DdlFile, DdlQueries}
import util.Logging
import util.tracing.TraceData

object MasterDdl extends Logging {
  val dir = File("./ddl")

  lazy val files = if (dir.isDirectory) {
    dir.children.filter(_.name.endsWith(".sql")).map { f =>
      val now = LocalDateTime.now()
      val split = f.name.stripSuffix(".sql").split('_')
      if (split.length != 2) {
        throw new IllegalStateException(s"Invalid filename [${f.name}].")
      }
      val index = split.headOption.getOrElse(throw new IllegalStateException()).toInt
      val name = split(1)
      val sql = f.contentAsString
      DdlFile(index, name, sql, now)
    }.toSeq.sortBy(_.id)
  } else {
    log.warn(s"Unable to read DDL directory [${dir.path}].")
    Nil
  }

  def init()(implicit trace: TraceData) = {
    if (!SystemDatabase.query(DdlQueries.DoesTableExist("ddl"))) {
      log.info("Creating DDL table.")
      SystemDatabase.execute(DdlQueries.CreateDdlTable)
    }

    val ids = SystemDatabase.query(DdlQueries.GetIds)
    log.debug(s"Found [${ids.size}/${files.size}] applied ddl files.")

    val candidates = files.filterNot(f => ids.contains(f.id))
    val result = candidates.map { f =>
      log.info(s"Applying [${f.statements.size}] statements for DDL [${f.id}:${f.name}].")
      SystemDatabase.transaction { (txTd, conn) =>
        val results = f.statements.map { sql =>
          val statement = DdlStatement(sql._1)
          log.debug("Applying DDL statement [" + statement.sql.take(64) + "...].")
          val result = SystemDatabase.execute(statement, Some(conn))(txTd)
          log.debug("Applied DDL statement [" + statement.sql.take(64) + "...].")
          result
        }
        SystemDatabase.execute(DdlQueries.insert(f))
        results
      }
    }

    s"DDL update successful. [${result.map(_.size).sum}] queries applied across [${result.size}] ddl files."
  }
}
