/* Generated File */
package models.doobie.ddl

import cats.data.NonEmptyList
import models.ddl.FlywaySchemaHistory
import services.database.DoobieQueryService.Imports._

object FlywaySchemaHistoryDoobie {
  val countFragment = fr"select count(*) from flyway_schema_history"
  val selectFragment = fr"select installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success from flyway_schema_history"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[FlywaySchemaHistory].to[Seq]

  def getByPrimaryKey(installedRank: Long) = (selectFragment ++ fr"where installedRank = $installedRank").query[Option[FlywaySchemaHistory]].unique
  def getByPrimaryKeySeq(installedRankSeq: NonEmptyList[Long]) = (selectFragment ++ in(fr"installedRank", installedRankSeq)).query[FlywaySchemaHistory].to[Seq]
}

