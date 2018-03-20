package models.rest.http

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed abstract class UserAgent(val v: String) extends EnumEntry

object UserAgent extends Enum[UserAgent] with CirceEnum[UserAgent] {
  case object Default extends UserAgent("RestAgent")
  case object Firefox extends UserAgent(
    v = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:58.0) Gecko/20100101 Firefox/58.0"
  )
  case object Chrome extends UserAgent(
    v = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36"
  )
  case object Safari extends UserAgent(
    v = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6"
  )

  override val values = findValues
}
