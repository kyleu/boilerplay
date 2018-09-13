package services.database

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}

import cats.Reducible
import cats.effect.IO
import doobie.free._
import doobie.implicits._
import doobie.postgres.{Instances => PInstances}
import doobie.syntax.AllSyntax
import doobie.util.meta.MetaInstances
import doobie.util.transactor.Transactor
import javax.sql.DataSource
import util.tracing.TracingService

object DoobieQueryService {
  object Imports extends PInstances with Instances with AllSyntax with doobie.Aliases with doobie.hi.Modules with Modules with Types with MetaInstances {

    import scala.language.higherKinds

    implicit val localDateTimeMeta: Meta[LocalDateTime] = Meta[Timestamp].xmap(ts => ts.toLocalDateTime, dt => Timestamp.from(dt.toInstant(ZoneOffset.UTC)))
    implicit val zonedDateTimeMeta: Meta[ZonedDateTime] = Meta[Timestamp].xmap(ts => ZonedDateTime.from(ts.toLocalDateTime), dt => Timestamp.from(dt.toInstant))

    def in[F[_]: Reducible, A: Param](f: Fragment, fs: F[A]): Fragment = doobie.Fragments.in(f, fs)
    def notIn[F[_]: Reducible, A: Param](f: Fragment, fs: F[A]): Fragment = doobie.Fragments.notIn(f, fs)
    def whereAndOpt(fs: Option[Fragment]*) = doobie.Fragments.whereAndOpt(fs: _*)
    def whereAnd(fs: Fragment*) = doobie.Fragments.whereAnd(fs: _*)
  }
}

class DoobieQueryService(key: String, dataSource: DataSource, tracingService: TracingService) {
  val db = Transactor.fromDataSource[IO](ApplicationDatabase.source)

  def runSync[T](x: doobie.ConnectionIO[T]) = x.transact(db).unsafeRunSync
}
