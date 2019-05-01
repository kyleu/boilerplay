package services.database

import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.DateUtils
import services.file.FileService

import scala.sys.process._

object BackupRestore {
  private[this] val backupRoot = FileService.getDir("backup")

  def backup(db: JdbcDatabase) = {
    val filename = s"${util.Config.projectId}-backup-${DateUtils.today.toString}.sql"
    val f = backupRoot / filename
    if (f.exists) {
      throw new IllegalStateException(s"File [$filename] already exists.")
    }

    val zipFilename = filename + ".gz"
    val zf = backupRoot / zipFilename
    if (zf.exists) {
      throw new IllegalStateException(s"File [$zipFilename] already exists.")
    }

    val path = (backupRoot / filename).path.toAbsolutePath.toString
    val host = db.getConfig.host
    val schema = db.getConfig.database.getOrElse(util.Config.projectId)
    val dumpResult = Seq("pg_dump", "-Fp", "-h", host, "-O", "-f", path, "-d", schema).!!
    if (!f.exists) {
      throw new IllegalStateException(s"Backup to file [$filename] failed: [$dumpResult].")
    }

    val gzipResult = Seq("gzip", path).!!
    if (!zf.exists) {
      throw new IllegalStateException(s"Compression of file [$zipFilename] failed: [$gzipResult].")
    }

    zipFilename
  }

  def restore(key: String) = {
    key
  }
}
