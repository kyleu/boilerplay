package services.database

import models.database.Statement
import models.database.queries.ddl.DdlQueries
import models.database.queries.ddl._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.Logging

import scala.concurrent.Future

object Schema extends Logging {
  val tables = Seq(
    "users" -> CreateUsersTable,

    "user_profiles" -> CreateUserProfilesTable,
    "password_info" -> CreatePasswordInfoTable,
    "oauth1_info" -> CreateOAuth1InfoTable,
    "oauth2_info" -> CreateOAuth2InfoTable,
    "openid_info" -> CreateOpenIdInfoTable,
    "session_info" -> CreateSessionInfoTable,

    "requests" -> CreateRequestsTable,
    "client_trace" -> CreateClientTraceTable,

    "user_feedback" -> CreateUserFeedbackTable,
    "user_feedback_notes" -> CreateUserFeedbackNotesTable,

    "daily_metrics" -> CreateDailyMetricsTable,

    "adhoc_queries" -> CreateAdHocQueriesTable
  )

  def update() = {
    val tableFuture = tables.foldLeft(Future.successful(Unit)) { (f, t) =>
      f.flatMap { u =>
        Database.query(DdlQueries.DoesTableExist(t._1)).flatMap { exists =>
          if (exists) {
            Future.successful(Unit)
          } else {
            log.info(s"Creating missing table [${t._1}].")
            val name = s"CreateTable-${t._1}"
            Database.raw(name, t._2.sql).map(x => Unit)
          }
        }
      }
    }

    tableFuture.flatMap { ok =>
      createUser(Database.query(DdlQueries.DoesTestUserExist), DdlQueries.InsertTestUser)
    }
  }

  def wipe() = {
    log.warn("Wiping database schema.")
    val tableNames = tables.reverse.map(_._1)
    Database.execute(DdlQueries.TruncateTables(tableNames)).map(x => tableNames)
  }

  private[this] def createUser(q: Future[Boolean], insert: Statement) = q.flatMap { exists =>
    if (exists) {
      Future.successful(Unit)
    } else {
      log.info(s"Creating user [${insert.getClass.getSimpleName}].")
      Database.execute(insert).map(x => x == 1)
    }
  }
}
