package services.audit

import java.util.UUID

object AuditArgs {
  def getArg(id: Seq[String], i: Int) = {
    if (id.length <= i) { throw new IllegalStateException(s"Needed at least [${i + 1}] id arguments, only have [${id.length}].") }
    id(i)
  }

  def stringArg(s: String) = s
  def booleanArg(s: String) = s.toBoolean
  def integerArg(s: String) = s.toInt
  def longArg(s: String) = s.toLong
  def floatArg(s: String) = s.toFloat
  def uuidArg(s: String) = UUID.fromString(s)
  def byteArg(s: String) = s.toInt.toByte
  def dateArg(s: String) = util.DateUtils.fromDateString(s)
  def timeArg(s: String) = util.DateUtils.fromTimeString(s)
  def timestampArg(s: String) = util.DateUtils.fromIsoString(s)
}
