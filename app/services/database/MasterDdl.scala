package services.database

import java.nio.charset.Charset

import models.ddl.DdlQueries.DdlStatement
import models.ddl.{DdlFile, DdlQueries}
import org.apache.commons.io.FileUtils
import org.joda.time.LocalDateTime
import utils.Logging
import utils.FutureUtils.defaultContext

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object MasterDdl extends Logging {
  val dir = new java.io.File("./ddl")

  lazy val files = if (dir.isDirectory) {
    dir.listFiles.filter(_.getName.endsWith(".sql")).map { f =>
      val now = new LocalDateTime()
      val split = f.getName.stripSuffix(".sql").split('_')
      if (split.length != 2) {
        throw new IllegalStateException(s"Invalid filename [${f.getName}].")
      }
      val index = split.headOption.getOrElse(throw new IllegalStateException()).toInt
      val name = split(1)
      val sql = FileUtils.readFileToString(f, Charset.defaultCharset)
      DdlFile(index, name, sql, now)
    }.sortBy(_.id).toSeq
  } else {
    Nil
  }

  def init() = {
    val withDdlTable = Database.query(DdlQueries.DoesTableExist("ddl")).flatMap {
      case true => Future.successful(0)
      case false => Database.execute(DdlQueries.CreateDdlTable)
    }

    val withData = withDdlTable.flatMap(_ => Database.query(DdlQueries.getAll))

    val appliedFiles = withData.map { data =>
      val candidates = files.filterNot(f => data.exists(_.id == f.id))
      val applied = candidates.map { f =>
        log.info(s"Applying [${f.statements.size}] statements for DDL [${f.id}:${f.name}].")
        val tx = Database.transaction { conn =>
          f.statements.map { sql =>
            val statement = DdlStatement(sql._1)
            Await.result(Database.execute(statement, Some(conn)), 5.seconds)
          }
          Database.execute(DdlQueries.insert(f)).map(_ => f)
        }
        Await.result(tx, 30.seconds)
      }
      applied
    }

    appliedFiles.map { result =>
      s"DDL update successful. [${result.map(_.statements.size).sum}] queries applied across [${result.size}] ddl files."
    }
  }
}
