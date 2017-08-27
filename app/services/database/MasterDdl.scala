package services.database

import models.ddl.DdlQueries.DdlStatement
import models.ddl.{DdlFile, DdlQueries}
import java.time.LocalDateTime

import better.files.File
import util.Logging
import util.FutureUtils.databaseContext
import util.tracing.TraceData

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

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
    Nil
  }

  def init()(implicit trace: TraceData) = {
    val withDdlTable = SystemDatabase.query(DdlQueries.DoesTableExist("ddl")).flatMap {
      case true => Future.successful(0)
      case false => SystemDatabase.execute(DdlQueries.CreateDdlTable)
    }

    val withData = withDdlTable.flatMap(_ => SystemDatabase.query(DdlQueries.GetIds))

    val appliedFiles = withData.map { data =>
      val candidates = files.filterNot(f => data.contains(f.id))
      val applied = candidates.map { f =>
        log.info(s"Applying [${f.statements.size}] statements for DDL [${f.id}:${f.name}].")
        val tx = SystemDatabase.transaction { (txTd, conn) =>
          f.statements.map { sql =>
            val statement = DdlStatement(sql._1)
            Await.result(SystemDatabase.execute(statement, Some(conn))(txTd), 5.seconds)
          }
          SystemDatabase.execute(DdlQueries.insert(f)).map(_ => f)
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
