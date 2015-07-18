package utils

import _root_.play.api.i18n.Messages

object NumberUtils {
  def toWords(i: Int, properCase: Boolean = false)(implicit messages: Messages) = if (messages.lang.language == "en") {
    val ret = i match {
      case 0 => "zero"
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
      case 6 => "six"
      case 7 => "seven"
      case 8 => "eight"
      case 9 => "nine"
      case 10 => "ten"
      case 11 => "eleven"
      case 12 => "twelve"
      case _ => i.toString
    }
    if (properCase) {
      ret.head.toUpper + ret.tail
    } else {
      ret
    }
  } else {
    i.toString
  }
}
