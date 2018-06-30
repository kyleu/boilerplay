package models.sandbox

import better.files._
import graphql.GraphQLService
import models.Application
import models.auth.Credentials
import util.FutureUtils.serviceContext
import util.tracing.TraceData

import scala.sys.process._
import scala.util.control.NonFatal

object GraphdocLogic {
  private[this] val d = "./doc/src/main/graphdoc".toFile

  def generate(creds: Credentials, app: Application, graphQLService: GraphQLService, argument: Option[String])(implicit td: TraceData) = {
    checkInstalled()
    if (argument.contains("force") && !d.exists) {
      d.createDirectories()
    }
    if (d.isDirectory) {
      run(creds, app, graphQLService)
    } else {
      throw new IllegalStateException(s"""Directory [${d.path}] doe not exist, pass "force" as an argument to create.""")
    }
  }

  private[this] val queryFilename = "IntrospectionQuery.graphql"
  private[this] val schemaFilename = "schema.json"

  private[this] lazy val introspectionQuery = Option(getClass.getClassLoader.getResourceAsStream(queryFilename)) match {
    case Some(q) => scala.io.Source.fromInputStream(q).mkString
    case None => throw new IllegalStateException(s"Cannot read [$queryFilename].")
  }

  private[this] def writeIntrospectionResult(creds: Credentials, app: Application, graphQLService: GraphQLService)(implicit td: TraceData) = try {
    val f = d / schemaFilename
    if (!f.exists) {
      f.createFile()
    }
    graphQLService.executeQuery(app, introspectionQuery, None, None, creds, debug = false).map(_.spaces2).map { json =>
      f.overwrite(json)
    }
  } catch {
    case NonFatal(x) => throw new IllegalStateException(s"Cannot run graphdoc. Is it installed? (${x.getMessage})", x)
  }

  private[this] def run(creds: Credentials, app: Application, graphQLService: GraphQLService)(implicit td: TraceData) = {
    writeIntrospectionResult(creds, app, graphQLService).map { _ =>
      val args = Seq("graphdoc", "--force", "-s", (d / schemaFilename).pathAsString, "-o", d.pathAsString)
      try {
        args.!!
      } catch {
        case NonFatal(x) => throw new IllegalStateException(s"Cannot run [$args]. (${x.getMessage})", x)
      }
    }
  }

  private[this] def checkInstalled() = try {
    Seq("graphdoc", "--help").!!
  } catch {
    case NonFatal(x) => throw new IllegalStateException(s"Cannot run graphdoc. Is it installed? (${x.getMessage})", x)
  }
}
