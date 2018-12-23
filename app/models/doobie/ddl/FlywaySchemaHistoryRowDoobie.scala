/* Generated File */
package models.doobie.ddl

import cats.data.NonEmptyList
import models.ddl.FlywaySchemaHistoryRow
import services.database.doobie.DoobieQueries
import services.database.doobie.DoobieQueryService.Imports._

object FlywaySchemaHistoryRowDoobie extends DoobieQueries[FlywaySchemaHistoryRow]("flyway_schema_history") {
  override val countFragment = fr"""select count(*) from "flyway_schema_history""""
  override val selectFragment = fr"""select "installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success" from "flyway_schema_history""""

  override val columns = Seq("installed_rank", "version", "description", "type", "script", "checksum", "installed_by", "installed_on", "execution_time", "success")
  override val searchColumns = Seq("installed_rank", "version", "description", "type", "installed_on", "success")

  override def searchFragment(q: String) = {
    fr""""installed_rank"::text = $q or "version"::text = $q or "description"::text = $q or "type"::text = $q or "script"::text = $q or "checksum"::text = $q or "installed_by"::text = $q or "installed_on"::text = $q or "execution_time"::text = $q or "success"::text = $q"""
  }

  def getByPrimaryKey(installedRank: Long) = (selectFragment ++ whereAnd(fr"installedRank = $installedRank")).query[Option[FlywaySchemaHistoryRow]].unique
  def getByPrimaryKeySeq(installedRankSeq: NonEmptyList[Long]) = (selectFragment ++ in(fr"installedRank", installedRankSeq)).query[FlywaySchemaHistoryRow].to[Seq]
}
